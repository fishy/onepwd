package com.yhsif.onepwd;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public class OnePwd extends Activity implements View.OnClickListener {

	private Dialog dialog = null;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.generate).setOnClickListener(this);
		findViewById(R.id.settings).setOnClickListener(this);
		findViewById(R.id.close).setOnClickListener(this);
	}

	// for View.OnClickListener
	@Override public void onClick(View v) {
		switch (v.getId()) {
			case R.id.generate:
				break;
			case R.id.settings:
				break;
			case R.id.close:
				this.finish();
				break;
		}
	}

}
