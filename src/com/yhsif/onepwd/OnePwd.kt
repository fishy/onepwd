package com.yhsif.onepwd

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.usage.UsageStatsManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

import com.yhsif.onepwd.settings.SettingsActivity

class OnePwd
: AppCompatActivity()
, View.OnClickListener
, View.OnFocusChangeListener
, TextView.OnEditorActionListener
, RadioGroup.OnCheckedChangeListener {
  companion object {
    private const val NOTIFICATION_ID = 1
    private const val CHANNEL_ID = "quick_access"
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

    fun showToast(ctx: Context, text: String) {
      val toast = Toast.makeText(ctx, text, Toast.LENGTH_LONG)
      toast.getView()?.findViewById<TextView>(android.R.id.message)?.let { tv ->
        // Put the icon on the right
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_round, 0)
        tv.setCompoundDrawablePadding(
            ctx.getResources().getDimensionPixelSize(R.dimen.toast_padding))
      }
      toast.show()
    }

    fun showToast(ctx: Context, rscId: Int) {
      showToast(ctx, ctx.getString(rscId))
    }

    fun showNotification(ctx: Context) = showNotification(
        ctx,
        PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(
            SettingsActivity.KEY_USE_SERVICE,
            SettingsActivity.DEFAULT_USE_SERVICE))

    fun showNotification(ctx: Context, show: Boolean) {
      val manager = ctx.getSystemService(
          Context.NOTIFICATION_SERVICE) as NotificationManager
      if (!show) {
        manager.cancel(NOTIFICATION_ID)
        return
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
          && !manager.areNotificationsEnabled()) {
        return
      }
      val existing = manager.getActiveNotifications()
      if ((existing?.size ?: 0) > 0) {
        // Already has the notification
        return
      }
      val channelId: String by lazy {
        // Lazy create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          val channel = NotificationChannel(
              CHANNEL_ID,
              ctx.getString(R.string.channel_name),
              NotificationManager.IMPORTANCE_MIN)
          channel.setDescription(ctx.getString(R.string.channel_desc))
          channel.setShowBadge(false)
          manager.createNotificationChannel(channel)
        }
        CHANNEL_ID
      }
      val activity =
        PendingIntent.getActivity(ctx, 0, Intent(ctx, OnePwd::class.java), 0)
      val notification: Notification.Builder
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notification = Notification.Builder(ctx, channelId)
      } else {
        @Suppress("DEPRECATION")
        notification = Notification.Builder(ctx)
      }
      notification
        .setSmallIcon(R.drawable.notify_icon)
        .setWhen(System.currentTimeMillis())
        .setTicker(ctx.getText(R.string.ticker))
        .setContentTitle(ctx.getText(R.string.ticker))
        .setContentIntent(activity)
        .setCategory(Notification.CATEGORY_STATUS)
        .setOngoing(true)
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        @Suppress("DEPRECATION")
        notification.setPriority(Notification.PRIORITY_MIN)
      }
      manager.notify(NOTIFICATION_ID, notification.build())
    }
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

    findViewById<View>(R.id.generate).setOnClickListener(this)
    findViewById<View>(R.id.settings).setOnClickListener(this)
    findViewById<View>(R.id.close).setOnClickListener(this)

    lengthGroup = findViewById<RadioGroup>(R.id.length_group)
    lengthGroup?.setOnCheckedChangeListener(this)

    master = findViewById<TextView>(R.id.master_key)
    master?.setOnFocusChangeListener(this)
    master?.setOnEditorActionListener(this)

    site = findViewById<TextView>(R.id.site_key)
    site?.setOnFocusChangeListener(this)
    site?.setOnEditorActionListener(this)

    password = findViewById<TextView>(R.id.password)

    radioButtons = listOf(
        findViewById<RadioButton>(R.id.length1),
        findViewById<RadioButton>(R.id.length2),
        findViewById<RadioButton>(R.id.length3),
        findViewById<RadioButton>(R.id.length4))
    checkedIndex = radioButtons.size - 1
    checkedLength = radioButtons[checkedIndex]

    showNotification(this)
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

    val defaultIndex = radioButtons.size - 1
    var index =
      getSharedPreferences(PREF, 0).getInt(KEY_SELECTED_LENGTH, defaultIndex)
    if (index !in 1..radioButtons.size) {
      index = defaultIndex
    }
    lengthGroup?.check(radioButtons[index].getId())

    var siteKey: String = ""
    if (getIntent()?.getAction() == Intent.ACTION_SEND) {
      siteKey = getSiteKeyFromIntent(intent)
    }
    if (siteKey == "") {
      siteKey = getSiteKeyFromForegroundApp()
    }
    site?.setText(siteKey)
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
    checkedLength = findViewById<RadioButton>(checkedId)
    checkedIndex = radioButtons.indexOf(checkedLength!!)
  }

  private fun doGenerate() {
    val length = checkedLength?.getText().toString().toInt()
    val masterKey = master?.getText().toString()
    if (masterKey.isEmpty()) {
      showToast(this, R.string.empty_master_toast)
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
      showToast(this, R.string.clip_toast)
      val time = pref.getString(
          SettingsActivity.KEY_CLEAR_CLIPBOARD,
          SettingsActivity.DEFAULT_CLEAR_CLIPBOARD)!!.toLong()
      if (time > 0) {
        Handler().postDelayed(
            Runnable() {
              if (clip.hasPrimaryClip()) {
                val item = clip.getPrimaryClip()?.getItemAt(0)
                if (item?.getText().toString() == value) {
                  clip.setPrimaryClip(EMPTY_CLIP)
                  showToast(this@OnePwd, R.string.clear_clip_toast)
                }
              }
            },
            time * 1000)
      }
    }
  }

  private fun getSiteKeyFromForegroundApp(): String {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    if (!pref.getBoolean(
        SettingsActivity.KEY_PREFILL_USAGE,
        SettingsActivity.DEFAULT_PREFILL_USAGE)) {
      return ""
    }

    getForegroundApp().let { pkg ->
      if (CHROME_PACKAGES.contains(pkg)) {
        showToast(this, R.string.chrome_toast)
      }

      val segments = pkg.split(".")
      if (segments.size > 1) {
        return segments[1]
      }
      return pkg
    }
  }

  private fun getForegroundApp(): String {
    val manager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    val apps = manager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY, time - USAGE_TIMEFRAME, time)
    var max: Long = 0
    var result: String = ""
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

  private fun getSiteKeyFromIntent(intent: Intent): String {
    intent.getStringExtra(Intent.EXTRA_TEXT).let { url ->
      if (url.isEmpty()) {
        return ""
      }
      Uri.parse(url).getHost()?.let { host ->
        val segments = host.split(".")
        if (segments.size > 1) {
          return segments[segments.size - 2]
        }
        return host
      }
    }
    return ""
  }
}
