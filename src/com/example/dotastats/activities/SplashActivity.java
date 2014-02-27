package com.example.dotastats.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.dotastats.R;

public class SplashActivity extends Activity {

	private final int SPLASH_LENGTH = 1000; //in ms.

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		//This will run for a second and start the main Activity.
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(mainActivityIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_LENGTH);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.layout.splashscreen));

		System.gc();
	}

	@Override
	public void onPause() {
		super.onPause();
		System.gc();
	}

	public void unbindDrawables(View view) {

		if(view != null) {

			if(view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}
			if(view instanceof ViewGroup && !(view instanceof AdapterView)) {
				for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
				((ViewGroup) view).removeAllViews();
			}
		}

	}

}
