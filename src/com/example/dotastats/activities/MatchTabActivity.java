package com.example.dotastats.activities;

import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.dotastats.R;
import com.example.dotastats.adapters.ListViewAdapterForMatches;
import com.example.dotastats.helperclasses.DownloadResult;
import com.example.dotastats.helperclasses.GetImageFromURL;
import com.example.dotastats.helperclasses.MatchResultObject;
import com.example.dotastats.parsing.JSoupCleaner;

/*
 * Match Tab. Lists all the users Matches and the Match-IDs.
 * 
 * @author swaroop
 */
public class MatchTabActivity extends ListFragment {

	private static final String matchLink = "/matches";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get the Match list for this User.
		DownloadResult result = JSoupCleaner.getMatchListAndInfo(getActivity().getIntent().getExtras().getString("LINK") + matchLink);

		// If the result failed to be retrieved, Toast a fail message.
		// Else, create the MatchResultObjects and pass that to the ViewAdapter.
		if(result == null || result.isFailure() || result.isRedirected()) {

			Toast.makeText(getActivity(), "Failed to Retrieve List.", Toast.LENGTH_SHORT).show();
			getActivity().finish();

		} else {

			HashMap<String,List<String>> myMatchList = result.getMatchList();
			List<String> heroNames = myMatchList.get("HERONAMES");
			List<String> heroImages = myMatchList.get("HEROIMAGES");
			List<String> matchIds = myMatchList.get("MATCHIDS");

			MatchResultObject[] results =  new MatchResultObject[20];

			for(int i=0; i < heroNames.size(); i++) {
				results[i] = createMatchResultObjectFromHeroInfo(GetImageFromURL.getImageFromURL(heroImages.get(i).toString()),
						heroNames.get(i).toString(), matchIds.get(i).toString());		
			}

			ListViewAdapterForMatches myAdapter = new ListViewAdapterForMatches(getActivity(),
							R.layout.match_list_view, results);
			setListAdapter(myAdapter);
		}

	}

	/**
	 * Static factory method to create a MatchResultObject.
	 * Ensures no news in the logic.
	 * 
	 * @param img
	 * @param heroName
	 * @param matchID
	 * @return
	 */
	private static MatchResultObject createMatchResultObjectFromHeroInfo(Bitmap img, String heroName, String matchID) {
		return new MatchResultObject(img, heroName, matchID);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindDrawables(getActivity().findViewById(R.layout.match_list_view));
		System.gc();
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindDrawables(getActivity().findViewById(R.layout.match_list_view));
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
