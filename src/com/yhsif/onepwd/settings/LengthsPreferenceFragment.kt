package com.yhsif.onepwd.settings

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle

import com.yhsif.onepwd.R

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class LengthsPreferenceFragment: BasePreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.pref_lengths)
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
