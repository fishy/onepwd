package com.yhsif.onepwd

import android.app.Application

import androidx.room.Room

import com.yhsif.onepwd.db.SiteKeyPairing

class MyApp : Application() {
  override fun onCreate() {
    SiteKeyPairing.initDb(this)
  }
}
