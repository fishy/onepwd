package com.yhsif.onepwd.settings

import android.view.MenuItem
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.yhsif.onepwd.length.LengthDialog
import com.yhsif.onepwd.length.LengthPreference

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            getParentFragmentManager().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDisplayPreferenceDialog(pref: Preference) {
        if (pref is LengthPreference) {
            val frag = LengthDialog.newInstance(pref)
            frag.setTargetFragment(this, 0)
            frag.show(
                getParentFragmentManager(),
                "androidx.preference.PreferenceFragment.DIALOG"
            )
        } else {
            super.onDisplayPreferenceDialog(pref)
        }
    }
}
