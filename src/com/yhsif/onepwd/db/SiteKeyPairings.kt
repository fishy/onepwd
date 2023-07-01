package com.yhsif.onepwd.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public class SiteKeyPairings {
  companion object {
    private const val DATABASE_NAME = "SiteKeyPairings.db"
    private const val DATABASE_VERSION = 1

    private object PairingEntry : BaseColumns {
      const val TABLE_NAME = "site_key_pairings"

      const val COLUMN_NAME_FULL = "full_site_key"
      const val COLUMN_NAME_SITE_KEY = "site_key"
    }

    private const val INDEX_NAME_FULL = "idx_full"

    private const val SQL_CREATE_ENTRIES =
      "CREATE TABLE ${PairingEntry.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "${PairingEntry.COLUMN_NAME_FULL} TEXT NOT NULL," +
        "${PairingEntry.COLUMN_NAME_SITE_KEY} TEXT NOT NULL)"

    private const val SQL_CREATE_INDEX =
      "CREATE UNIQUE INDEX $INDEX_NAME_FULL ON " +
        "${PairingEntry.TABLE_NAME}(${PairingEntry.COLUMN_NAME_FULL})"

    private const val SQL_DELETE_ENTRIES =
      "DROP TABLE IF EXISTS ${PairingEntry.TABLE_NAME}"

    public fun openHelper(ctx: Context): SupportSQLiteOpenHelper {
      val config = SupportSQLiteOpenHelper.Configuration.builder(ctx)
        .name(DATABASE_NAME)
        .callback(object : SupportSQLiteOpenHelper.Callback(DATABASE_VERSION) {
          override fun onCreate(db: SupportSQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
            db.execSQL(SQL_CREATE_INDEX)
          }

          override fun onUpgrade(
            db: SupportSQLiteDatabase,
            oldVersion: Int,
            newVersion: Int,
          ) {
            // do nothing
          }
        })
        .build()
      return FrameworkSQLiteOpenHelperFactory().create(config)
    }

    public fun insert(
      helper: SupportSQLiteOpenHelper,
      full: String,
      siteKey: String,
      // arg is the new unique id
      callback: (Long) -> Unit,
    ) {
      GlobalScope.launch(Dispatchers.Default) {
        val db = helper.getWritableDatabase()
        val values = ContentValues().apply {
          put(PairingEntry.COLUMN_NAME_FULL, full)
          put(PairingEntry.COLUMN_NAME_SITE_KEY, siteKey)
        }
        val id = db.insert(
          PairingEntry.TABLE_NAME,
          SQLiteDatabase.CONFLICT_ROLLBACK,
          values,
        )
        withContext(Dispatchers.Main) {
          callback(id)
        }
      }
    }

    public fun update(
      helper: SupportSQLiteOpenHelper,
      full: String,
      siteKey: String,
      // arg is the number of rows deleted
      callback: (Int) -> Unit,
    ) {
      GlobalScope.launch(Dispatchers.Default) {
        val db = helper.getWritableDatabase()
        val values = ContentValues().apply {
          put(PairingEntry.COLUMN_NAME_SITE_KEY, siteKey)
        }
        val selection = "${PairingEntry.COLUMN_NAME_FULL} == ?"
        val rows = db.update(
          PairingEntry.TABLE_NAME,
          SQLiteDatabase.CONFLICT_ROLLBACK,
          values,
          selection,
          arrayOf(full),
        )
        withContext(Dispatchers.Main) {
          callback(rows)
        }
      }
    }

    public fun delete(
      helper: SupportSQLiteOpenHelper,
      vararg full: String,
      // arg is the number of rows deleted
      callback: (Int) -> Unit,
    ) {
      GlobalScope.launch(Dispatchers.Default) {
        val db = helper.getWritableDatabase()
        val selection = "${PairingEntry.COLUMN_NAME_FULL} == ?"
        val rows = db.delete(PairingEntry.TABLE_NAME, selection, full)
        withContext(Dispatchers.Main) {
          callback(rows)
        }
      }
    }

    public fun getSiteKey(
      helper: SupportSQLiteOpenHelper,
      full: String,
      // arg is the site key, or null if pairing not found
      callback: (String?) -> Unit,
    ) {
      GlobalScope.launch(Dispatchers.Default) {
        val db = helper.getReadableDatabase()
        val query = SupportSQLiteQueryBuilder
          .builder(PairingEntry.TABLE_NAME)
          .selection("${PairingEntry.COLUMN_NAME_FULL} == ?", arrayOf(full))
          .columns(
            arrayOf(
              PairingEntry.COLUMN_NAME_FULL,
              PairingEntry.COLUMN_NAME_SITE_KEY,
            ),
          )
          .limit("1")
          .create()

        var siteKey: String?
        db.query(query).use { cursor ->
          if (!cursor.moveToFirst()) {
            siteKey = null
            return@use
          }
          if (cursor.getString(0) != full) {
            siteKey = null
            return@use
          }
          siteKey = cursor.getString(1)
        }

        withContext(Dispatchers.Main) {
          callback(siteKey)
        }
      }
    }

    public fun listAll(
      helper: SupportSQLiteOpenHelper,
      // arg is list of pairs of (full, site_key)
      callback: (List<Pair<String, String>>) -> Unit,
    ) {
      GlobalScope.launch(Dispatchers.Default) {
        val db = helper.getReadableDatabase()
        val query = SupportSQLiteQueryBuilder
          .builder(PairingEntry.TABLE_NAME)
          .columns(
            arrayOf(
              PairingEntry.COLUMN_NAME_FULL,
              PairingEntry.COLUMN_NAME_SITE_KEY,
            ),
          )
          .create()

        val res = mutableListOf<Pair<String, String>>()
        db.query(query).use { cursor ->
          while (cursor.moveToNext()) {
            res.add(Pair(cursor.getString(0), cursor.getString(1)))
          }
        }

        withContext(Dispatchers.Main) {
          callback(res)
        }
      }
    }
  }
}
