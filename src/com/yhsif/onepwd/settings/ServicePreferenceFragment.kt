package com.yhsif.onepwd.settings

import android.os.Bundle
import androidx.preference.Preference
import com.yhsif.onepwd.R

class ServicePreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?
  ) {
    setPreferencesFromResource(R.xml.pref_service, rootKey)
    setHasOptionsMenu(true)

    val pref: Preference? = findPreference(SettingsActivity.KEY_USE_SERVICE)
    if (pref != null) {
      SettingsActivity.bindPreferenceSummaryToBoolean(
        pref,
        SettingsActivity.DEFAULT_USE_SERVICE
      )
    }
  }
}
