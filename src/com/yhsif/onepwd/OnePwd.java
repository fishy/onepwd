package com.yhsif.onepwd;

import android.app.Activity;
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

	public static final String PREF = "com.yhsif.onepwd";
	public static final String KEY_SELECTED_LENGTH = "selected_length";

	RadioGroup lengthGroup;
	TextView master;
	TextView site;
	TextView password;
	List<Integer> radioButtons;

	@Override public void onCreate(Bundle savedInstanceState) {
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
	}

	@Override public void onPause() {
		super.onPause();

		SharedPreferences.Editor editor = getSharedPreferences(PREF, 0).edit();

		int id = lengthGroup.getCheckedRadioButtonId();
		int index = radioButtons.size() - 1;
		for(int i = 0; i < radioButtons.size(); i++)
			if(radioButtons.get(i) == id) {
				index = i;
				break;
			}
		editor.putInt(KEY_SELECTED_LENGTH, index);

		editor.commit();
	}

	@Override public void onResume() {
		SharedPreferences pref = getSharedPreferences(PREF, 0);
		int index = pref.getInt(KEY_SELECTED_LENGTH, radioButtons.size() - 1);
		if(index >= radioButtons.size() || index < 0)
			index = radioButtons.size() - 1;
		lengthGroup.check(radioButtons.get(index));

		super.onResume();
	}

	// for View.OnClickListener
	@Override public void onClick(View v) {
		switch (v.getId()) {
			case R.id.generate:
				doGenerate();
				break;
			case R.id.settings:
				break;
			case R.id.close:
				this.finish();
				break;
		}
	}

	public void doGenerate() {
		RadioButton button = (RadioButton) findViewById(lengthGroup.getCheckedRadioButtonId());
		int length = Integer.valueOf(button.getText().toString());
		byte[] array;
		try {
			array = HmacMd5.hmac(
					master.getText().toString(),
					site.getText().toString().trim());
		} catch (java.security.NoSuchAlgorithmException e) {
			// won't happen
			return;
		}
		String value = Base64.encodeToString(array, Base64.URL_SAFE | Base64.NO_PADDING);
		value = value.replaceAll("\\+", "");
		value = value.replaceAll("\\/", "");
		value = value.replaceAll("_", "");
		value = value.substring(0, length);

		password.setText(value);
	}

}
