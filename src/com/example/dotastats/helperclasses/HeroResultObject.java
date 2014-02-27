package com.example.dotastats.helperclasses;

import android.graphics.Bitmap;

public class HeroResultObject {

	private Bitmap heroImage;
	private String heroName;
	private String heroWinRate;
	private String kda;
	private String numMatches;

	public HeroResultObject(Bitmap img, String heroName, String heroWinRate, String kda, String numMatches) {
		this.setHeroImage(img);
		this.setHeroName(heroName);
		this.setHeroWinRate(heroWinRate);
		this.setKda(kda);
		this.setNumMatches(numMatches);
	}

	public Bitmap getHeroImage() {
		return heroImage;
	}

	public void setHeroImage(Bitmap heroImage) {
		this.heroImage = heroImage;
	}

	public String getHeroName() {
		return this.heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public String getHeroWinRate() {
		return this.heroWinRate;
	}

	public void setHeroWinRate(String heroWinRate) {
		this.heroWinRate = heroWinRate;
	}

	public String getKda() {
		return this.kda;
	}

	public void setKda(String kda) {
		this.kda = kda;
	}

	public String getNumMatches() {
		return this.numMatches;
	}

	public void setNumMatches(String numMatches) {
		this.numMatches = numMatches;
	}
}
