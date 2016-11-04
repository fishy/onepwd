package com.yhsif.onepwd;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

public class LengthPreference extends DialogPreference {
  // The max guaranteed length of Base64 encoding of HMAC-MD5
  public static final int MAX_VALUE = 20;
  public static final int MIN_VALUE = 1;
  public static final boolean WRAP_WHEEL = true;

  private NumberPicker picker;
  private int value;

  public LengthPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public LengthPreference(
      Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected View onCreateDialogView() {
    FrameLayout.LayoutParams layoutParams =
      new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    layoutParams.gravity = CENTER;

    picker = new NumberPicker(getContext());
    picker.setLayoutParams(layoutParams);

    FrameLayout dialogView = new FrameLayout(getContext());
    dialogView.addView(picker);

    return dialogView;
  }

  @Override
  protected void onBindDialogView(View view) {
    super.onBindDialogView(view);
    picker.setMinValue(MIN_VALUE);
    picker.setMaxValue(MAX_VALUE);
    picker.setWrapSelectorWheel(WRAP_WHEEL);
    picker.setValue(getValue());
  }

  @Override
  protected void onDialogClosed(boolean positiveResult) {
    if (positiveResult) {
      picker.clearFocus();
      int newValue = picker.getValue();
      if (callChangeListener(newValue)) {
        setValue(newValue);
      }
    }
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getInt(index, MIN_VALUE);
  }

  @Override
  protected void onSetInitialValue(
      boolean restorePersistedValue, Object defaultValue) {
    setValue(
        restorePersistedValue
        ? getPersistedInt(MIN_VALUE)
        : (Integer) defaultValue);
  }

  private void setValue(int value) {
    this.value = value;
    persistInt(value);
  }

  private int getValue() {
    return value;
  }
}
