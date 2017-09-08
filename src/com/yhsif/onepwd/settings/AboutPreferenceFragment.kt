package com.yhsif.onepwd.settings

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle

import com.yhsif.onepwd.R

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class AboutPreferenceFragment: BasePreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.pref_about)
    setHasOptionsMenu(true)
  }
}
