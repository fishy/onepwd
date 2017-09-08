package com.yhsif.onepwd.settings

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle

import com.yhsif.onepwd.R

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class ClipboardPreferenceFragment: BasePreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.pref_clipboard)
    setHasOptionsMenu(true)

    SettingsActivity.bindPreferenceSummaryToBoolean(
        findPreference(SettingsActivity.KEY_COPY_CLIPBOARD),
        SettingsActivity.DEFAULT_COPY_CLIPBOARD)
    SettingsActivity.bindPreferenceSummaryToString(
        findPreference(SettingsActivity.KEY_CLEAR_CLIPBOARD))
  }
}
