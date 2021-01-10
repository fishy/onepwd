package com.yhsif.onepwd.settings

import android.os.Bundle
import com.yhsif.onepwd.R

class AboutPreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?,
  ) {
    setPreferencesFromResource(R.xml.pref_about, rootKey)
    setHasOptionsMenu(true)
  }
}
