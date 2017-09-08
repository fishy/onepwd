package com.yhsif.onepwd

import android.content.Context
import android.content.res.TypedArray
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.NumberPicker

public class LengthPreference: DialogPreference {
  companion object {
    private const val WRAP_WHEEL = true

    // Converting 16-byte MD5 to Base64 will get us 8*16/6 = 21.3 characters,
    // so anything larger than 22 is meaningless.
    // We are not guaranteed to get 22 characters though. The padding "="s will
    // be trimmed, and also there might be "+" and "/" in the middle being
    // stripped out.
    public const val MAX_VALUE = 22
    public const val MIN_VALUE = 1
  }

  private var picker: NumberPicker? = null
  private var value: Int = 0

  public constructor(ctx: Context, attrs: AttributeSet)
  : super(ctx, attrs) {}

  public constructor(
      ctx: Context, attrs: AttributeSet, defStyleAttr: Int)
      : super(ctx, attrs, defStyleAttr) {}

  override fun onCreateDialogView(): View {
    val layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    layoutParams.gravity = CENTER

    picker = NumberPicker(getContext())
    picker?.setLayoutParams(layoutParams)

    val dialogView = FrameLayout(getContext())
    dialogView.addView(picker)

    return dialogView
  }

  override fun onBindDialogView(view: View) {
    super.onBindDialogView(view)
    picker?.setMinValue(MIN_VALUE)
    picker?.setMaxValue(MAX_VALUE)
    picker?.setWrapSelectorWheel(WRAP_WHEEL)
    picker?.setValue(getValue())
  }

  override fun onDialogClosed(positiveResult: Boolean) {
    if (positiveResult) {
      picker?.clearFocus()
      picker?.getValue()?.let { newValue ->
        if (callChangeListener(newValue)) {
          setValue(newValue)
        }
      }
    }
  }

  override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
    return a.getInt(index, MIN_VALUE)
  }

  override fun onSetInitialValue(
      restorePersistedValue: Boolean, defaultValue: Any?) {
    setValue(
        if (restorePersistedValue) getPersistedInt(MIN_VALUE)
        else if (defaultValue != null) defaultValue as Int else 0)
  }

  private fun setValue(value: Int) {
    this.value = value
    persistInt(value)
  }

  private fun getValue(): Int {
    return value
  }
}
