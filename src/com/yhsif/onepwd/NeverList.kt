package com.yhsif.onepwd

import androidx.preference.PreferenceManager

class NeverList : ListActivityBase(R.string.never_list_hint) {

  var prev: Set<String> = setOf()

  override fun refreshData() {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    val neverSet = pref.getStringSet(OnePwd.KEY_NEVER_PAIRINGS, setOf())!!
    if (neverSet != prev) {
      prev = neverSet
      adapter?.let { a ->
        a.list = prev
          .map() { ListDataNever(it) }
          .sortedBy() { it.getText().toLowerCase() }
          .toMutableList()
        a.notifyDataSetChanged()
      }
    }
  }

  override fun getDialogMessage(data: ListDataBase): String {
    return getString(R.string.dialog_text_never, data.getText())
  }
}
