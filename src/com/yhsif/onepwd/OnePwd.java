package com.yhsif.onepwd;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class OnePwd extends Activity
	implements DialogInterface.OnDismissListener, View.OnClickListener {

	private Dialog dialog = null;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dialog = new Dialog(this);
		dialog.setTitle(R.string.app_name);
		dialog.setContentView(R.layout.main);

		dialog.setOnDismissListener(this);
		dialog.findViewById(R.id.generate).setOnClickListener(this);
		dialog.findViewById(R.id.settings).setOnClickListener(this);
		dialog.findViewById(R.id.close).setOnClickListener(this);
		dialog.findViewById(R.id.length_widget).setOnClickListener(this);

		dialog.show();
	}

	// for DialogInterface.OnDismissListener
	@Override public void onDismiss(DialogInterface dialog) {
		this.finish();
	}

	// for View.OnClickListener
	@Override public void onClick(View v) {
		switch (v.getId()) {
			case R.id.generate:
				break;
			case R.id.settings:
				break;
			case R.id.close:
				dialog.dismiss();
				break;
			case R.id.length_widget:
				break;
		}
	}

}
