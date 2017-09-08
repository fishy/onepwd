package com.yhsif.onepwd

import android.app.Notification
import android.app.Notification.PRIORITY_MIN
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v7.preference.PreferenceManager

import com.yhsif.onepwd.settings.SettingsActivity

class NotificationService: Service() {
  companion object {
    const val NOTIFICATION_ID = 1

    fun run(ctx: Context) {
      if (PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(
            SettingsActivity.KEY_USE_SERVICE,
            SettingsActivity.DEFAULT_USE_SERVICE)) {
        ctx.startService(Intent(ctx, NotificationService::class.java))
      } else {
        ctx.stopService(Intent(ctx, NotificationService::class.java))
      }
    }
  }

  val binder: IBinder = LocalBinder()

  override fun onCreate() {
    showNotification()
  }

  override fun onBind(intent: Intent): IBinder = binder

  private fun showNotification() {
    val activity =
      PendingIntent.getActivity(this, 0, Intent(this, OnePwd::class.java), 0)
    val notification =
      Notification.Builder(this)
        .setSmallIcon(R.drawable.notify_icon)
        .setWhen(System.currentTimeMillis())
        .setTicker(getText(R.string.ticker))
        .setContentTitle(getText(R.string.ticker))
        .setContentIntent(activity)
        .setPriority(PRIORITY_MIN)
        .build()
    startForeground(NOTIFICATION_ID, notification)
  }

  /**
   * Class for clients to access.
   */
  inner class LocalBinder: Binder() {
    fun getService(): NotificationService = this@NotificationService
  }
}
