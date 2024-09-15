package com.yhsif.onepwd.length

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference

public class LengthPreference : DialogPreference {
  companion object {
    // Converting 16-byte MD5 to Base64 will get us 8*16/6 = 21.3 characters,
    // so anything larger than 22 is meaningless.
    // We are not guaranteed to get 22 characters though.
    // The padding "="s will be trimmed,
    // and also there might be "+" and "/" in the middle being stripped out.
    public const val MAX_VALUE = 22
    public const val MIN_VALUE = 1
  }

  private var value: Int = 0

  public constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {}

  public constructor(
    ctx: Context,
    attrs: AttributeSet,
    defStyleAttr: Int,
  ) : super(ctx, attrs, defStyleAttr) {}

  override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
    return a.getInt(index, MIN_VALUE)
  }

  override fun onSetInitialValue(
    defaultValue: Any?,
  ) {
    val def = if (defaultValue != null) {
      defaultValue as Int
    } else {
      MIN_VALUE
    }
    setValue(getPersistedInt(def))
  }

  fun setValue(value: Int) {
    this.value = value
    persistInt(value)
  }

  fun getValue(): Int {
    return value
  }
}
