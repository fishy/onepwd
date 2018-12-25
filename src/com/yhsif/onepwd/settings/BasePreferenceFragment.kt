package com.yhsif.onepwd.settings

import android.support.v4.app.DialogFragment
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem

import com.yhsif.onepwd.length.LengthDialog
import com.yhsif.onepwd.length.LengthPreference

abstract class BasePreferenceFragment: PreferenceFragmentCompat() {
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.getItemId() == android.R.id.home) {
      getFragmentManager()?.popBackStack()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onDisplayPreferenceDialog(pref: Preference) {
    if (pref is LengthPreference) {
      val frag: DialogFragment = LengthDialog.newInstance(pref)
      frag.setTargetFragment(this, 0)
      frag.show(
          getFragmentManager(),
          "android.support.v7.preference.PreferenceFragment.DIALOG")
    } else {
      super.onDisplayPreferenceDialog(pref);
    }
  }
}
