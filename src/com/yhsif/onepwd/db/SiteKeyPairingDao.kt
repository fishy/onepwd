package com.yhsif.onepwd.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SiteKeyPairingDao {
  @Insert(onConflict = OnConflictStrategy.ABORT)
  fun insert(pairing: SiteKeyPairing): Long

  @Query("UPDATE site_key_pairings SET site_key = :siteKey WHERE full_site_key == :full")
  fun update(full: String, siteKey: String): Int

  @Query("DELETE FROM site_key_pairings WHERE full_site_key == :full")
  fun delete(full: String): Int

  @Query("SELECT * FROM site_key_pairings WHERE full_site_key == :full LIMIT 1")
  fun getSiteKey(full: String): SiteKeyPairing?

  @Query("SELECT * FROM site_key_pairings")
  fun listAll(): List<SiteKeyPairing>
}
