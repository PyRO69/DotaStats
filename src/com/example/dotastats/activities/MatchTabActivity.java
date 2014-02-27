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
import com.example.dotastats.adapters.ListViewAdapterForMatches;
import com.example.dotastats.helperclasses.DownloadResult;
import com.example.dotastats.helperclasses.GetImageFromURL;
import com.example.dotastats.helperclasses.MatchResultObject;
import com.example.dotastats.parsing.JSoupCleaner;

public class MatchTabActivity extends ListFragment {


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		DownloadResult result = JSoupCleaner.getMatchListAndInfo(getActivity().getIntent().getExtras().getString("LINK") + "/matches");
		if(result.isFailure() || result.isRedirected()) {

			Toast.makeText(getActivity(), "Failed to Retrieve List.", Toast.LENGTH_SHORT).show();
			getActivity().finish();

		} else {

			HashMap<String,List<String>> myMatchList = result.getMatchList();
			List<String> heroNames = myMatchList.get("HERONAMES");
			List<String> heroImages = myMatchList.get("HEROIMAGES");
			List<String> matchIds = myMatchList.get("MATCHIDS");

			MatchResultObject[] results =  new MatchResultObject[20];

			for(int i=0; i < heroNames.size(); i++) {
				results[i] = new MatchResultObject(GetImageFromURL.getImageFromURL(heroImages.get(i).toString()), heroNames.get(i).toString(), matchIds.get(i).toString());		
			}

			ListViewAdapterForMatches myAdapter = new ListViewAdapterForMatches(getActivity(),
							R.layout.match_list_view, results);
			setListAdapter(myAdapter);
		}

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
