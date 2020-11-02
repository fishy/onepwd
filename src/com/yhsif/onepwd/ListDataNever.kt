package com.yhsif.onepwd

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

data class ListDataNever(val full: String) : ListDataBase {
  override fun getText() = full

  override fun doRemove(ctx: Context) {
    val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
    val neverSet = pref.getStringSet(OnePwd.KEY_NEVER_PAIRINGS, setOf())!!
    val mutableSet = neverSet.toMutableSet()
    mutableSet.remove(full)
    pref.edit {
      putStringSet(OnePwd.KEY_NEVER_PAIRINGS, mutableSet)
    }
  }
}
