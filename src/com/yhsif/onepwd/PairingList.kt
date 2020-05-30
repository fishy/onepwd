package com.yhsif.onepwd

import com.yhsif.onepwd.db.SiteKeyPairing

class PairingList : ListActivityBase(R.string.pairing_list_hint) {

    var prev: Set<ListDataPairing> = setOf()

    override fun refreshData() {
        SiteKeyPairing.listAll() { pairings ->
            val pairingSet = mutableSetOf<ListDataPairing>()
            for (entry in pairings) {
                pairingSet.add(ListDataPairing(entry.full, entry.siteKey))
            }
            if (pairingSet != prev) {
                prev = pairingSet
                adapter?.let { a ->
                    a.list = prev
                        .map() { it }
                        .sortedBy() { it.getText().toLowerCase() }
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
