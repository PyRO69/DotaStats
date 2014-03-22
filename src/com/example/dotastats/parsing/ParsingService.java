package com.example.dotastats.parsing;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.example.dotastats.helperclasses.DownloadResult;

/*
 * The background Parsing service that the app hooks on to to get all the information.
 * 
 * @author swaroop
 */
public class ParsingService extends IntentService {

	public static final String NOTIFICATION = "com.example.dotastats.ParsingService";

	public ParsingService() {
		super("DotaStats Parsing Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		if(intent == null) {
			System.out.println("Null Intent Sent to Parsing Service !");
			broadcastFailure();
			return;
		}

		String userName = intent.getExtras().getString("UserName");
		Intent returnIntent = new Intent(NOTIFICATION);

		DownloadResult result = JSoupCleaner.getNameList(userName);
		if(result == null || result.isFailure()) {
			broadcastFailure();
			return;
		} else if (result.isRedirected()) {
			returnIntent.putExtra("RESULT", "REDIRECT");
			returnIntent.putExtra("REDIRECT_LINK", result.getRedirectLink());
		} else { 
			Bundle returnData = bundleAllNames(result.getNameList());
			returnIntent.putExtra("RESULT", "SUCCESS");
			returnIntent.putExtras(returnData);
		}

		sendBroadcast(returnIntent);
	}

	/**
	 * Bundles all the Hashmap Values so that they can be passed via an Intent.
	 * @param input
	 * @return
	 */
	private Bundle bundleAllNames(HashMap<String, String> input) {

		if(input == null) {
			System.out.println("Names Hashmap is Null !");
			return null;
		}

		Bundle myBundle = new Bundle();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> pages = new ArrayList<String>();

		for(String key : input.keySet()) {
			names.add(key);
			pages.add(input.get(key));
		}

		myBundle.putStringArrayList("NAME", names);
		myBundle.putStringArrayList("PAGE", pages);

		return myBundle;
	}

	/**
	 * Returns A failure Broadcast to calling activity.
	 */
	protected void broadcastFailure() {

		Intent returnIntent = new Intent(NOTIFICATION);
		returnIntent.putExtra("RESULT", "FAILURE");
		sendBroadcast(returnIntent);

	}

}
