package com.yhsif.onepwd.settings

import android.os.Bundle

import com.yhsif.onepwd.R

class ServicePreferenceFragment: BasePreferenceFragment() {
  override fun onCreatePreferences(
      savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.pref_service, rootKey)
    setHasOptionsMenu(true)

    SettingsActivity.bindPreferenceSummaryToBoolean(
        findPreference(SettingsActivity.KEY_USE_SERVICE),
        SettingsActivity.DEFAULT_USE_SERVICE)
  }
}
