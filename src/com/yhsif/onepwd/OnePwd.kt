package com.yhsif.onepwd

import android.app.usage.UsageStatsManager
import android.app.usage.UsageStatsManager.INTERVAL_DAILY
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

import com.yhsif.onepwd.settings.SettingsActivity

class OnePwd
: AppCompatActivity()
, View.OnClickListener
, View.OnFocusChangeListener
, TextView.OnEditorActionListener
, RadioGroup.OnCheckedChangeListener {
  companion object {
    private const val USAGE_TIMEFRAME = 24 * 60 * 60 * 1000 // 24 hours
    private const val PREF = "com.yhsif.onepwd"
    private const val KEY_SELECTED_LENGTH = "selected_length"
    private const val PKG_SELF = "com.yhsif.onepwd"

    private val CHROME_PACKAGES = setOf(
        "com.chrome.canary",  // canary
        "com.chrome.dev",     // dev
        "com.chrome.beta",    // beta
        "com.android.chrome") // stable
    private val EMPTY_CLIP = ClipData.newPlainText("", "")
  }

  var lengthGroup: RadioGroup? = null
  var master: TextView? = null
  var site: TextView? = null
  var password: TextView? = null
  var radioButtons: List<RadioButton> = listOf()
  var checkedLength: RadioButton? = null
  var checkedIndex: Int = 0


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    findViewById(R.id.generate).setOnClickListener(this)
    findViewById(R.id.settings).setOnClickListener(this)
    findViewById(R.id.close).setOnClickListener(this)

    lengthGroup = findViewById(R.id.length_group) as RadioGroup
    lengthGroup?.setOnCheckedChangeListener(this)

    master = findViewById(R.id.master_key) as TextView
    master?.setOnFocusChangeListener(this)
    master?.setOnEditorActionListener(this)

    site = findViewById(R.id.site_key) as TextView
    site?.setOnFocusChangeListener(this)
    site?.setOnEditorActionListener(this)

    password = findViewById(R.id.password) as TextView

    radioButtons = listOf(
        findViewById(R.id.length1) as RadioButton,
        findViewById(R.id.length2) as RadioButton,
        findViewById(R.id.length3) as RadioButton,
        findViewById(R.id.length4) as RadioButton)
    checkedIndex = radioButtons.size - 1
    checkedLength = radioButtons[checkedIndex]
  }

  override fun onPause() {
    super.onPause()

    getSharedPreferences(PREF, 0).edit().let { editor ->
      editor.putInt(KEY_SELECTED_LENGTH, checkedIndex)
      editor.commit()
    }
  }

  override fun onResume() {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    SettingsActivity.sortLengths(pref)
    radioButtons[0].setText(
        pref.getInt(
            SettingsActivity.KEY_LENGTH1,
            SettingsActivity.DEFAULT_LENGTH1).toString(10))
    radioButtons[1].setText(
        pref.getInt(
            SettingsActivity.KEY_LENGTH2,
            SettingsActivity.DEFAULT_LENGTH2).toString(10))
    radioButtons[2].setText(
        pref.getInt(
            SettingsActivity.KEY_LENGTH3,
            SettingsActivity.DEFAULT_LENGTH3).toString(10))
    radioButtons[3].setText(
        pref.getInt(
            SettingsActivity.KEY_LENGTH4,
            SettingsActivity.DEFAULT_LENGTH4).toString(10))

    NotificationService.run(this)

    val defaultIndex = radioButtons.size - 1
    var index =
      getSharedPreferences(PREF, 0).getInt(KEY_SELECTED_LENGTH, defaultIndex)
    if (index !in 1..radioButtons.size) {
      index = defaultIndex
    }
    lengthGroup?.check(radioButtons[index].getId())

    var siteKey: String? = null
    if (getIntent()?.getAction() == Intent.ACTION_SEND) {
      siteKey = getSiteKeyFromIntent(intent)
    }
    if (siteKey == null) {
      siteKey = getSiteKeyFromForegroundApp()
    }
    if (siteKey != null) {
      site?.setText(siteKey)
    }
    master?.setText("")
    password?.setText("")

    super.onResume()
  }

  // for View.OnClickListener
  override fun onClick(v: View) {
    when (v.getId()) {
      R.id.generate -> doGenerate()
      R.id.close -> this.finish()
      R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
    }
  }

  // for View.OnFocusChangeListener
  override fun onFocusChange(v: View, hasFocus: Boolean) {
    if (hasFocus) {
      when (v.getId()) {
        master!!.getId() -> {
          if (site!!.getText().toString().trim().isEmpty()) {
            master?.setImeOptions(EditorInfo.IME_ACTION_NEXT)
          } else {
            master?.setImeOptions(EditorInfo.IME_ACTION_SEND)
          }
        }
        site!!.getId() -> {
          if (master!!.getText().toString().isEmpty()) {
            site?.setImeOptions(EditorInfo.IME_ACTION_NEXT)
          } else {
            site?.setImeOptions(EditorInfo.IME_ACTION_SEND)
          }
        }
      }
    }
  }

  // for TextView.OnEditorActionListener
  override fun onEditorAction(
      v: TextView, actionId: Int, event: KeyEvent?): Boolean {
    if ((v.getId() == master?.getId() || v.getId() == site?.getId())
        && actionId == EditorInfo.IME_ACTION_SEND) {
      doGenerate()
      return true
    }
    return false
  }

  // for RadioGroup.OnCheckedChangeListener
  override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
    checkedLength = findViewById(checkedId) as RadioButton
    checkedIndex = radioButtons.indexOf(checkedLength!!)
  }

  private fun doGenerate() {
    val length = checkedLength?.getText().toString().toInt()
    val masterKey = master?.getText().toString()
    if (masterKey.isEmpty()) {
      Toast
        .makeText(this, R.string.empty_master_toast, Toast.LENGTH_LONG)
        .show()
      return
    }

    val siteKey = site?.getText().toString().trim()
    site?.setText(siteKey)
    val array = HmacMd5.hmac(masterKey, siteKey)
    val value =
        Base64.encodeToString(array, Base64.URL_SAFE or Base64.NO_PADDING)
            .replace("-", "")
            .replace("_", "")
            .substring(0, length)

    password?.setText(value)
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    if (pref.getBoolean(
        SettingsActivity.KEY_COPY_CLIPBOARD,
        SettingsActivity.DEFAULT_COPY_CLIPBOARD)) {
      val clip = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
      val clipData = ClipData.newPlainText("", value)
      clip.setPrimaryClip(clipData)
      Toast.makeText(this, R.string.clip_toast, Toast.LENGTH_LONG).show()
      val time = pref.getString(
          SettingsActivity.KEY_CLEAR_CLIPBOARD,
          SettingsActivity.DEFAULT_CLEAR_CLIPBOARD).toLong()
      if (time > 0) {
        Handler().postDelayed(
            Runnable() {
              if (clip.hasPrimaryClip()) {
                val item = clip.getPrimaryClip().getItemAt(0)
                if (item.getText().toString() == value) {
                  clip.setPrimaryClip(EMPTY_CLIP)
                  Toast.makeText(
                      this@OnePwd,
                      R.string.clear_clip_toast,
                      Toast.LENGTH_LONG).show()
                }
              }
            },
            time * 1000)
      }
    }
  }

  private fun getSiteKeyFromForegroundApp(): String? {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    if (!pref.getBoolean(
        SettingsActivity.KEY_PREFILL_USAGE,
        SettingsActivity.DEFAULT_PREFILL_USAGE)) {
      return null
    }

    getForegroundApp()?.let { pkg ->
      if (CHROME_PACKAGES.contains(pkg)) {
        Toast.makeText(this, R.string.chrome_toast, Toast.LENGTH_LONG).show()
      } else {
        val segments = pkg.split(".")
        if (segments.size > 1) {
          return segments[1]
        } else {
          return pkg
        }
      }
    }
    return null
  }

  private fun getForegroundApp(): String? {
    val manager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    val apps =
      manager.queryUsageStats(INTERVAL_DAILY, time - USAGE_TIMEFRAME, time)
    var max: Long = 0
    var result: String? = null
    if (apps != null) {
      for (app in apps) {
        val pkg = app.getPackageName().toLowerCase()
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

  private fun getSiteKeyFromIntent(intent: Intent): String? {
    intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
      if (url.isEmpty()) {
        return null;
      }
      Uri.parse(url).getHost()?.let { host ->
        val segments = host.split(".")
        if (segments.size > 1) {
          return segments[segments.size - 2]
        } else {
          return host
        }
      }
    }
    return null
  }
}
