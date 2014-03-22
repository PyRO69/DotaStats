package com.example.dotastats.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.dotastats.helperclasses.DownloadResult;
import com.example.dotastats.helperclasses.DownloadResult.RESULT_TYPE;

/*
 * Main Parser class for extracting all the User Data
 * for all Activities. Included wrapper methods to expose only the
 * minimum information required. All the tags were isolated by looking
 * at dotabuff's source. 
 * 
 * Any change to their source and this is the class
 * that will break the app.
 * 
 * @author swaroop
 */
public class HtmlParser {

	/**
	 * Returns a map of Player names as Key and their player number to search
	 * as Value.
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult stripNamesLogic(Document fetchedDocument) {
		
		if(fetchedDocument == null || !fetchedDocument.hasText()) {
			System.out.println("The Document passed to stripNames is Empty or NULL");
			return null;
		}

		Elements playerNames = fetchedDocument.getElementsByClass("name");
		HashMap<String, String> namesAndPages = new HashMap<String, String>();

		for(Element i : playerNames) {
			namesAndPages.put(i.getElementsByAttribute("href").text(),
					i.getElementsByTag("a").attr("href"));
		}

		DownloadResult myResult = new DownloadResult.DownloadResultBuilder().failure(false).redirected(false).
				setResultType(RESULT_TYPE.RESULT_TYPE_NAMELIST).setNameList(namesAndPages).build();

		return myResult;
	}

	/**
	 * Extracts the names and returns them.
	 * @param fetchedDocument
	 * @return
	 */
	public static DownloadResult stripNames(Document fetchedDocument) {
		//Wrapper Method
		return stripNamesLogic(fetchedDocument);
	}

	/**
	 * Extracts out the Image URLs, Hero Names and Match IDs.
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult getMatchListsLogic(Document fetchedDocument) {

		if(fetchedDocument == null || !fetchedDocument.hasText()) {
			System.out.println("The Document passed to getInfo is Empty or NULL");
			return null;
		}

		HashMap<String, List<String>>  matchLists =  new HashMap<String, List<String>>();
		Elements images = fetchedDocument.getElementsByClass("image-hero");
		Elements otherData =  fetchedDocument.getElementsByClass("matchid");

		int listSize = 0;
		if(images.size() != otherData.size()) {
			return null;
		} else {
			listSize = (images.size() >= 20) ? 20 : images.size(); 
		}

		ArrayList<String> imgs = new ArrayList<String>(listSize);
		ArrayList<String> heronames = new ArrayList<String>(listSize);
		ArrayList<String> matchid = new ArrayList<String>(listSize);

		for(int i=0; i < listSize; i++) {
			imgs.add(i, images.get(i).attr("src"));
			heronames.add(i, images.get(i).attr("alt"));
			matchid.add(i, otherData.get(i).text());
		}

		matchLists.put("HEROIMAGES", imgs);
		matchLists.put("HERONAMES", heronames);
		matchLists.put("MATCHIDS", matchid);

		DownloadResult myResult = new DownloadResult.DownloadResultBuilder().failure(false).redirected(false).
				setResultType(RESULT_TYPE.RESULT_TYPE_MATCHLIST).setMatchList(matchLists).build();

		return myResult;
	}

	/**
	 * Extracts the Match Lists for a given user.
	 * @param fetchedDocument
	 * @return
	 */
	public static DownloadResult getMatchLists(Document fetchedDocument) {
		return getMatchListsLogic(fetchedDocument);
	}

