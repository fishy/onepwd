package com.yhsif.onepwd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class OnePwd extends Activity implements View.OnClickListener {

	RadioGroup lengthGroup;
	TextView master;
	TextView site;
	TextView password;

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
		value = value.substring(0, length);

		password.setText(value);
	}

}
