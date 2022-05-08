package com.yhsif.onepwd

import com.yhsif.onepwd.db.SiteKeyPairings

class PairingList : ListActivityBase(R.string.pairing_list_hint) {

  var prev: Set<ListDataPairing> = setOf()

  override fun refreshData() {
    SiteKeyPairings.listAll(MyApp.pairingHelper!!) { list ->
      val pairingSet = mutableSetOf<ListDataPairing>()
      for (pair in list) {
        pairingSet.add(ListDataPairing(pair.first, pair.second))
      }
      if (pairingSet != prev) {
        prev = pairingSet
        adapter?.let { a ->
          a.list = prev
            .map() { it }
            .sortedBy() { it.getText().lowercase() }
            .toMutableList()
          a.notifyDataSetChanged()
        }
      }
    }
  }

  override fun getDialogMessage(data: ListDataBase): String {
    val d = data as ListDataPairing
    return getString(R.string.dialog_text_pairing, d.full)
  }
}
