package com.yhsif.onepwd

import android.content.Intent
import android.service.quicksettings.TileService

class QSTileService : TileService() {
  override fun onClick() {
    val intent = Intent(this, OnePwd::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivityAndCollapse(intent)
  }
}
