package com.example.dotastats.helperclasses;

import android.graphics.Bitmap;

/*
 * Immutable Class to hold the Match Results to be
 * passed to the Activity from the Parser.
 * 
 * @author swaroop
 */

public class MatchResultObject {

	private final Bitmap heroImage;
	private final String heroName;
	private final String matchID;
	
	public MatchResultObject(Bitmap img, String heroName, String matchID) {
		this.heroImage = img;
		this.heroName = heroName;
		this.matchID = matchID;
	}

	public Bitmap getHeroImage() {
		return heroImage;
	}

	public String getHeroName() {
		return heroName;
	}

	public String getMatchID() {
		return matchID;
	}

}
