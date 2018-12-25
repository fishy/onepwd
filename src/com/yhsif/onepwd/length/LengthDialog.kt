package com.yhsif.onepwd.length

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.NumberPicker

import com.yhsif.onepwd.R

public class LengthDialog: PreferenceDialogFragmentCompat() {
  companion object {
    private const val WRAP_WHEEL = true

    public fun newInstance(pref: Preference): LengthDialog {
      val frag: LengthDialog = LengthDialog()
      val bundle: Bundle = Bundle(1)
      bundle.putString(ARG_KEY, pref.getKey())
      frag.setArguments(bundle)
      return frag
    }
  }

  private var picker: NumberPicker? = null

  override fun onBindDialogView(view: View) {
    super.onBindDialogView(view)
    picker = view.findViewById(R.id.picker)

    picker?.setMinValue(LengthPreference.MIN_VALUE)
    picker?.setMaxValue(LengthPreference.MAX_VALUE)
    picker?.setWrapSelectorWheel(WRAP_WHEEL)
    getLengthPreference()?.getValue()?.let { value ->
      picker?.setValue(value)
    }
  }

  override fun onDialogClosed(positiveResult: Boolean) {
    if (positiveResult) {
      picker?.clearFocus()
      picker?.getValue()?.let { newValue ->
        val pref = getLengthPreference()
        if (pref?.callChangeListener(newValue) == true) {
          pref.setValue(newValue)
        }
      }
    }
  }

  fun getLengthPreference(): LengthPreference? {
    return getPreference() as? LengthPreference
  }
}
