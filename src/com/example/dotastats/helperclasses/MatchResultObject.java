package com.example.dotastats.helperclasses;

import android.graphics.Bitmap;

public class MatchResultObject {

	private Bitmap heroImage;
	private String heroName;
	private String matchID;
	
	public MatchResultObject(Bitmap img, String heroName, String matchID) {
		this.setHeroImage(img);
		this.setHeroName(heroName);
		this.setMatchID(matchID);
	}

	public Bitmap getHeroImage() {
		return heroImage;
	}

	public void setHeroImage(Bitmap heroImage) {
		this.heroImage = heroImage;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public String getMatchID() {
		return matchID;
	}

	public void setMatchID(String matchID) {
		this.matchID = matchID;
	}
}
