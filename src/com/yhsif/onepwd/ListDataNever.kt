package com.yhsif.onepwd

import android.content.Context

import androidx.preference.PreferenceManager

data class ListDataNever(val full: String) : ListDataBase {
  override fun getText() = full

  override fun doRemove(ctx: Context) {
    val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
    val neverSet = pref.getStringSet(OnePwd.KEY_NEVER_PAIRINGS, setOf())!!
    val mutableSet = neverSet.toMutableSet()
    mutableSet.remove(full)
    pref.edit().let { editor ->
      editor.putStringSet(OnePwd.KEY_NEVER_PAIRINGS, mutableSet)
      editor.commit()
    }
  }
}
