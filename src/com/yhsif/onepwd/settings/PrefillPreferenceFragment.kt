package com.yhsif.onepwd.settings

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle

import com.yhsif.onepwd.R

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class PrefillPreferenceFragment: BasePreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.pref_prefill)
    setHasOptionsMenu(true)

    SettingsActivity.bindPreferenceSummaryToBoolean(
        findPreference(SettingsActivity.KEY_PREFILL_USAGE),
        SettingsActivity.DEFAULT_PREFILL_USAGE)
  }
}
