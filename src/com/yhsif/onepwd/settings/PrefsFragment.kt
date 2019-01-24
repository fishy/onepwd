package com.yhsif.onepwd.settings

import android.os.Build
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
    // TODO: Use androidx.biometrics.BiometricPrompt when it's stable enough.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
      setPreferencesFromResource(R.xml.pref_headers_no_bio, rootKey)
    } else {
      setPreferencesFromResource(R.xml.pref_headers, rootKey)
    }
    setHasOptionsMenu(true)
  }
}
