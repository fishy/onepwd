package com.yhsif.onepwd.settings

import android.os.Bundle
import com.yhsif.onepwd.R

class BioPreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?,
  ) {
    setPreferencesFromResource(R.xml.pref_bio, rootKey)
    setHasOptionsMenu(true)
  }
}
