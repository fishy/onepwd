package com.yhsif.onepwd.settings

import android.os.Bundle

import com.yhsif.onepwd.R

class PrefillPreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?
  ) {
    setPreferencesFromResource(R.xml.pref_prefill, rootKey)
    setHasOptionsMenu(true)

    SettingsActivity.bindPreferenceSummaryToBoolean(
      findPreference(SettingsActivity.KEY_PREFILL_USAGE),
      SettingsActivity.DEFAULT_PREFILL_USAGE
    )
  }
}
