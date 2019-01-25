package com.yhsif.onepwd.settings

import android.os.Bundle

import com.yhsif.onepwd.R

class ClipboardPreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?
  ) {
    setPreferencesFromResource(R.xml.pref_clipboard, rootKey)
    setHasOptionsMenu(true)

    SettingsActivity.bindPreferenceSummaryToBoolean(
      findPreference(SettingsActivity.KEY_COPY_CLIPBOARD),
      SettingsActivity.DEFAULT_COPY_CLIPBOARD
    )
    SettingsActivity.bindPreferenceSummaryToString(
      findPreference(SettingsActivity.KEY_CLEAR_CLIPBOARD)
    )
  }
}
