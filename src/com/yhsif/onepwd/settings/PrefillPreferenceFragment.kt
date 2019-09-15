package com.yhsif.onepwd.settings

import android.os.Bundle

import androidx.preference.Preference

import com.yhsif.onepwd.R

class PrefillPreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?
  ) {
    setPreferencesFromResource(R.xml.pref_prefill, rootKey)
    setHasOptionsMenu(true)

    val pref: Preference? = findPreference(SettingsActivity.KEY_PREFILL_USAGE)
    if (pref != null) {
      SettingsActivity.bindPreferenceSummaryToBoolean(
        pref,
        SettingsActivity.DEFAULT_PREFILL_USAGE
      )
    }
  }
}
