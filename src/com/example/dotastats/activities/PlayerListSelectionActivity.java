package com.example.dotastats.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.example.dotastats.R;
import com.example.dotastats.adapters.PlayerListAdapter;

public class PlayerListSelectionActivity extends Activity {

	private GridView myGridView;
	private String[] playerNames;
	private String[] playerPages;
	private HashMap<String, String> nameAndPage = new HashMap<String, String>();
	private final String websitelink = "http://dotabuff.com";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_name_selection);

		ArrayList<String> nameList = getIntent().getExtras().getStringArrayList("NAME");
		ArrayList<String> pageList = getIntent().getExtras().getStringArrayList("PAGE");

		playerNames = (String[]) nameList.toArray(new String[nameList.size()]);
		playerPages = (String[]) pageList.toArray(new String[pageList.size()]);

		if(playerNames.length ==  playerPages.length) {

			for(int i=0; i < playerPages.length; i++) {
				nameAndPage.put(playerNames[i], playerPages[i]);
			}
		}

		myGridView = (GridView) findViewById(R.id.mygridview);
		myGridView.setAdapter(new PlayerListAdapter(this, playerNames));

		myGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				String link = websitelink + 
						getPageFromName(((TextView) arg1.findViewById(R.id.grid_item_label)).getText().toString());
				Intent playerPageIntent;

				if(link.contains("/teams/")) {
					playerPageIntent = new Intent(PlayerListSelectionActivity.this, TeamInfoActivity.class);
				} else {
					playerPageIntent = new Intent(PlayerListSelectionActivity.this, TabSwitchActivity.class);
				}

				playerPageIntent.putExtra("LINK", link);
				startActivity(playerPageIntent);

				InfoTabActivity myInfoFragment = new InfoTabActivity();
				MatchTabActivity myMatchFragment = new MatchTabActivity();
				Bundle myBundle = new Bundle();

				myBundle.putString("LINK", link);

				myMatchFragment.setArguments(myBundle);
				myInfoFragment.setArguments(myBundle);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String getPageFromName(String name) {
		return nameAndPage.get(name);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.id.mygridview));

		System.gc();
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindDrawables(findViewById(R.id.mygridview));
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
