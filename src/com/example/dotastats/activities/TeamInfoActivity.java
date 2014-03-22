package com.example.dotastats.activities;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotastats.R;
import com.example.dotastats.helperclasses.DownloadResult;
import com.example.dotastats.helperclasses.GetImageFromURL;
import com.example.dotastats.parsing.JSoupCleaner;

/*
 * This activity is a standalone for teams. The teams page
 * has only limited info and we display only that infomation.
 * 
 * @author swaroop
 */
public class TeamInfoActivity extends Activity {

	private ImageView teamImage;
	private TextView teamName;
	private TextView teammatches;
	private TextView teamwinrate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_info);
		assignViews();

		// Get the Team info. If this fails, toast a message and finish the activity
		// otherwise, update the view with the information.
		DownloadResult myResults = JSoupCleaner.getUserInfo(getIntent().getExtras().getString("LINK"));

		if(myResults == null || myResults.isFailure()) {

			Toast.makeText(this, "Failed to acquire Team Info. Please try again.", Toast.LENGTH_SHORT).show();
			this.finish();

		} else {

			Map<String, String> teamInfo =  myResults.getUserInfo();
			teamImage.setImageBitmap(GetImageFromURL.getImageFromURL(teamInfo.get("PROFILEIMAGE")));
			teamName.setText(teamInfo.get("PROFILENAME"));
			teammatches.setText(teamInfo.get("RECORD"));
			teamwinrate.setText(teamInfo.get("WINRATE"));

		}

	}

	/**
	 * Assign the views to the variables.
	 */
	private void assignViews() {
		teamImage = (ImageView) findViewById(R.id.teamimage);
		teamName = (TextView) findViewById(R.id.teamname);
		teammatches = (TextView) findViewById(R.id.teammatches);
		teamwinrate = (TextView) findViewById(R.id.teamwinrate);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindDrawables(findViewById(R.layout.team_info));
		System.gc();
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindDrawables(findViewById(R.layout.team_info));
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
