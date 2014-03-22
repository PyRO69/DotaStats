package com.example.dotastats.activities;

import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
 * User info Tab that displays all the users basic ingame info.
 * 
 * @author swaroop
 */
public class InfoTabActivity extends Fragment {

	private ImageView profileImage;
	private TextView profileName;
	private TextView record;
	private TextView winRate;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.info_tab, container, false);
		profileImage =  (ImageView) view.findViewById(R.id.profileimage);
		profileName = (TextView) view.findViewById(R.id.profilename);
		record = (TextView) view.findViewById(R.id.recordedmatches);
		winRate = (TextView) view.findViewById(R.id.winrate);

		// Get the parsed user info from the parser for this user.
		DownloadResult myResult = JSoupCleaner.getUserInfo(getActivity().getIntent().getExtras().getString("LINK"));

		// If the result is failure, Toast a fail message and end the activity.
		// Else, update the view with the information.
		if(myResult == null || myResult.isFailure()) {

			Toast.makeText(getActivity(), "Failed to acquire User Info. Please try again.", Toast.LENGTH_SHORT).show();
			getActivity().finish();

		} else {

			Map<String, String> userInfo = myResult.getUserInfo();
			profileImage.setImageBitmap(GetImageFromURL.getImageFromURL(userInfo.get("PROFILEIMAGE")));
			profileName.setText(userInfo.get("PROFILENAME"));
			record.setText(userInfo.get("RECORD"));
			winRate.setText(userInfo.get("WINRATE"));
		}
		myResult = null;

		return view;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindDrawables(getActivity().findViewById(R.layout.info_tab));
		System.gc();
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindDrawables(getActivity().findViewById(R.layout.info_tab));
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
