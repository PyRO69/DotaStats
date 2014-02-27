package com.example.dotastats.activities;

import java.util.HashMap;

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

public class TeamInfoActivity extends Activity {

	private ImageView teamImage;
	private TextView teamName;
	private TextView teammatches;
	private TextView teamwinrate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_info);
		String Link =  getIntent().getExtras().getString("LINK");
		assignViews();

		DownloadResult myResults = JSoupCleaner.getUserInfo(Link);
		if(myResults.isFailure()) {

			Toast.makeText(this, "Failed to acquire Team Info. Please try again.", Toast.LENGTH_SHORT).show();
			this.finish();

		} else {

			HashMap<String, String> teamInfo =  myResults.getUserInfo();
			teamImage.setImageBitmap(GetImageFromURL.getImageFromURL(teamInfo.get("PROFILEIMAGE")));
			teamName.setText(teamInfo.get("PROFILENAME"));
			teammatches.setText(teamInfo.get("RECORD"));
			teamwinrate.setText(teamInfo.get("WINRATE"));

		}

	}

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
