package com.yhsif.onepwd.settings

import android.os.Bundle

import com.yhsif.onepwd.R

class LengthsPreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?
  ) {
    setPreferencesFromResource(R.xml.pref_lengths, rootKey)
    setHasOptionsMenu(true)

    SettingsActivity.bindPreferenceSummaryToInt(
        findPreference(SettingsActivity.KEY_LENGTH1),
        SettingsActivity.DEFAULT_LENGTH1)
    SettingsActivity.bindPreferenceSummaryToInt(
        findPreference(SettingsActivity.KEY_LENGTH2),
        SettingsActivity.DEFAULT_LENGTH2)
    SettingsActivity.bindPreferenceSummaryToInt(
        findPreference(SettingsActivity.KEY_LENGTH3),
        SettingsActivity.DEFAULT_LENGTH3)
    SettingsActivity.bindPreferenceSummaryToInt(
        findPreference(SettingsActivity.KEY_LENGTH4),
        SettingsActivity.DEFAULT_LENGTH4)
  }
}
