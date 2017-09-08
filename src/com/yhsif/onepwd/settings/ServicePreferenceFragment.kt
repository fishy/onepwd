package com.yhsif.onepwd.settings

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle

import com.yhsif.onepwd.R

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class ServicePreferenceFragment: BasePreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.pref_service)
    setHasOptionsMenu(true)

    SettingsActivity.bindPreferenceSummaryToBoolean(
        findPreference(SettingsActivity.KEY_USE_SERVICE),
        SettingsActivity.DEFAULT_USE_SERVICE)
  }
}
