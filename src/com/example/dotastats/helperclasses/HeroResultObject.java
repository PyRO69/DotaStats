package com.example.dotastats.helperclasses;

import android.graphics.Bitmap;

/*
 * Immutabe Result object to hold all the users hero info.
 * 
 * @author swaroop
 */
public class HeroResultObject {

	private final Bitmap heroImage;
	private final String heroName;
	private final String heroWinRate;
	private final String kda;
	private final String numMatches;

	public HeroResultObject(Bitmap img, String heroName, String heroWinRate, String kda, String numMatches) {
		this.heroImage = img;
		this.heroName = heroName;
		this.heroWinRate = heroWinRate;
		this.kda = kda;
		this.numMatches = numMatches;
	}

	public Bitmap getHeroImage() {
		return heroImage;
	}

	public String getHeroName() {
		return this.heroName;
	}

	public String getHeroWinRate() {
		return this.heroWinRate;
	}

	public String getKda() {
		return this.kda;
	}

	public String getNumMatches() {
		return this.numMatches;
	}

}
