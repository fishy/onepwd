package com.yhsif.onepwd;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {
  private static final String KEY_SETTINGS_INTENT = "dummy_settings_intent";

  static final String KEY_PREFILL_USAGE = "prefill_usage";
  static final boolean DEFAULT_PREFILL_USAGE = false;
  static final String KEY_COPY_CLIPBOARD = "copy_clipboard";
  static final boolean DEFAULT_COPY_CLIPBOARD = false;
  static final String KEY_CLEAR_CLIPBOARD = "clear_clipboard";
  static final String DEFAULT_CLEAR_CLIPBOARD = "60";
  static final String KEY_LENGTH1 = "length1";
  static final int DEFAULT_LENGTH1 = 8;
  static final String KEY_LENGTH2 = "length2";
  static final int DEFAULT_LENGTH2 = 10;
  static final String KEY_LENGTH3 = "length3";
  static final int DEFAULT_LENGTH3 = 15;
  static final String KEY_LENGTH4 = "length4";
  static final int DEFAULT_LENGTH4 = 20;
  static final String KEY_USE_SERVICE = "use_service";
  static final boolean DEFAULT_USE_SERVICE = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
  }

  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      // Show the Up button in the action bar.
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onIsMultiPane() {
    return (getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK)
      >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }

  @Override
  public void onHeaderClick(Header header, int position) {
    if (header.titleRes == R.string.pref_header_lengths) {
      sortLengths(getPreferenceManager().getDefaultSharedPreferences(this));
    }
    super.onHeaderClick(header, position);
  }

  @Override
  protected void onStop() {
    sortLengths(getPreferenceManager().getDefaultSharedPreferences(this));
    super.onStop();
  }

  @Override
  protected boolean isValidFragment(String fragmentName) {
    return PreferenceFragment.class.getName().equals(fragmentName)
        || PrefillPreferenceFragment.class.getName().equals(fragmentName)
        || ClipboardPreferenceFragment.class.getName().equals(fragmentName)
        || LengthsPreferenceFragment.class.getName().equals(fragmentName)
        || ServicePreferenceFragment.class.getName().equals(fragmentName)
        || AboutPreferenceFragment.class.getName().equals(fragmentName);
  }

  @Override
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public void onBuildHeaders(List<Header> target) {
    loadHeadersFromResource(R.xml.pref_headers, target);
  }

  private static Preference.OnPreferenceChangeListener prefBinder =
    new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object value) {
        if (preference instanceof ListPreference) {
          ListPreference listPreference = (ListPreference) preference;
          int index = listPreference.findIndexOfValue(value.toString());
          preference.setSummary(
              index >= 0
              ? listPreference.getEntries()[index]
              : null);
        } else if (preference.getKey().equals(KEY_COPY_CLIPBOARD)) {
          Preference clearClip =
            preference
                .getPreferenceManager()
                .findPreference(KEY_CLEAR_CLIPBOARD);
          if (value.equals(Boolean.TRUE)) {
            preference.setSummary(R.string.pref_desc_copy_clipboard_yes);
            clearClip.setEnabled(true);
          } else {
            preference.setSummary(R.string.pref_desc_copy_clipboard_no);
            clearClip.setEnabled(false);
          }
        } else if (preference.getKey().equals(KEY_PREFILL_USAGE)) {
          Preference settings =
            preference
                .getPreferenceManager()
                .findPreference(KEY_SETTINGS_INTENT);
          if (value.equals(Boolean.TRUE)) {
            preference.setSummary(R.string.pref_desc_usage_yes);
            settings.setEnabled(true);
          } else {
            preference.setSummary(R.string.pref_desc_usage_no);
            settings.setEnabled(false);
          }
        } else if (preference.getKey().equals(KEY_USE_SERVICE)) {
          preference.setSummary(R.string.pref_desc_service);
        } else {
          // For all other preferences, set the summary to the value's
          // simple string representation.
          preference.setSummary(value.toString());
        }
        return true;
      }
    };

  private static void bindPreferenceSummaryToString(Preference preference) {
    preference.setOnPreferenceChangeListener(prefBinder);
    prefBinder.onPreferenceChange(
        preference,
        PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(), ""));
  }

  private static void bindPreferenceSummaryToInt(
      Preference preference, int defaultValue) {
    preference.setOnPreferenceChangeListener(prefBinder);
    prefBinder.onPreferenceChange(
        preference,
        PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getInt(preference.getKey(), defaultValue));
  }

  private static void bindPreferenceSummaryToBoolean(
      Preference preference, boolean defaultValue) {
    preference.setOnPreferenceChangeListener(prefBinder);
    prefBinder.onPreferenceChange(
        preference,
        PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getBoolean(preference.getKey(), defaultValue));
  }

  static void sortLengths(SharedPreferences pref) {
    List<Integer> lengths = new ArrayList<>(4);
    List<String> keys =
      Arrays.asList(KEY_LENGTH1, KEY_LENGTH2, KEY_LENGTH3, KEY_LENGTH4);
    List<Integer> defaults =
      Arrays.asList(
          DEFAULT_LENGTH1,
          DEFAULT_LENGTH2,
          DEFAULT_LENGTH3,
          DEFAULT_LENGTH4);
    for (int i = 0; i < 4; i++) {
      lengths.add(pref.getInt(keys.get(i), defaults.get(i)));
    }
    Collections.sort(lengths);
    SharedPreferences.Editor editor = pref.edit();
    for (int i = 0; i < 4; i++) {
      editor.putInt(keys.get(i), lengths.get(i));
    }
    editor.commit();
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class PrefillPreferenceFragment
      extends BasePreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_prefill);
      setHasOptionsMenu(true);

      bindPreferenceSummaryToBoolean(
          findPreference(KEY_PREFILL_USAGE), DEFAULT_PREFILL_USAGE);
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class ClipboardPreferenceFragment
      extends BasePreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_clipboard);
      setHasOptionsMenu(true);

      bindPreferenceSummaryToBoolean(
          findPreference(KEY_COPY_CLIPBOARD), DEFAULT_COPY_CLIPBOARD);
      bindPreferenceSummaryToString(findPreference(KEY_CLEAR_CLIPBOARD));
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class LengthsPreferenceFragment extends BasePreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_lengths);
      setHasOptionsMenu(true);

      bindPreferenceSummaryToInt(findPreference(KEY_LENGTH1), DEFAULT_LENGTH1);
      bindPreferenceSummaryToInt(findPreference(KEY_LENGTH2), DEFAULT_LENGTH2);
      bindPreferenceSummaryToInt(findPreference(KEY_LENGTH3), DEFAULT_LENGTH3);
      bindPreferenceSummaryToInt(findPreference(KEY_LENGTH4), DEFAULT_LENGTH4);
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class ServicePreferenceFragment extends BasePreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_service);
      setHasOptionsMenu(true);

      bindPreferenceSummaryToBoolean(
          findPreference(KEY_USE_SERVICE), DEFAULT_USE_SERVICE);
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class AboutPreferenceFragment extends BasePreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_about);
      setHasOptionsMenu(true);
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private static abstract class BasePreferenceFragment
      extends PreferenceFragment {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == android.R.id.home) {
        getFragmentManager().popBackStack();
      }
      return super.onOptionsItemSelected(item);
    }
  }
}
