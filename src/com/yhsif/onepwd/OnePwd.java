package com.yhsif.onepwd;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class OnePwd extends Activity implements View.OnClickListener {
  private final static int USAGE_TIMEFRAME = 24 * 60 * 60 * 1000; // 24 hours

  static final String PREF = "com.yhsif.onepwd";
  static final String KEY_SELECTED_LENGTH = "selected_length";
  static final String PKG_SELF = "com.yhsif.onepwd";

  RadioGroup lengthGroup;
  TextView master;
  TextView site;
  TextView password;
  List<Integer> radioButtons;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    findViewById(R.id.generate).setOnClickListener(this);
    findViewById(R.id.settings).setOnClickListener(this);
    findViewById(R.id.close).setOnClickListener(this);

    lengthGroup = (RadioGroup) findViewById(R.id.length_group);
    master = (TextView) findViewById(R.id.master_key);
    site = (TextView) findViewById(R.id.site_key);
    password = (TextView) findViewById(R.id.password);

    radioButtons = Arrays.asList(
        R.id.length1,
        R.id.length2,
        R.id.length3,
        R.id.length4);

    NotificationService.run(this);
  }

  @Override
  public void onPause() {
    super.onPause();

    SharedPreferences.Editor editor = getSharedPreferences(PREF, 0).edit();

    int id = lengthGroup.getCheckedRadioButtonId();
    int index = radioButtons.size() - 1;
    for (int i = 0; i < radioButtons.size(); i++) {
      if(radioButtons.get(i) == id) {
        index = i;
        break;
      }
    }
    editor.putInt(KEY_SELECTED_LENGTH, index);

    editor.commit();
  }

  @Override
  public void onResume() {
    SharedPreferences pref = getSharedPreferences(PREF, 0);
    int index = pref.getInt(KEY_SELECTED_LENGTH, radioButtons.size() - 1);
    if (index >= radioButtons.size() || index < 0) {
      index = radioButtons.size() - 1;
    }
    lengthGroup.check(radioButtons.get(index));

    prefillSiteKey();

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
      // TODO
    }
  }

  public void doGenerate() {
    RadioButton button =
        (RadioButton) findViewById(lengthGroup.getCheckedRadioButtonId());
    int length = Integer.valueOf(button.getText().toString());
    String siteStr = site.getText().toString();
    siteStr = siteStr.trim();
    site.setText(siteStr);
    byte[] array;
    try {
      array = HmacMd5.hmac(
          master.getText().toString(),
          siteStr);
    } catch (java.security.NoSuchAlgorithmException e) {
      // won't happen
      return;
    }
    String value =
        Base64.encodeToString(array, Base64.URL_SAFE | Base64.NO_PADDING)
            .replaceAll("\\+", "")
            .replaceAll("\\/", "")
            .replaceAll("_", "")
            .substring(0, length);

    password.setText(value);
  }

  private void prefillSiteKey() {
    String pkg = getForegroundApp();
    if (pkg != null) {
      String[] segments = pkg.split("\\.");
      if (segments.length >= 2) {
        String sitekey = segments[1];
        site.setText(sitekey);
      }
    }
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
}
