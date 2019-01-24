package com.yhsif.onepwd.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceScreen

import com.yhsif.onepwd.OnePwd
import com.yhsif.onepwd.R

public class SettingsActivity
: AppCompatActivity()
, PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

  companion object {
    private const val KEY_SETTINGS_INTENT = "dummy_settings_intent"

    public const val KEY_BIO_AUTOLOAD = "bio_autoload"
    public const val DEFAULT_BIO_AUTOLOAD = true
    public const val KEY_BIO_INVALIDATE = "bio_invalidate"
    public const val DEFAULT_BIO_INVALIDATE = true
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
        lengths.add(pref.getInt(keys[i], defaults[i]))
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
    setContentView(R.layout.settings)

    if (savedInstanceState == null) {
      var frag = getSupportFragmentManager().findFragmentByTag(
          PrefsFragment.FRAGMENT_TAG)
      if (frag == null) {
        frag = PrefsFragment()
      }

      getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, frag, PrefsFragment.FRAGMENT_TAG)
        .commit()
    }
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

  override fun onStop() {
    sortLengths(PreferenceManager.getDefaultSharedPreferences(this))
    super.onStop()
  }

  override fun onPreferenceStartFragment(
      caller: PreferenceFragmentCompat, pref: Preference): Boolean {
    getSupportFragmentManager().beginTransaction().let { ft ->
      val key = pref.getKey()
      val args = Bundle()
      args.putString(
          PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, key)
      val frag: BasePreferenceFragment? =
        when(key) {
          getString(R.string.pref_tag_about) -> AboutPreferenceFragment()
          getString(R.string.pref_tag_bio) -> BioPreferenceFragment()
          getString(R.string.pref_tag_clipboard) ->
            ClipboardPreferenceFragment()
          getString(R.string.pref_tag_lengths) -> {
            sortLengths(PreferenceManager.getDefaultSharedPreferences(this))
            LengthsPreferenceFragment()
          }
          getString(R.string.pref_tag_prefill) -> PrefillPreferenceFragment()
          getString(R.string.pref_tag_service) -> ServicePreferenceFragment()
          else -> null
        }
      if (frag != null) {
        frag.setArguments(args)
        ft.replace(R.id.fragment_container, frag, key)
      }
      ft.addToBackStack(key)
      ft.commit()
    }
    return true
  }
}
