package com.example.dotastats.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.example.dotastats.R;

/*
 * This is the fragment activity that creates all the tabs and handles the
 * switching between tabs as well as passing the intent to the correct tabs.
 * 
 * @author swaroop
 */
public class TabSwitchActivity extends FragmentActivity {

	private FragmentTabHost myTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_view_tabs);

		//System.out.println(getIntent().getStringExtra("LINK"));
		InfoTabActivity myInfoFragment = new InfoTabActivity();
		Bundle myBundle = new Bundle();
		// Pass on the link passed from the calling activity to the tabs.
		myBundle.putString("LINK", getIntent().getStringExtra("LINK"));
		myInfoFragment.setArguments(myBundle);

		myTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		myTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		myTabHost.addTab(myTabHost.newTabSpec("info").setIndicator("Info"), InfoTabActivity.class, null);
		myTabHost.addTab(myTabHost.newTabSpec("matches").setIndicator("Matches"), MatchTabActivity.class, null);
		myTabHost.addTab(myTabHost.newTabSpec("heroes").setIndicator("Heroes"), HeroesTabActivity.class, null);
		myTabHost.addTab(myTabHost.newTabSpec("records").setIndicator("Records"), RecordsTabActivity.class, null);

	}

}
