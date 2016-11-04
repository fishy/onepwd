package com.yhsif.onepwd;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OnePwd extends AppCompatActivity implements
    View.OnClickListener, View.OnFocusChangeListener,
    TextView.OnEditorActionListener, RadioGroup.OnCheckedChangeListener {
  private final static String TAG = "onepwd";
  private final static int USAGE_TIMEFRAME = 24 * 60 * 60 * 1000; // 24 hours
  private final static Set<String> CHROME_PACKAGES = new HashSet(Arrays.asList(
        "com.chrome.canary",    // canary
        "com.chrome.dev",       // dev
        "com.chrome.beta",      // beta
        "com.android.chrome")); // stable
  private final static ClipData EMPTY_CLIP = ClipData.newPlainText("", "");

  static final String PREF = "com.yhsif.onepwd";
  static final String KEY_SELECTED_LENGTH = "selected_length";
  static final String PKG_SELF = "com.yhsif.onepwd";

  RadioGroup lengthGroup;
  TextView master;
  TextView site;
  TextView password;
  List<RadioButton> radioButtons;
  RadioButton checkedLength;
  int checkedIndex;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    findViewById(R.id.generate).setOnClickListener(this);
    findViewById(R.id.settings).setOnClickListener(this);
    findViewById(R.id.close).setOnClickListener(this);

    lengthGroup = (RadioGroup) findViewById(R.id.length_group);
    lengthGroup.setOnCheckedChangeListener(this);
    master = (TextView) findViewById(R.id.master_key);
    master.setOnFocusChangeListener(this);
    master.setOnEditorActionListener(this);
    site = (TextView) findViewById(R.id.site_key);
    site.setOnFocusChangeListener(this);
    site.setOnEditorActionListener(this);
    password = (TextView) findViewById(R.id.password);

    radioButtons = Arrays.asList(
        (RadioButton) findViewById(R.id.length1),
        (RadioButton) findViewById(R.id.length2),
        (RadioButton) findViewById(R.id.length3),
        (RadioButton) findViewById(R.id.length4));
    checkedIndex = radioButtons.size() - 1;
    checkedLength = radioButtons.get(checkedIndex);

    NotificationService.run(this);
  }

  @Override
  public void onPause() {
    super.onPause();

    SharedPreferences.Editor editor = getSharedPreferences(PREF, 0).edit();
    editor.putInt(KEY_SELECTED_LENGTH, checkedIndex);
    editor.commit();
  }

  @Override
  public void onResume() {
    SharedPreferences pref =
      PreferenceManager.getDefaultSharedPreferences(this);
    SettingsActivity.sortLengths(pref);
    radioButtons.get(0).setText(Integer.toString(pref.getInt(
            SettingsActivity.KEY_LENGTH1, SettingsActivity.DEFAULT_LENGTH1)));
    radioButtons.get(1).setText(Integer.toString(pref.getInt(
            SettingsActivity.KEY_LENGTH2, SettingsActivity.DEFAULT_LENGTH2)));
    radioButtons.get(2).setText(Integer.toString(pref.getInt(
            SettingsActivity.KEY_LENGTH3, SettingsActivity.DEFAULT_LENGTH3)));
    radioButtons.get(3).setText(Integer.toString(pref.getInt(
            SettingsActivity.KEY_LENGTH4, SettingsActivity.DEFAULT_LENGTH4)));

    int defaultIndex = radioButtons.size() - 1;
    int index =
      getSharedPreferences(PREF, 0).getInt(KEY_SELECTED_LENGTH, defaultIndex);
    if (index < 0 || index >= radioButtons.size()) {
      index = defaultIndex;
    }
    lengthGroup.check(radioButtons.get(index).getId());

    Intent intent = getIntent();
    String sitekey = null;
    if (intent != null && Intent.ACTION_SEND.equals(intent.getAction())) {
      sitekey = getSiteKeyFromIntent(intent);
    }
    if (sitekey == null) {
      sitekey = getSiteKeyFromForegroundApp();
    }
    if (sitekey != null) {
      site.setText(sitekey);
    }
    master.setText("");
    password.setText("");

    super.onResume();
  }

  // for View.OnClickListener
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.generate) {
      doGenerate();
    } else if (v.getId() == R.id.close) {
      this.finish();
    } else if (v.getId() == R.id.settings) {
      startActivity(new Intent(this, SettingsActivity.class));
    }
  }

  // for View.OnFocusChangeListener
  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    if (hasFocus) {
      if (v.getId() == master.getId()) {
        if (site.getText().toString().trim().isEmpty()) {
          master.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
          master.setImeOptions(EditorInfo.IME_ACTION_SEND);
        }
      } else if (v.getId() == site.getId()) {
        if (master.getText().toString().isEmpty()) {
          site.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
          site.setImeOptions(EditorInfo.IME_ACTION_SEND);
        }
      }
    }
  }

  // for TextView.OnEditorActionListener
  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if ((v.getId() == master.getId() || v.getId() == site.getId())
        && actionId == EditorInfo.IME_ACTION_SEND) {
      doGenerate();
      return true;
    }
    return false;
  }

  // for RadioGroup.OnCheckedChangeListener
  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    checkedLength = (RadioButton) findViewById(checkedId);
    checkedIndex = radioButtons.indexOf(checkedLength);
  }

  private void doGenerate() {
    int length = Integer.valueOf(checkedLength.getText().toString());
    String masterKey = master.getText().toString();
    if (masterKey.isEmpty()) {
      Toast
        .makeText(this, R.string.empty_master_toast, Toast.LENGTH_LONG)
        .show();
      return;
    }

    String siteKey = site.getText().toString().trim();
    site.setText(siteKey);
    byte[] array;
    try {
      array = HmacMd5.hmac(masterKey, siteKey);
    } catch (java.security.NoSuchAlgorithmException e) {
      // won't happen
      return;
    }
    final String value =
        Base64.encodeToString(array, Base64.URL_SAFE | Base64.NO_PADDING)
            .replaceAll("\\+", "")
            .replaceAll("\\/", "")
            .replaceAll("_", "")
            .substring(0, length);

    password.setText(value);
    SharedPreferences pref =
      PreferenceManager.getDefaultSharedPreferences(this);
    if (pref.getBoolean(
          SettingsActivity.KEY_COPY_CLIPBOARD,
          SettingsActivity.DEFAULT_COPY_CLIPBOARD)) {
      final ClipboardManager clip =
        (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
      ClipData clipData = ClipData.newPlainText("", value);
      clip.setPrimaryClip(clipData);
      Toast.makeText(this, R.string.clip_toast, Toast.LENGTH_LONG).show();
      String timeStr =
        pref.getString(
            SettingsActivity.KEY_CLEAR_CLIPBOARD,
            SettingsActivity.DEFAULT_CLEAR_CLIPBOARD);
      long time = Long.parseLong(timeStr);
      if (time > 0) {
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            if (clip.hasPrimaryClip()) {
              ClipData.Item item = clip.getPrimaryClip().getItemAt(0);
              if (item.getText().toString().equals(value)) {
                clip.setPrimaryClip(EMPTY_CLIP);
                Toast.makeText(
                    OnePwd.this,
                    R.string.clear_clip_toast,
                    Toast.LENGTH_LONG).show();
              }
            }
          }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, time * 1000);
      }
    }
  }

  private String getSiteKeyFromForegroundApp() {
    SharedPreferences pref =
      PreferenceManager.getDefaultSharedPreferences(this);
    if (!pref.getBoolean(
          SettingsActivity.KEY_PREFILL_USAGE,
          SettingsActivity.DEFAULT_PREFILL_USAGE)) {
      return null;
    }

    String pkg = getForegroundApp();
    if (pkg != null) {
      Log.v(TAG, String.format("Package is \"%s\"", pkg));
      if (CHROME_PACKAGES.contains(pkg)) {
        Toast.makeText(this, R.string.chrome_toast, Toast.LENGTH_LONG).show();
      } else {
        String[] segments = pkg.split("\\.");
        if (segments.length > 1) {
          return segments[1];
        } else {
          return pkg;
        }
      }
    }
    return null;
  }

  private String getForegroundApp() {
    UsageStatsManager manager =
      (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
    long time = System.currentTimeMillis();
    List<UsageStats> apps =
      manager.queryUsageStats(INTERVAL_DAILY, time - USAGE_TIMEFRAME, time);
    long max = 0;
    String result = null;
    if (apps != null) {
      for (UsageStats app : apps) {
        String pkg = app.getPackageName().toLowerCase();
        long timestamp = app.getLastTimeUsed();
        if (pkg.equals(PKG_SELF)) {
          continue;
        }
        if (timestamp > max) {
          max = timestamp;
          result = pkg;
        }
      }
    }
    return result;
  }

  private String getSiteKeyFromIntent(Intent intent) {
    String url = intent.getStringExtra(Intent.EXTRA_TEXT);
    if (url == null || url.isEmpty()) {
      return null;
    }
    Uri uri = Uri.parse(url);
    String host = uri.getHost();
    if (host == null) {
      return null;
    }
    String[] segments = host.split("\\.");
    if (segments.length > 1) {
      return segments[segments.length - 2];
    } else {
      return host;
    }
  }
}
