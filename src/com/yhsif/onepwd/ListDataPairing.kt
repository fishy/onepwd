package com.yhsif.onepwd

import android.content.Context
import com.yhsif.onepwd.db.SiteKeyPairing

data class ListDataPairing(val full: String, val key: String) : ListDataBase {
  override fun getText() = "\"$full\" -> \"$key\""

  override fun doRemove(ctx: Context) {
    SiteKeyPairing.delete(full) {}
  }
}
