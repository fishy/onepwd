package com.yhsif.onepwd

import android.content.Context
import com.yhsif.onepwd.db.SiteKeyPairings

data class ListDataPairing(val full: String, val key: String) : ListDataBase {
  override fun getText() = "\"$full\" -> \"$key\""

  override fun doRemove(ctx: Context) {
    SiteKeyPairings.delete(MyApp.pairingHelper!!, full) {}
  }
}
