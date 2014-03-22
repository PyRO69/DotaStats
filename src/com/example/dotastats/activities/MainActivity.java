package com.example.dotastats.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotastats.R;
import com.example.dotastats.parsing.ParsingService;

/*
 * Main Activity for the App. Handles the Inputs and starting of the Parsing Service.
 * 
 * @author swaroop
 */

public class MainActivity extends Activity {

	private EditText myQuery;
	private Button sendData;
	private TextView textView;
	private BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		assignViews();

		// Setup the broadcast reciever for the Parsing Service.
		this.receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				if(context == null || intent == null) {
					System.out.println("Invalid Arguments passed !");
					return;
				}

				Bundle bundle = intent.getExtras();

				// If the parsing service fails to retrieve the information for the user,
				// Toast a fail and message and update the view .
				if(bundle == null || intent.getStringExtra("RESULT").equals("FAILURE")) {

					Toast.makeText(MainActivity.this, "Search Failed. Please try again.", Toast.LENGTH_SHORT).show();
					textView.setText("Search Failed.");

				} else if(intent.getStringExtra("RESULT").equals("REDIRECT")) {

					// If the User name is a unique name that matches only 1 user, then skip the
					// User list view and directly use start the user Info activity.
					textView.setText("Found User.");
					Intent redirectIntent;

					// If the username searched is that of a team, redirect to the Team Info activity.
					// otherwise create the tabs for user info.
					if(intent.getStringExtra("REDIRECT_LINK").contains("/teams/")) {
						redirectIntent = new Intent(MainActivity.this, TeamInfoActivity.class);
					} else {
						redirectIntent = new Intent(MainActivity.this, TabSwitchActivity.class);
					}

					InfoTabActivity myInfoFragment = new InfoTabActivity();
					redirectIntent.putExtra("LINK", intent.getStringExtra("REDIRECT_LINK"));
					MatchTabActivity myMatchFragment = new MatchTabActivity();
					Bundle myBundle = new Bundle();
					myBundle.putString("LINK", intent.getStringExtra("REDIRECT_LINK"));

					// Since we're using Fragment Views for the tabs, we need to set the extra string
					// using the setArguments method.

					myMatchFragment.setArguments(myBundle);
					myInfoFragment.setArguments(myBundle);
					startActivity(redirectIntent);

				} else {

					// If the username matched multiple users (Thanks Valve !) then create the player
					// List so the user can select the correct one.
					textView.setText("Success");
					Intent playerListIntent = new Intent(MainActivity.this, PlayerListSelectionActivity.class);
					playerListIntent.putExtras(bundle);
					startActivity(playerListIntent);
				}
			}
		};
		// Register the receiver after setup.
		registerReceiver(receiver, new IntentFilter(ParsingService.NOTIFICATION));

		// Setup the click listener for the 'Send' button.
		sendData.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String queryString = myQuery.getText().toString();
				if(queryString.isEmpty() || queryString == null) {
					Toast.makeText(MainActivity.this, "Please Enter a Player Name to search", Toast.LENGTH_SHORT).show();
					return;
				}

				// Pass the User name to the Parsing Service via an intent.
				Intent sendQueryIntent = new Intent(MainActivity.this, ParsingService.class);
				sendQueryIntent.putExtra("UserName", myQuery.getText().toString());
				MainActivity.this.startService(sendQueryIntent);
				Toast.makeText(MainActivity.this, "Searching for User name:" + queryString, Toast.LENGTH_SHORT).show();
				textView.setText("Searching...");
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onResume();
		// Unregister our receiver to avoid leaking it.
		unregisterReceiver(receiver);
	}

	/**
	 * Just assign The views to the members variables.
	 */
	private void assignViews() {

		myQuery = (EditText) findViewById(R.id.username);
		sendData = (Button) findViewById(R.id.mysendbutton);
		textView = (TextView) findViewById(R.id.textView1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
