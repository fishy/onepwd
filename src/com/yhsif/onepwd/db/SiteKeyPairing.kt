package com.yhsif.onepwd.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Room

@Entity(
  tableName = "site_key_pairings",
  indices = arrayOf(
    Index(
      name = "idx_full",
      unique = true,
      value = ["full_site_key"]
    )
  )
)
data class SiteKeyPairing(
  @ColumnInfo(name = "full_site_key") val full: String = "",
  @ColumnInfo(name = "site_key") val siteKey: String = "",
  @PrimaryKey @ColumnInfo(name = "_id", autoGenerate = true) val id: Long? = null
) {
  companion object {
    private lateinit var db: SiteKeyPairingDatabase

    fun initDb(context: Context) {
      db = Room.databaseBuilder(
        context,
        SiteKeyPairingDatabase::class.java,
        "SiteKeyPairings.db"
      ).build()
    }

    fun dao(): SiteKeyPairingDao {
      return db.dao()
    }

    // callback arg is the new unique id
    fun insert(full: String, siteKey: String, callback: (Long) -> Unit) {
      val task = object : AsyncTask<Unit, Unit, Long>() {
        override fun doInBackground(vararg unused: Unit): Long {
          return db.dao().insert(SiteKeyPairing(full, siteKey))
        }

        override fun onPostExecute(id: Long) {
          callback(id)
        }
      }
      task.execute()
    }

    // callback arg is the number of rows deleted
    fun delete(full: String, callback: (Int) -> Unit) {
      val task = object : AsyncTask<Unit, Unit, Int>() {
        override fun doInBackground(vararg unused: Unit): Int {
          return db.dao().delete(full)
        }

        override fun onPostExecute(rows: Int) {
          callback(rows)
        }
      }
      task.execute()
    }

    // callback arg is the number of rows updated
    fun update(full: String, siteKey: String, callback: (Int) -> Unit) {
      val task = object : AsyncTask<Unit, Unit, Int>() {
        override fun doInBackground(vararg unused: Unit): Int {
          return db.dao().update(full, siteKey)
        }

        override fun onPostExecute(rows: Int) {
          callback(rows)
        }
      }
      task.execute()
    }

    // callback arg is the site key, or null if pairing not found
    fun getSiteKey(full: String, callback: (String?) -> Unit) {
      val task = object : AsyncTask<Unit, Unit, String?>() {
        override fun doInBackground(vararg unused: Unit): String? {
          return db.dao().getSiteKey(full)?.siteKey
        }

        override fun onPostExecute(siteKey: String?) {
          callback(siteKey)
        }
      }
      task.execute()
    }

    // callback arg is the list of pairings
    fun listAll(callback: (List<SiteKeyPairing>) -> Unit) {
      val task = object : AsyncTask<Unit, Unit, List<SiteKeyPairing>>() {
        override fun doInBackground(vararg unused: Unit): List<SiteKeyPairing> {
          return db.dao().listAll()
        }

        override fun onPostExecute(pairings: List<SiteKeyPairing>) {
          callback(pairings)
        }
      }
      task.execute()
    }
  }
}
