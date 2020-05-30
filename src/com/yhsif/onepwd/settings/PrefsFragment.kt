package com.yhsif.onepwd.settings

import android.os.Bundle
import com.yhsif.onepwd.R

class PrefsFragment : BasePreferenceFragment() {
    companion object {
        const val FRAGMENT_TAG = "prefs_fragment"
    }

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.pref_headers, rootKey)
        setHasOptionsMenu(true)
    }
}
