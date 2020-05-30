package com.yhsif.onepwd

import android.app.Application
import com.yhsif.onepwd.db.SiteKeyPairing

class MyApp : Application() {
    override fun onCreate() {
        SiteKeyPairing.initDb(this)
    }
}
