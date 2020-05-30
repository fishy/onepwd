package com.yhsif.onepwd.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(SiteKeyPairing::class), version = 1, exportSchema = false)
abstract class SiteKeyPairingDatabase : RoomDatabase() {
    abstract fun dao(): SiteKeyPairingDao
}
