package com.yhsif.onepwd;


import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {
  static final String KEY_COPY_CLIPBOARD = "copy_clipboard";
  static final boolean DEFAULT_COPY_CLIPBOARD = false;
  static final String KEY_CLEAR_CLIPBOARD = "clear_clipboard";
  static final String DEFAULT_CLEAR_CLIPBOARD = "60";

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
        } else {
          // For all other preferences, set the summary to the value's
          // simple string representation.
          preference.setSummary(value.toString());
        }
        return true;
      }
    };

  private static void bindPreferenceSummaryToValue(Preference preference) {
    preference.setOnPreferenceChangeListener(prefBinder);
    prefBinder.onPreferenceChange(
        preference,
        PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(), ""));
  }

  private static void bindPreferenceSummaryToValueBoolean(
      Preference preference, boolean defaultValue) {
    preference.setOnPreferenceChangeListener(prefBinder);
    prefBinder.onPreferenceChange(
        preference,
        PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getBoolean(preference.getKey(), defaultValue));
  }

  /**
   * This method stops fragment injection in malicious applications.
   * Make sure to deny any unknown fragments here.
   */
  protected boolean isValidFragment(String fragmentName) {
    return PreferenceFragment.class.getName().equals(fragmentName)
        || ClipboardPreferenceFragment.class.getName().equals(fragmentName);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class ClipboardPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_clipboard);
      setHasOptionsMenu(true);

      bindPreferenceSummaryToValueBoolean(
          findPreference(KEY_COPY_CLIPBOARD), DEFAULT_COPY_CLIPBOARD);
      bindPreferenceSummaryToValue(findPreference(KEY_CLEAR_CLIPBOARD));
    }

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
