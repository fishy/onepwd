package com.yhsif.onepwd.settings

import android.annotation.TargetApi
import android.os.Build
import android.preference.PreferenceFragment
import android.view.MenuItem

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
abstract class BasePreferenceFragment: PreferenceFragment() {
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.getItemId() == android.R.id.home) {
      getFragmentManager().popBackStack()
    }
    return super.onOptionsItemSelected(item)
  }
}
