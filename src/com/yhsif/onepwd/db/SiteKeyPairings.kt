package com.yhsif.onepwd.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.provider.BaseColumns

import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.SupportSQLiteQueryBuilder

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
            newVersion: Int
          ) {
            // do nothing
          }
        })
        .build()
      return FrameworkSQLiteOpenHelperFactory().create(config)
    }

    public fun insertOrUpdate(
      helper: SupportSQLiteOpenHelper,
      full: String,
      siteKey: String,
      // arg is the new unique id
      callback: (Long) -> Unit
    ) {
      val task = object : AsyncTask<Unit, Unit, Long>() {
        override fun doInBackground(vararg unused: Unit): Long {
          val db = helper.getWritableDatabase()
          val values = ContentValues().apply {
            put(PairingEntry.COLUMN_NAME_FULL, full)
            put(PairingEntry.COLUMN_NAME_SITE_KEY, siteKey)
          }
          return db.insert(
            PairingEntry.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE,
            values
          )
        }

        override fun onPostExecute(id: Long) {
          callback(id)
        }
      }
      task.execute()
    }

    public fun delete(
      helper: SupportSQLiteOpenHelper,
      vararg full: String,
      // arg is the number of rows deleted
      callback: (Int) -> Unit
    ) {
      val task = object : AsyncTask<Unit, Unit, Int>() {
        override fun doInBackground(vararg unused: Unit): Int {
          val db = helper.getWritableDatabase()
          val selection = "${PairingEntry.COLUMN_NAME_FULL} LIKE ?"
          return db.delete(PairingEntry.TABLE_NAME, selection, full)
        }

        override fun onPostExecute(rows: Int) {
          callback(rows)
        }
      }
      task.execute()
    }

    public fun getSiteKey(
      helper: SupportSQLiteOpenHelper,
      full: String,
      // arg is the site key, or null if pairing not found
      callback: (String?) -> Unit
    ) {
      val task = object : AsyncTask<Unit, Unit, String?>() {
        override fun doInBackground(vararg unused: Unit): String? {
          val db = helper.getReadableDatabase()
          val query = SupportSQLiteQueryBuilder
            .builder(PairingEntry.TABLE_NAME)
            .selection("${PairingEntry.COLUMN_NAME_FULL} LIKE ?", arrayOf(full))
            .columns(arrayOf(
              PairingEntry.COLUMN_NAME_FULL,
              PairingEntry.COLUMN_NAME_SITE_KEY
            ))
            .limit("1")
            .create()
          db.query(query).use { cursor ->
            if (!cursor.moveToFirst()) {
              return null
            }
            if (cursor.getString(0) != full) {
              return null
            }
            return cursor.getString(1)
          }
        }

        override fun onPostExecute(siteKey: String?) {
          callback(siteKey)
        }
      }
      task.execute()
    }

    public fun listAll(
      helper: SupportSQLiteOpenHelper,
      // arg is list of pairs of (full, site_key)
      callback: (List<Pair<String, String>>) -> Unit
    ) {
      val task = object : AsyncTask<Unit, Unit, List<Pair<String, String>>>() {
        override fun doInBackground(
          vararg unused: Unit
        ): List<Pair<String, String>> {
          val db = helper.getReadableDatabase()
          val query = SupportSQLiteQueryBuilder
            .builder(PairingEntry.TABLE_NAME)
            .columns(arrayOf(
              PairingEntry.COLUMN_NAME_FULL,
              PairingEntry.COLUMN_NAME_SITE_KEY
            ))
            .create()
          val res = mutableListOf<Pair<String, String>>()
          db.query(query).use { cursor ->
            while (cursor.moveToNext()) {
              res.add(Pair(cursor.getString(0), cursor.getString(1)))
            }
            return res
          }
        }

        override fun onPostExecute(list: List<Pair<String, String>>) {
          callback(list)
        }
      }
      task.execute()
    }
  }
}
