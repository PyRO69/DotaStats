package com.example.dotastats.activities;

import java.util.ArrayList;
import java.util.Arrays;
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

/*
 * Player list activity that creates a grid of all user names that match
 * the searched name.
 * 
 * @author swaroop
 */
public class PlayerListSelectionActivity extends Activity {

	private GridView myGridView;
	private HashMap<String, String> nameAndPageMappings = new HashMap<String, String>();

	private static final String websitelink = "http://dotabuff.com";
	private static final String teamIdentifier = "/teams/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_name_selection);

		// Get the Player names and their pagelinks and create a mapping to be used later for the onclicklistener.
		createPlayerNameLinkMapping(getIntent().getExtras().getStringArrayList("NAME"), getIntent().getExtras().getStringArrayList("PAGE"));

		this.myGridView = (GridView) findViewById(R.id.mygridview);

		// I've used the adapter with an Array rather than a list. Can maybe eliminate this to save creating an unecessary object ?
		Object[] myresult = getIntent().getExtras().getStringArrayList("NAME").toArray();
		this.myGridView.setAdapter(new PlayerListAdapter(this, Arrays.copyOf(myresult,myresult.length, String[].class)));

		this.myGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				// Get the link for the username clicked on and pass to the info activity.
				String link = websitelink + 
						getPageFromName(((TextView) arg1.findViewById(R.id.grid_item_label)).getText().toString());
				Intent playerPageIntent;

				// Setup view based on whether the user is a team or an individual player.
				if(link.contains(teamIdentifier)) {
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

				// set arguments for Fragments in the tabs.
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

	/**
	 * Create a mapping of each players name to their respective
	 * page links.
	 * @param playerNames
	 * @param playerPages
	 */
	private void createPlayerNameLinkMapping(ArrayList<String> playerNames, ArrayList<String> playerPages) {

		if(playerNames.size() ==  playerPages.size()) {

			for(int i=0; i < playerPages.size(); i++) {
				this.nameAndPageMappings.put(playerNames.get(i), playerPages.get(i));
			}
		}
	}

	/**
	 * Get the Link for the Username.
	 * @param name
	 * @return
	 */
	private String getPageFromName(String name) {
		return this.nameAndPageMappings.get(name);
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

	/**
	 * Simple Function to unbind Views to save memory.
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
