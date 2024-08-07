package com.yhsif.onepwd

import android.app.KeyguardManager
import android.app.usage.UsageStatsManager
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.yhsif.onepwd.db.SiteKeyPairings
import com.yhsif.onepwd.settings.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.util.concurrent.atomic.AtomicBoolean
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class OnePwd :
  AppCompatActivity(),
  View.OnClickListener,
  View.OnFocusChangeListener,
  TextView.OnEditorActionListener,
  RadioGroup.OnCheckedChangeListener {

  companion object {
    const val KEY_NEVER_PAIRINGS = "never_pairings"

    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val KEY_STORE_KEY = "ONE_KEY_MASTER"
    private const val PREF_SECRET = "encrypted"
    private const val KEY_MASTER_ENCRYPTED = "encrypted_master"
    private const val KEY_IV = "encrypted_iv"
    private const val KEY_DEVICE_CREDENTIAL = "encrypted_allow_device_credential"

    private const val USAGE_TIMEFRAME = 24 * 60 * 60 * 1000 // 24 hours
    private const val PREF = "com.yhsif.onepwd"
    private const val KEY_SELECTED_LENGTH = "selected_length"
    private const val KEY_MIGRATED = "encrypted_migrated"
    private const val PKG_SELF = "com.yhsif.onepwd"

    private val CHROME_PACKAGES = setOf(
      "com.chrome.canary", // canary
      "com.chrome.dev", // dev
      "com.chrome.beta", // beta
      "com.android.chrome", // stable
    )
    private val EMPTY_CLIP = ClipData.newPlainText("", "")

    private val visible = AtomicBoolean(false)

    fun showToast(ctx: Context, text: String) {
      GlobalScope.launch(Dispatchers.Main) {
        val msg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
          text
        } else {
          ctx.getString(
            R.string.toast_text_template,
            ctx.getString(R.string.app_name),
            text,
          )
        }
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
      }
    }

    fun showToast(ctx: Context, rscId: Int) {
      showToast(ctx, ctx.getString(rscId))
    }
  }

  lateinit var lengthGroup: RadioGroup
  lateinit var master: EditText
  lateinit var site: EditText
  lateinit var siteFull: TextView
  lateinit var password: EditText

  lateinit var loadButton: TextView
  lateinit var storeButton: TextView

  lateinit var radioButtons: List<RadioButton>
  lateinit var checkedLength: RadioButton
  var checkedIndex: Int = 0

  var loadedMaster: String = ""
  var siteKeyFull: SiteKey = SiteKey.Empty

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    migrateEncryptedPrefs()

    findViewById<View>(R.id.generate).setOnClickListener(this)
    findViewById<View>(R.id.settings).setOnClickListener(this)
    findViewById<View>(R.id.close).setOnClickListener(this)
    findViewById<View>(R.id.master_key_load).setOnClickListener(this)
    findViewById<View>(R.id.master_key_store).setOnClickListener(this)

    lengthGroup = findViewById<RadioGroup>(R.id.length_group)
    lengthGroup.setOnCheckedChangeListener(this)

    master = findViewById<EditText>(R.id.master_key)
    master.setOnFocusChangeListener(this)
    master.setOnEditorActionListener(this)

    site = findViewById<EditText>(R.id.site_key)
    site.setOnFocusChangeListener(this)
    site.setOnEditorActionListener(this)

    siteFull = findViewById<TextView>(R.id.site_key_full)

    findViewById<View>(R.id.load_store_container).setVisibility(View.VISIBLE)

    password = findViewById<EditText>(R.id.password)

    radioButtons = listOf(
      findViewById<RadioButton>(R.id.length1),
      findViewById<RadioButton>(R.id.length2),
      findViewById<RadioButton>(R.id.length3),
      findViewById<RadioButton>(R.id.length4),
    )
    checkedIndex = radioButtons.size - 1
    checkedLength = radioButtons[checkedIndex]
  }

  override fun onPause() {
    super.onPause()

    visible.set(false)

    // Clear loaded MasterKey
    loadedMaster = ""

    getSharedPreferences(PREF, 0).edit {
      putInt(KEY_SELECTED_LENGTH, checkedIndex)
    }
  }

  override fun onResume() {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    SettingsActivity.sortLengths(pref)
    radioButtons[0].setText(
      pref.getInt(
        SettingsActivity.KEY_LENGTH1,
        SettingsActivity.DEFAULT_LENGTH1,
      ).toString(10),
    )
    radioButtons[1].setText(
      pref.getInt(
        SettingsActivity.KEY_LENGTH2,
        SettingsActivity.DEFAULT_LENGTH2,
      ).toString(10),
    )
    radioButtons[2].setText(
      pref.getInt(
        SettingsActivity.KEY_LENGTH3,
        SettingsActivity.DEFAULT_LENGTH3,
      ).toString(10),
    )
    radioButtons[3].setText(
      pref.getInt(
        SettingsActivity.KEY_LENGTH4,
        SettingsActivity.DEFAULT_LENGTH4,
      ).toString(10),
    )

    val defaultIndex = radioButtons.size - 1
    var index =
      getSharedPreferences(PREF, 0).getInt(KEY_SELECTED_LENGTH, defaultIndex)
    if (index !in 1..radioButtons.size) {
      index = defaultIndex
    }
    lengthGroup.check(radioButtons[index].getId())

    siteKeyFull = SiteKey.Empty
    if (getIntent()?.getAction() == Intent.ACTION_SEND) {
      siteKeyFull = getSiteKeyFromIntent(intent)
    }
    if (siteKeyFull == SiteKey.Empty) {
      siteKeyFull = getSiteKeyFromForegroundApp()
    }
    val full = siteKeyFull.getFull()
    if (full == "") {
      siteFull.setVisibility(View.GONE)
      site.setText("")
    } else {
      siteFull.setText(getString(R.string.site_full, full))
      siteFull.setVisibility(View.VISIBLE)

      SiteKeyPairings.getSiteKey(
        MyApp.pairingHelper!!,
        full,
      ) { siteKey ->
        if (siteKey == null) {
          site.setText(siteKeyFull.getKey())
        } else {
          site.setText(siteKey)
        }
      }
    }

    master.setText("")
    password.setText("")

    setMasterHint()
    val v = pref.getBoolean(
      SettingsActivity.KEY_BIO_AUTOLOAD,
      SettingsActivity.DEFAULT_BIO_AUTOLOAD,
    )
    if (v) {
      GlobalScope.launch(Dispatchers.Default) {
        // delay for half second to work around the biometric prompt being dismissed.
        delay(500)
        if (visible.get()) {
          GlobalScope.launch(Dispatchers.Main) {
            doLoad(false)
          }
        }
      }
    }

    visible.set(true)
    super.onResume()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
  }

  // for View.OnClickListener
  override fun onClick(v: View) {
    when (v.getId()) {
      R.id.generate -> doGenerate()
      R.id.close -> this.finish()
      R.id.settings ->
        startActivity(Intent(this, SettingsActivity::class.java))
      R.id.master_key_load -> doLoad(true)
      R.id.master_key_store -> doStore()
    }
  }

  // for View.OnFocusChangeListener
  override fun onFocusChange(v: View, hasFocus: Boolean) {
    if (hasFocus) {
      when (v.getId()) {
        master.getId() -> {
          if (site.getText().toString().trim().isEmpty()) {
            master.setImeOptions(EditorInfo.IME_ACTION_NEXT)
          } else {
            master.setImeOptions(EditorInfo.IME_ACTION_SEND)
          }
        }
        site.getId() -> {
          if (master.getText().toString().isEmpty()) {
            site.setImeOptions(EditorInfo.IME_ACTION_NEXT)
          } else {
            site.setImeOptions(EditorInfo.IME_ACTION_SEND)
          }
        }
      }
    }
  }

  // for TextView.OnEditorActionListener
  override fun onEditorAction(
    v: TextView,
    actionId: Int,
    event: KeyEvent?,
  ): Boolean {
    if (
      (v.getId() == master.getId() || v.getId() == site.getId()) &&
      actionId == EditorInfo.IME_ACTION_SEND
    ) {
      doGenerate()
      return true
    }
    return false
  }

  // for RadioGroup.OnCheckedChangeListener
  override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
    checkedLength = findViewById<RadioButton>(checkedId)
    checkedIndex = radioButtons.indexOf(checkedLength)
  }

  private fun setMasterHint() {
    if (loadedMaster.isEmpty()) {
      master.setHint(getString(R.string.hint_master))
    } else {
      master.setHint(getString(R.string.hint_master_loaded))
    }
  }

  private fun doGenerate() {
    val length = checkedLength.getText().toString().toInt()
    var masterKey = master.getText().toString()
    if (masterKey.isEmpty()) {
      if (loadedMaster.isEmpty()) {
        showToast(this, R.string.empty_master_toast)
        return
      }
      masterKey = loadedMaster
    }

    val siteKey = site.getText().toString().trim()
    site.setText(siteKey)
    val array = HmacMd5.hmac(masterKey, siteKey)
    val value =
      Base64.encodeToString(array, Base64.URL_SAFE or Base64.NO_PADDING)
        .replace("-", "")
        .replace("_", "")
        .substring(0, length)

    password.setText(value)
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    val prompt = pref.getBoolean(
      SettingsActivity.KEY_REMEMBER_PROMPT,
      SettingsActivity.DEFAULT_REMEMBER_PROMPT,
    )
    val neverSet = pref.getStringSet(KEY_NEVER_PAIRINGS, setOf())!!
    val full = siteKeyFull.getFull()
    val afterwork = { -> maybeCopyValueToClip(value, pref) }
    if (prompt && siteKeyFull != SiteKey.Empty && !neverSet.contains(full)) {
      val defSiteKey = siteKeyFull.getKey()
      SiteKeyPairings.getSiteKey(
        MyApp.pairingHelper!!,
        full,
      ) { key ->
        val insert = DialogInterface.OnClickListener() { dialog, _ ->
          SiteKeyPairings.insert(
            MyApp.pairingHelper!!,
            full,
            siteKey,
          ) {
            dialog.dismiss()
          }
        }
        val update = DialogInterface.OnClickListener() { dialog, _ ->
          SiteKeyPairings.update(
            MyApp.pairingHelper!!,
            full,
            siteKey,
          ) {
            dialog.dismiss()
          }
        }
        val delete = DialogInterface.OnClickListener() { dialog, _ ->
          SiteKeyPairings.delete(
            MyApp.pairingHelper!!,
            full,
          ) {
            dialog.dismiss()
          }
        }
        val negative = DialogInterface.OnClickListener() { dialog, _ ->
          dialog.dismiss()
        }
        val neutral = DialogInterface.OnClickListener() { dialog, _ ->
          val mutableSet = neverSet.toMutableSet()
          mutableSet.add(full)
          pref.edit {
            putStringSet(KEY_NEVER_PAIRINGS, mutableSet)
          }
          dialog.dismiss()
        }
        val builder = AlertDialog.Builder(this)
          .setCancelable(true)
          .setIcon(R.mipmap.icon_round)
          .setOnDismissListener(
            DialogInterface.OnDismissListener() { _ ->
              afterwork()
            },
          )
          .setNeutralButton(
            R.string.button_never,
            neutral,
          )
          .setNegativeButton(
            R.string.no,
            negative,
          )
        if (key == null) {
          if (siteKey == defSiteKey) {
            afterwork()
          } else {
            builder
              .setTitle(R.string.title_remember)
              .setMessage(
                getString(
                  R.string.msg_remember,
                  getString(R.string.button_never),
                  full,
                  siteKey,
                ),
              )
              .setPositiveButton(
                R.string.yes,
                insert,
              )
              .create()
              .show()
          }
        } else {
          when (siteKey) {
            key -> afterwork()
            defSiteKey ->
              builder
                .setTitle(R.string.title_delete)
                .setMessage(
                  getString(
                    R.string.msg_delete,
                    getString(R.string.button_never),
                    full,
                    key,
                  ),
                )
                .setPositiveButton(
                  R.string.yes,
                  delete,
                )
                .create()
                .show()
            else ->
              builder
                .setTitle(R.string.title_update)
                .setMessage(
                  getString(
                    R.string.msg_update,
                    getString(R.string.button_never),
                    full,
                    key,
                    siteKey,
                  ),
                )
                .setPositiveButton(
                  R.string.yes,
                  update,
                )
                .create()
                .show()
          }
        }
      }
    } else {
      afterwork()
    }
  }

  private fun clearClipboard(clip: ClipboardManager) {
    GlobalScope.launch(Dispatchers.Main) {
      clip.setPrimaryClip(EMPTY_CLIP)
      // Only show a toast for Android 12 and lower.
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        showToast(this@OnePwd, R.string.clear_clip_toast)
      }
    }
  }

  private fun maybeCopyValueToClip(value: String, pref: SharedPreferences) {
    val v = pref.getBoolean(
      SettingsActivity.KEY_COPY_CLIPBOARD,
      SettingsActivity.DEFAULT_COPY_CLIPBOARD,
    )
    if (v) {
      val clip = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
      val clipData = ClipData.newPlainText("", value)
      clipData.apply {
        description.extras = PersistableBundle().apply {
          putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
        }
      }
      clip.setPrimaryClip(clipData)
      // Only show a toast for Android 12 and lower.
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        showToast(this, R.string.clip_toast)
      }
      val time = pref.getString(
        SettingsActivity.KEY_CLEAR_CLIPBOARD,
        SettingsActivity.DEFAULT_CLEAR_CLIPBOARD,
      )?.toLong()
      if (time != null && time > 0) {
        GlobalScope.launch(Dispatchers.Default) {
          delay(time * 1000)
          if (clip.hasPrimaryClip()) {
            val item = clip.getPrimaryClip()?.getItemAt(0)
            if (item == null || item.getText().toString() == value) {
              clearClipboard(clip)
            }
          } else {
            // This most likely means we are not in foreground and thus denied
            // clipboard access, not really that there's no clip data.
            clearClipboard(clip)
          }
        }
      }
    }
  }

  private fun getSiteKeyFromForegroundApp(): SiteKey {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    val v = pref.getBoolean(
      SettingsActivity.KEY_PREFILL_USAGE,
      SettingsActivity.DEFAULT_PREFILL_USAGE,
    )
    if (!v) {
      return SiteKey.Empty
    }

    getForegroundApp().let { pkg ->
      if (pkg.isEmpty()) {
        return SiteKey.Empty
      }
      if (CHROME_PACKAGES.contains(pkg)) {
        showToast(this, R.string.chrome_toast)
      }
      return SiteKey.Package(pkg)
    }
  }

  private fun getForegroundApp(): String {
    val manager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    val apps = manager.queryUsageStats(
      UsageStatsManager.INTERVAL_DAILY,
      time - USAGE_TIMEFRAME,
      time,
    )
    var max: Long = 0
    var result: String = ""
    if (apps != null) {
      for (app in apps) {
        val pkg = app.getPackageName().lowercase()
        val timestamp = app.getLastTimeUsed()
        if (pkg == PKG_SELF) {
          continue
        }
        if (timestamp > max) {
          max = timestamp
          result = pkg
        }
      }
    }
    return result
  }

  private fun getSiteKeyFromIntent(intent: Intent): SiteKey {
    intent.getStringExtra(Intent.EXTRA_TEXT).let { url ->
      if (url != null && url.isEmpty()) {
        return SiteKey.Empty
      }
      Uri.parse(url).getHost()?.let { host ->
        return SiteKey.Host(host)
      }
    }
    return SiteKey.Empty
  }

  private val keyguardManager: KeyguardManager by lazy {
    getSystemService(KeyguardManager::class.java)
  }

  private fun doLoad(toast: Boolean) {
    if (!keyguardManager.isKeyguardSecure) {
      if (toast) {
        showToast(this, R.string.biometric_unsupported)
      }
      return
    }

    val pref = getSharedPreferences(PREF_SECRET, 0)
    val msgStr = pref.getString(KEY_MASTER_ENCRYPTED, "")
    val ivStr = pref.getString(KEY_IV, "")
    if (msgStr == "" || ivStr == "") {
      if (toast) {
        showToast(this, R.string.load_empty)
      }
      return
    }

    val msg: ByteArray
    val iv: ByteArray
    try {
      msg = Base64.decode(msgStr, Base64.DEFAULT)
      iv = Base64.decode(ivStr, Base64.DEFAULT)
    } catch (e: IllegalArgumentException) {
      if (toast) {
        showToast(this, R.string.load_empty)
      }
      return
    }

    try {
      val initCipher = decryptionCipher(KEY_STORE_KEY, iv)
      if (initCipher == null) {
        if (toast) {
          showToast(this, R.string.load_empty)
        }
        return
      }

      BioAuthHelper(
        R.string.load_title,
        initCipher,
      ) { cipher ->
        try {
          if (cipher != null) {
            loadedMaster = String(cipher.doFinal(msg))
            setMasterHint()
            if (!loadedMaster.isEmpty()) {
              master.setText("")
            }
            showToast(this, R.string.load_succeed)
          } else {
            showToast(this, R.string.load_empty_or_canceled)
          }
        } catch (e: BadPaddingException) {
          showToast(this, R.string.biometric_invalid)
        }
      }
    } catch (e: KeyPermanentlyInvalidatedException) {
      if (toast) {
        showToast(this, R.string.biometric_invalid)
      }
      return
    } catch (e: InvalidAlgorithmParameterException) {
      if (toast) {
        showToast(this, R.string.biometric_unset)
      }
      return
    }
  }

  private fun doStore() {
    if (!keyguardManager.isKeyguardSecure) {
      showToast(this, R.string.biometric_unsupported)
      return
    }
    val masterKey = master.getText().toString()
    if (masterKey.isEmpty()) {
      showToast(this, R.string.empty_master_toast)
      return
    }

    val invalidate = PreferenceManager
      .getDefaultSharedPreferences(this)
      .getBoolean(
        SettingsActivity.KEY_BIO_INVALIDATE,
        SettingsActivity.DEFAULT_BIO_INVALIDATE,
      )

    try {
      BioAuthHelper(
        R.string.store_title,
        encryptionCipher(KEY_STORE_KEY, invalidate),
      ) { cipher ->
        if (cipher != null) {
          val message = cipher.doFinal(
            masterKey.toByteArray(charset("UTF-8")),
          )
          val iv = cipher
            .getParameters()
            .getParameterSpec(IvParameterSpec::class.java)
            .iv

          val msgStr = Base64.encodeToString(message, Base64.DEFAULT)
          val ivStr = Base64.encodeToString(iv, Base64.DEFAULT)

          getSharedPreferences(PREF_SECRET, 0).edit {
            putString(KEY_MASTER_ENCRYPTED, msgStr)
            putString(KEY_IV, ivStr)
          }
          showToast(this, R.string.store_succeed)
        }
      }
    } catch (e: InvalidAlgorithmParameterException) {
      showToast(this, R.string.biometric_unset)
      return
    }
  }

  private fun createCipher(): Cipher {
    return Cipher.getInstance(
      KeyProperties.KEY_ALGORITHM_AES + "/" +
        KeyProperties.BLOCK_MODE_CBC + "/" +
        KeyProperties.ENCRYPTION_PADDING_PKCS7,
    )
  }

  private fun encryptionCipher(name: String, invalidate: Boolean): Cipher {
    val cipher = createCipher()
    cipher.init(Cipher.ENCRYPT_MODE, createKey(name, invalidate))
    return cipher
  }

  private fun decryptionCipher(name: String, iv: ByteArray): Cipher? {
    val cipher = createCipher()
    val key = getKey(name)
    if (key == null) {
      return null
    }
    cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
    return cipher
  }

  private fun getKey(name: String): SecretKey? {
    val store = KeyStore.getInstance(ANDROID_KEY_STORE)
    store.load(null)
    return store.getKey(name, null) as? SecretKey
  }

  private fun deleteKey(name: String) {
    val store = KeyStore.getInstance(ANDROID_KEY_STORE)
    store.load(null)
    store.deleteEntry(name)
  }

  private fun createKey(name: String, invalidate: Boolean): SecretKey {
    deleteKey(name)

    val builder = KeyGenParameterSpec.Builder(
      name,
      KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
    )
    builder.apply {
      setBlockModes(KeyProperties.BLOCK_MODE_CBC)
      setUserAuthenticationRequired(true)
      setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
      setInvalidatedByBiometricEnrollment(invalidate)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setUserAuthenticationParameters(0, KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL)
        getSharedPreferences(PREF_SECRET, 0).edit {
          putBoolean(KEY_DEVICE_CREDENTIAL, true)
        }
      }
    }

    KeyGenerator.getInstance(
      KeyProperties.KEY_ALGORITHM_AES,
      ANDROID_KEY_STORE,
    ).apply {
      init(builder.build())
      generateKey()
    }

    return getKey(name)!!
  }

  private fun migrateEncryptedPrefs() {
    val pref = getSharedPreferences(PREF, 0)
    if (pref.getBoolean(KEY_MIGRATED, false)) {
      // Already migrated
      return
    }
    var toRemove: Boolean = false
    if (pref.contains(KEY_MASTER_ENCRYPTED) && pref.contains(KEY_IV)) {
      toRemove = true
      val encPref = getSharedPreferences(PREF_SECRET, 0)
      if (
        !encPref.contains(KEY_MASTER_ENCRYPTED) ||
        !encPref.contains(KEY_IV)
      ) {
        val master = pref.getString(KEY_MASTER_ENCRYPTED, "")
        val iv = pref.getString(KEY_IV, "")
        encPref.edit {
          putString(KEY_MASTER_ENCRYPTED, master)
          putString(KEY_IV, iv)
        }
      }
    }
    pref.edit {
      if (toRemove) {
        remove(KEY_MASTER_ENCRYPTED)
        remove(KEY_IV)
      }
      putBoolean(KEY_MIGRATED, true)
    }
  }

  private fun BioAuthHelper(
    title: Int,
    initCipher: Cipher,
    callback: (Cipher?) -> Unit,
  ) {
    val builder = BiometricPrompt.PromptInfo.Builder()
    builder.apply {
      setTitle(getString(title))
      setConfirmationRequired(true)
      var allowedAuthenticators = Authenticators.BIOMETRIC_STRONG
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && getSharedPreferences(PREF_SECRET, 0).getBoolean(KEY_DEVICE_CREDENTIAL, false)) {
        allowedAuthenticators = allowedAuthenticators or Authenticators.DEVICE_CREDENTIAL
      } else {
        setNegativeButtonText(getString(android.R.string.cancel))
      }
      setAllowedAuthenticators(allowedAuthenticators)
    }

    BiometricPrompt(
      this@OnePwd,
      Dispatchers.IO.asExecutor(),
      object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(
          res: BiometricPrompt.AuthenticationResult,
        ) {
          GlobalScope.launch(Dispatchers.Main) {
            callback(res.getCryptoObject()?.getCipher())
          }
        }
      },
    ).authenticate(
      builder.build(),
      BiometricPrompt.CryptoObject(initCipher),
    )
  }
}
