package com.yhsif.onepwd.settings

import android.os.Bundle
import androidx.preference.Preference
import com.yhsif.onepwd.R

class ClipboardPreferenceFragment : BasePreferenceFragment() {
  override fun onCreatePreferences(
    savedInstanceState: Bundle?,
    rootKey: String?,
  ) {
    setPreferencesFromResource(R.xml.pref_clipboard, rootKey)
    setHasOptionsMenu(true)

    val prefCopy: Preference? =
      findPreference(SettingsActivity.KEY_COPY_CLIPBOARD)
    if (prefCopy != null) {
      SettingsActivity.bindPreferenceSummaryToBoolean(
        prefCopy,
        SettingsActivity.DEFAULT_COPY_CLIPBOARD,
      )
    }
    val prefClear: Preference? =
      findPreference(SettingsActivity.KEY_CLEAR_CLIPBOARD)
    if (prefClear != null) {
      SettingsActivity.bindPreferenceSummaryToString(prefClear)
    }
  }
}
