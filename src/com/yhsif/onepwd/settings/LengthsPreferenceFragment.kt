package com.yhsif.onepwd.settings

import android.os.Bundle
import androidx.preference.Preference
import com.yhsif.onepwd.R

class LengthsPreferenceFragment : BasePreferenceFragment() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.pref_lengths, rootKey)
        setHasOptionsMenu(true)

        val pref1: Preference? = findPreference(SettingsActivity.KEY_LENGTH1)
        if (pref1 != null) {
            SettingsActivity.bindPreferenceSummaryToInt(
                pref1,
                SettingsActivity.DEFAULT_LENGTH1
            )
        }
        val pref2: Preference? = findPreference(SettingsActivity.KEY_LENGTH2)
        if (pref2 != null) {
            SettingsActivity.bindPreferenceSummaryToInt(
                pref2,
                SettingsActivity.DEFAULT_LENGTH2
            )
        }
        val pref3: Preference? = findPreference(SettingsActivity.KEY_LENGTH3)
        if (pref3 != null) {
            SettingsActivity.bindPreferenceSummaryToInt(
                pref3,
                SettingsActivity.DEFAULT_LENGTH3
            )
        }
        val pref4: Preference? = findPreference(SettingsActivity.KEY_LENGTH4)
        if (pref4 != null) {
            SettingsActivity.bindPreferenceSummaryToInt(
                pref4,
                SettingsActivity.DEFAULT_LENGTH4
            )
        }
    }
}
