package com.yhsif.onepwd.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Entity(
  tableName = "site_key_pairings",
  indices = arrayOf(
    Index(
      name = "idx_full",
      unique = true,
      value = ["full_site_key"],
    ),
  ),
)
data class SiteKeyPairing(
  @ColumnInfo(name = "full_site_key") val full: String = "",
  @ColumnInfo(name = "site_key") val siteKey: String = "",
  @PrimaryKey @ColumnInfo(name = "_id", autoGenerate = true) val id: Long? = null,
) {
  companion object {
    private lateinit var db: SiteKeyPairingDatabase

    fun initDb(context: Context) {
      db = Room.databaseBuilder(
        context,
        SiteKeyPairingDatabase::class.java,
        "SiteKeyPairings.db",
      ).build()
    }

    fun dao(): SiteKeyPairingDao {
      return db.dao()
    }

    // callback arg is the new unique id
    fun insert(full: String, siteKey: String, callback: (Long) -> Unit) {
      GlobalScope.launch(Dispatchers.Default) {
        withContext(Dispatchers.Default) {
          val id = db.dao().insert(SiteKeyPairing(full, siteKey))
          withContext(Dispatchers.Main) {
            callback(id)
          }
        }
      }
    }

    // callback arg is the number of rows deleted
    fun delete(full: String, callback: (Int) -> Unit) {
      GlobalScope.launch(Dispatchers.Default) {
        withContext(Dispatchers.Default) {
          val rows = db.dao().delete(full)
          withContext(Dispatchers.Main) {
            callback(rows)
          }
        }
      }
    }

    // callback arg is the number of rows updated
    fun update(full: String, siteKey: String, callback: (Int) -> Unit) {
      GlobalScope.launch(Dispatchers.Default) {
        withContext(Dispatchers.Default) {
          val rows = db.dao().update(full, siteKey)
          withContext(Dispatchers.Main) {
            callback(rows)
          }
        }
      }
    }

    // callback arg is the site key, or null if pairing not found
    fun getSiteKey(full: String, callback: (String?) -> Unit) {
      GlobalScope.launch(Dispatchers.Default) {
        withContext(Dispatchers.Default) {
          val siteKey = db.dao().getSiteKey(full)?.siteKey
          withContext(Dispatchers.Main) {
            callback(siteKey)
          }
        }
      }
    }

    // callback arg is the list of pairings
    fun listAll(callback: (List<SiteKeyPairing>) -> Unit) {
      GlobalScope.launch(Dispatchers.Default) {
        withContext(Dispatchers.Default) {
          val pairings = db.dao().listAll()
          withContext(Dispatchers.Main) {
            callback(pairings)
          }
        }
      }
    }
  }
}
