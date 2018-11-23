package com.yhsif.onepwd.settings

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.view.MenuItem

import com.yhsif.onepwd.OnePwd
import com.yhsif.onepwd.R

public class SettingsActivity: AppCompatPreferenceActivity() {
  companion object {
    private const val KEY_SETTINGS_INTENT = "dummy_settings_intent";

    public const val KEY_PREFILL_USAGE = "prefill_usage"
    public const val DEFAULT_PREFILL_USAGE = false
    public const val KEY_COPY_CLIPBOARD = "copy_clipboard"
    public const val DEFAULT_COPY_CLIPBOARD = false
    public const val KEY_CLEAR_CLIPBOARD = "clear_clipboard"
    public const val DEFAULT_CLEAR_CLIPBOARD = "60"
    public const val KEY_LENGTH1 = "length1"
    public const val DEFAULT_LENGTH1 = 8
    public const val KEY_LENGTH2 = "length2"
    public const val DEFAULT_LENGTH2 = 10
    public const val KEY_LENGTH3 = "length3"
    public const val DEFAULT_LENGTH3 = 15
    public const val KEY_LENGTH4 = "length4"
    public const val DEFAULT_LENGTH4 = 20
    public const val KEY_USE_SERVICE = "use_service"
    public const val DEFAULT_USE_SERVICE = true

    val allowedClassNames = setOf<String?>(
      PreferenceFragment::class.java.canonicalName,
      PrefillPreferenceFragment::class.java.canonicalName,
      ClipboardPreferenceFragment::class.java.canonicalName,
      LengthsPreferenceFragment::class.java.canonicalName,
      ServicePreferenceFragment::class.java.canonicalName,
      AboutPreferenceFragment::class.java.canonicalName)

    val prefBinder = object: Preference.OnPreferenceChangeListener {
      override fun onPreferenceChange(
          pref: Preference, value: Any): Boolean {
        if (pref is ListPreference) {
          val index = pref.findIndexOfValue(value.toString())
          pref.setSummary(if (index >= 0) pref.getEntries()[index] else null)
        } else {
          when (pref.getKey()) {
            KEY_COPY_CLIPBOARD -> {
              val clearClip =
                pref.getPreferenceManager().findPreference(KEY_CLEAR_CLIPBOARD)
              if (value == true) {
                pref.setSummary(R.string.pref_desc_copy_clipboard_yes)
                clearClip?.setEnabled(true)
              } else {
                pref.setSummary(R.string.pref_desc_copy_clipboard_no)
                clearClip?.setEnabled(false)
              }
            }
            KEY_PREFILL_USAGE -> {
              val settings =
                pref.getPreferenceManager().findPreference(KEY_SETTINGS_INTENT)
              if (value == true) {
                pref.setSummary(R.string.pref_desc_usage_yes)
                settings?.setEnabled(true)
              } else {
                pref.setSummary(R.string.pref_desc_usage_no)
                settings?.setEnabled(false)
              }
            }
            KEY_USE_SERVICE -> {
              pref.setSummary(R.string.pref_desc_service)
              OnePwd.showNotification(pref.getContext(), value as Boolean)
            }
            // For all other preferences, set the summary to the value's
            // simple string representation.
            else -> pref.setSummary(value.toString())
          }
        }
        return true
      }
    }

    fun bindPreferenceSummaryToString(preference: Preference) {
      preference.setOnPreferenceChangeListener(prefBinder)
      prefBinder.onPreferenceChange(
          preference,
          PreferenceManager
              .getDefaultSharedPreferences(preference.getContext())
              .getString(preference.getKey(), ""))
    }

    fun bindPreferenceSummaryToInt(
        preference: Preference, defaultValue: Int) {
      preference.setOnPreferenceChangeListener(prefBinder)
      prefBinder.onPreferenceChange(
          preference,
          PreferenceManager
              .getDefaultSharedPreferences(preference.getContext())
              .getInt(preference.getKey(), defaultValue))
    }

    fun bindPreferenceSummaryToBoolean(
        preference: Preference, defaultValue: Boolean) {
      preference.setOnPreferenceChangeListener(prefBinder)
      prefBinder.onPreferenceChange(
          preference,
          PreferenceManager
              .getDefaultSharedPreferences(preference.getContext())
              .getBoolean(preference.getKey(), defaultValue))
    }

    fun sortLengths(pref: SharedPreferences) {
      val lengths = mutableListOf<Int>()
      val keys = listOf(KEY_LENGTH1, KEY_LENGTH2, KEY_LENGTH3, KEY_LENGTH4)
      val defaults = listOf(
          DEFAULT_LENGTH1,
          DEFAULT_LENGTH2,
          DEFAULT_LENGTH3,
          DEFAULT_LENGTH4)
      for (i in 0 until 4) {
        lengths.add(pref.getInt(keys[i], defaults[i]));
      }
      val sorted = lengths.sorted()
      pref.edit().let { editor ->
        for (i in 0 until 4) {
          editor.putInt(keys.get(i), sorted.get(i))
        }
        editor.commit()
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setupActionBar()
  }

  fun setupActionBar() {
    // Show the Up button in the action bar.
    getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onIsMultiPane(): Boolean =
    ((getResources().getConfiguration().screenLayout
        and Configuration.SCREENLAYOUT_SIZE_MASK)
      >= Configuration.SCREENLAYOUT_SIZE_XLARGE)

  override fun onHeaderClick(header: Header, position: Int) {
    if (header.titleRes == R.string.pref_header_lengths) {
      sortLengths(PreferenceManager.getDefaultSharedPreferences(this))
    }
    super.onHeaderClick(header, position)
  }

  override fun onStop() {
    sortLengths(PreferenceManager.getDefaultSharedPreferences(this))
    super.onStop()
  }

  override fun isValidFragment(fragmentName: String): Boolean =
    allowedClassNames.contains(fragmentName)

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  override fun onBuildHeaders(target: MutableList<Header>) =
    loadHeadersFromResource(R.xml.pref_headers, target)
}
