package com.yhsif.onepwd;

import static android.app.Notification.PRIORITY_MIN;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.preference.PreferenceManager;

public class NotificationService extends Service {
  private static final int NOTIFICATION_ID = 1;

  private final IBinder binder = new LocalBinder();

  @Override
  public void onCreate() {
    showNotification();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  private void showNotification() {
    PendingIntent activity =
      PendingIntent.getActivity(this, 0, new Intent(this, OnePwd.class), 0);
    Notification notification =
      new Notification.Builder(this)
        .setSmallIcon(R.drawable.notify_icon)
        .setWhen(System.currentTimeMillis())
        .setTicker(getText(R.string.ticker))
        .setContentTitle(getText(R.string.ticker))
        .setContentIntent(activity)
        .setPriority(PRIORITY_MIN)
        .build();
    startForeground(NOTIFICATION_ID, notification);
  }

  public static void run(Context context) {
    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
          SettingsActivity.KEY_USE_SERVICE,
          SettingsActivity.DEFAULT_USE_SERVICE)) {
      context.startService(new Intent(context, NotificationService.class));
    } else {
      context.stopService(new Intent(context, NotificationService.class));
    }
  }

  /**
   * Class for clients to access.
   */
  public class LocalBinder extends Binder {
    NotificationService getService() {
      return NotificationService.this;
    }
  }
}
