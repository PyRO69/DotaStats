package com.example.dotastats.activities;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.dotastats.R;
import com.example.dotastats.adapters.ListViewAdapterForHeroes;
import com.example.dotastats.helperclasses.DownloadResult;
import com.example.dotastats.helperclasses.HeroResultObject;
import com.example.dotastats.parsing.JSoupCleaner;

/*
 * The heroes Tab to display all the Hero Info. 
 * 
 * @author swaroop
 */
public class HeroesTabActivity extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//Get the Parsed Data for the Passed Intent.

		DownloadResult result = JSoupCleaner.getHerosPlayed(getActivity().getIntent().getExtras().getString("LINK") + "/heroes");

		//If the Result failed, Toast a fail message and close the activity.
		if(result == null || result.isFailure() || result.isRedirected()) {

			Toast.makeText(getActivity(), "Failed to Retrieve List.", Toast.LENGTH_SHORT).show();
			getActivity().finish();

		} else {
			List<HashMap<String,String>> heroList = result.getHeroesData();

			HeroResultObject[] results = new HeroResultObject[heroList.size()];

			// Since the Game has close to a hundred Heroes, downloading all the images is too expensive
			// and not worth the effort. So the heroes will be a list with relevant information.
			for(int i=0; i < heroList.size(); i++) {
				results[i] = new HeroResultObject(null,
						heroList.get(i).get("HERO_NAME").toString(), heroList.get(i).get("WINRATE").toString(), heroList.get(i).get("KDA").toString(),
						heroList.get(i).get("NUM_MATCHES").toString());
			}

			ListViewAdapterForHeroes myAdapter = new ListViewAdapterForHeroes(getActivity(), R.layout.hero_tab, results);
			setListAdapter(myAdapter);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindDrawables(getActivity().findViewById(R.layout.hero_tab));
		System.gc();
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindDrawables(getActivity().findViewById(R.layout.hero_tab));
		System.gc();
	}

	/**
	 * Simple function to unbind Drawables to reduce memory usage when
	 * views are in the background or closed.
	 * @param view
	 */
	private void unbindDrawables(View view) {

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
