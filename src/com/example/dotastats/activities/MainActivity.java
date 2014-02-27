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

public class MainActivity extends Activity {

	private EditText myQuery;
	private Button sendData;
	private TextView textView;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if(context == null || intent == null) {
				System.out.println("Invalid Arguments passed !");
				return;
			}

			Bundle bundle = intent.getExtras();

			if(bundle == null || intent.getStringExtra("RESULT").equals("FAILURE")) {

				Toast.makeText(MainActivity.this, "Search Failed. Please try again.", Toast.LENGTH_SHORT).show();
				textView.setText("Search Failed.");

			} else if(intent.getStringExtra("RESULT").equals("REDIRECT")) {

				textView.setText("Found User.");
				Intent redirectIntent;
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
				myMatchFragment.setArguments(myBundle);
				myInfoFragment.setArguments(myBundle);
				startActivity(redirectIntent);

			} else {

				textView.setText("Success");
				Intent playerListIntent = new Intent(MainActivity.this, PlayerListSelectionActivity.class);
				playerListIntent.putExtras(bundle);
				startActivity(playerListIntent);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		assignViews();
		sendData.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String queryString = myQuery.getText().toString();
				if(queryString.isEmpty() || queryString == null) {
					Toast.makeText(MainActivity.this, "Please Enter a Player Name to search", Toast.LENGTH_SHORT).show();
					return;
				}

				Intent sendQueryIntent = new Intent(MainActivity.this, ParsingService.class);
				sendQueryIntent.putExtra("UserName", myQuery.getText().toString());
				MainActivity.this.startService(sendQueryIntent);
				Toast.makeText(MainActivity.this, "Searching for User name:" + queryString, Toast.LENGTH_LONG).show();
				textView.setText("Searching...");
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(ParsingService.NOTIFICATION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	/**
	 * Just assign The views to the members
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
