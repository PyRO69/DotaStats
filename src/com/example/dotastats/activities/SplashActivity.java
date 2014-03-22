package com.example.dotastats.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.dotastats.R;

/*
 * Creates a Splash screen when the App is started. This activity
 * is then destroyed and the main activity takes over.
 * 
 * @author swaroop
 */
public class SplashActivity extends Activity {

	private final int SPLASH_LENGTH = 1000; //Time to run in milliseconds.

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		//This will run for a second and start the main Activity and finish the splash
		// screen activity.
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

	/**
	 * Simple function to unbind Drawables to reduce memory usage when
	 * views are in the background or closed.
	 * @param view
	 */
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