	/**
	 * Extracts the users Records in Dota and returns in a Download Result object.
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult getRecordsLogic(Document fetchedDocument) {

		if(fetchedDocument == null || !fetchedDocument.hasText()) {
			System.out.println("The Document passed to getRecords is Empty or NULL");
			return null;
		}

		// Isolating tags as necessary and extracting the records for the user.
		Elements recordType = fetchedDocument.getElementsByTag("header");
		Elements recordValues = fetchedDocument.getElementsByClass("cell-centered");

		HashMap<String, List<String>> records = new HashMap<String, List<String>>();
		List<String> names = new ArrayList<String>();
		List<String> vals = new ArrayList<String>();

		for(int i = 1; i < recordType.size() - 2; i++) {
			names.add(recordType.get(i).text());
			vals.add(recordValues.get((i-1) * 2 + 1).text());
		}

		records.put("RECORD_NAMES", names);
		records.put("RECORD_VALUES", vals);

		DownloadResult myResult = new DownloadResult.DownloadResultBuilder().failure(false).redirected(false).
				setResultType(RESULT_TYPE.RESULT_TYPE_RECORDS).setRecordList(records).build();

		return myResult;
	}

	/**
	 * Extracts all the users Records.
	 * @param fetchedDocument
	 * @return
	 */
	public static DownloadResult getRecords(Document fetchedDocument) {
		return getRecordsLogic(fetchedDocument);
	}


	/**
	 * Extracts all the heroes the User has played with and their stats
	 * with that hero from the hero table.
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult getHeroesLogic(Document fetchedDocument) {

		if(fetchedDocument == null || !fetchedDocument.hasText()) {
			System.out.println("The Document passed to getRecords is Empty or NULL");
			return null;
		}

		Elements imageLinks = fetchedDocument.getElementsByClass("image-hero");
		Element data = fetchedDocument.select("table").first();
		Elements rows = data.select("tr");

		List<HashMap<String, String>> returnData = new ArrayList<HashMap<String,String>>();

		HashMap<String, String> heroData;

		for(int i=1; i < rows.size(); i++) {
			heroData = new HashMap<String, String>();
			heroData.put("IMAGE_LINK", imageLinks.get(i-1).attr("src"));
			heroData.put("HERO_NAME", rows.get(i).children().get(1).text());
			heroData.put("NUM_MATCHES", rows.get(i).children().get(2).text());
			heroData.put("WINRATE", rows.get(i).children().get(3).text());
			heroData.put("KDA", rows.get(i).children().get(4).text());
			returnData.add(heroData);
		}

		DownloadResult myResult = new DownloadResult.DownloadResultBuilder().failure(false).redirected(false).
				setResultType(RESULT_TYPE.RESULT_TYPE_HEROES).setHeroesData(returnData).build();

		return myResult;
	}

	/**
	 * Extracts all the heroes the user has played and their stats with those heroes.
	 * @param fetchedDocument
	 * @return
	 */
	public static DownloadResult getHeroes(Document fetchedDocument) {
		return getHeroesLogic(fetchedDocument);
	}

	/**
	 * Logic for the Player Info extraction.
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult getInfoLogic(Document fetchedDocument) {
		
		if(fetchedDocument == null || !fetchedDocument.hasText()) {
			System.out.println("The Document passed to getInfo is Empty or NULL");
			return null;
		}

		HashMap<String, String> info = new HashMap<String, String>();

		info.put("RECORD", "Record: " + fetchedDocument.getElementsByClass("won").get(0).text() + " - " + 
					fetchedDocument.getElementsByClass("lost").get(0).text());

		info.put("WINRATE", "Win Rate: " + fetchedDocument.getElementsByTag("dd").get(2).text());

		info.put("PROFILENAME", fetchedDocument.getElementsByClass("content-header-title").text());

		info.put("PROFILEIMAGE", fetchedDocument.getElementsByTag("img").get(0).attr("src"));

		DownloadResult myResult = new DownloadResult.DownloadResultBuilder().failure(false).redirected(false).
				setResultType(RESULT_TYPE.RESULT_TYPE_USERINFO).setUserInfo(info).build();

		return myResult;

	}

	/**
	 * Extracts the users basic info for the user info activity.
	 * @param fetchedDocument
	 * @return
	 */
	public static DownloadResult getInfo(Document fetchedDocument) {
		return getInfoLogic(fetchedDocument);
	}
}
