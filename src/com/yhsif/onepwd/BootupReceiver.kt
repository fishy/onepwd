package com.yhsif.onepwd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootupReceiver : BroadcastReceiver() {

  override fun onReceive(ctx: Context, intent: Intent) {
    OnePwd.showNotification(ctx)
  }
}
