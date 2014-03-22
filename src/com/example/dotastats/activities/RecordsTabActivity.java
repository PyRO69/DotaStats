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
import com.example.dotastats.adapters.ListViewAdapterForRecords;
import com.example.dotastats.helperclasses.DownloadResult;
import com.example.dotastats.parsing.JSoupCleaner;

public class RecordsTabActivity extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		DownloadResult result =  JSoupCleaner.getUserRecords(getActivity().getIntent().getExtras().getString("LINK") + "/records");
		if(result.isFailure() || result.isRedirected()) {

			Toast.makeText(getActivity(), "Failed to Retrieve Records. Retry again.", Toast.LENGTH_SHORT).show();
			getActivity().finish();

		} else {

			HashMap<String, List<String>> records = result.getRecordList();
			List<String> recordNames = records.get("RECORD_NAMES");
			List<String> recordVals = records.get("RECORD_VALUES");

			String[] recordData = new String[recordNames.size()];
			for(int i=0; i < recordNames.size(); i++) {
				recordData[i] = recordNames.get(i) + ": " + recordVals.get(i);
			}

			ListViewAdapterForRecords myAdapter = new ListViewAdapterForRecords(getActivity(), R.layout.record_list_view, recordData);
			setListAdapter(myAdapter);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unbindDrawables(getActivity().findViewById(R.layout.record_list_view));

		System.gc();
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindDrawables(getActivity().findViewById(R.layout.record_list_view));
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
