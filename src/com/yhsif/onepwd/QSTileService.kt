package com.yhsif.onepwd

import android.content.Intent
import android.service.quicksettings.TileService

class QSTileService: TileService() {
  override fun onClick() {
    startActivityAndCollapse(Intent(this, OnePwd::class.java))
  }
}
