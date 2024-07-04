package com.yhsif.onepwd

import android.content.Intent
import android.service.quicksettings.TileService
import androidx.core.service.quicksettings.PendingIntentActivityWrapper
import androidx.core.service.quicksettings.TileServiceCompat

class QSTileService : TileService() {
  override fun onClick() {
    unlockAndRun() {
      val intent = Intent(this, OnePwd::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      TileServiceCompat.startActivityAndCollapse(
        this,
        PendingIntentActivityWrapper(
          this,
          0, // requestCode
          intent,
          Intent.FLAG_ACTIVITY_NEW_TASK,
          false, // isMutable
        ),
      )
    }
  }
}
