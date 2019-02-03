package com.yhsif.onepwd

import android.app.Application

import androidx.sqlite.db.SupportSQLiteOpenHelper

import com.yhsif.onepwd.db.SiteKeyPairings

class MyApp : Application() {
  companion object {
    var pairingHelper: SupportSQLiteOpenHelper? = null
  }

  override fun onCreate() {
    pairingHelper = SiteKeyPairings.openHelper(this)
  }
}
