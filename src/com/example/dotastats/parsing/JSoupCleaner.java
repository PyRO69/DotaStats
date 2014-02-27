package com.example.dotastats.parsing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.StrictMode;

import com.example.dotastats.helperclasses.DownloadResult;

public class JSoupCleaner {

	private static final String search_URL = "http://dotabuff.com/search?q=";
	private static Response response;

	private static final int RESPONSE_SUCCESS = 1;
	private static final int RESPONSE_REDIRECT = 2;
	private static final int RESPONSE_FAILURE = 4;
	
	/**
	 * Returns the Downloaded Document from the
	 * URL and User name provided.
	 * @param UserName
	 * @return
	 */
	private static int downloadDocument(String link) {

		StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(threadPolicy);

		if(link == null || link.isEmpty()) {
			System.out.println("The Link provided to downloadDocument is Empty or NULL !");
			return RESPONSE_FAILURE;
		}

		try {
			response = Jsoup.connect(link).followRedirects(false).execute();
			if(response.statusCode() == HttpURLConnection.HTTP_MOVED_PERM || 
					response.statusCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				return RESPONSE_REDIRECT;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Downloading the Document Failed !");
			return RESPONSE_FAILURE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in trying to download Page !");
			return RESPONSE_FAILURE;
		}

		return RESPONSE_SUCCESS;
	}
	
	/**
	 * Returns a map of Player names as Key and their player number to search
	 * as Value.
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult stripNames(Document fetchedDocument) {
		
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

		DownloadResult myResult = new DownloadResult();
		myResult.setFailure(false);
		myResult.setRedirected(false);
		myResult.setResultType(DownloadResult.RESULT_TYPE_NAMELIST);
		myResult.setNameList(namesAndPages);

		return myResult;
	}

	/**
	 * Returns the stripped Player Info.
	 * @param link
	 * @return
	 */
	public static DownloadResult getNameList(String UserName) {
		
		if(UserName == null || UserName.isEmpty()) {
			System.out.println("The UserName provided to getNameList is Empty or NULL !");
			return null;
		}

		int status = downloadDocument(search_URL + UserName);

		if(status == RESPONSE_SUCCESS) {

			try {
				return stripNames(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Parsing the Document Failed.");
			}

		} 

		return sendResponse(status, DownloadResult.RESULT_TYPE_NAMELIST);

	}

	private static DownloadResult sendResponse(int status, int resultType) {
		DownloadResult myResult =  new DownloadResult();
		myResult.setResultType(resultType);
		if(status == RESPONSE_REDIRECT) {
			myResult.setRedirected(true);
			myResult.setRedirectLink(response.header("location"));
		} else {
			myResult.setFailure(true);
		}
		return myResult;
	}

	/**
	 * Extracts the Player Info to populate the Info Tab activity
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult getInfo(Document fetchedDocument) {
		
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

		DownloadResult myResult = new DownloadResult();
		myResult.setFailure(false);
		myResult.setRedirected(false);
		myResult.setResultType(DownloadResult.RESULT_TYPE_USERINFO);
		myResult.setUserInfo(info);

		return myResult;

	}
	
	/**
	 * Returns the stripped Player names and their pages.
	 * @param UserName
	 * @return
	 */
	public static DownloadResult getUserInfo(String link) {

		if(link == null || link.isEmpty()) {
			System.out.println("The Document passed to getInfo is Empty or NULL");
			return null;
		}

		int status = downloadDocument(link);

		if(status != RESPONSE_FAILURE) {

			try {
				return getInfo(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Parsing the Document Failed.");
			}
		} 

		return sendResponse(status, DownloadResult.RESULT_TYPE_USERINFO);

	}

	/**
	 * Returns the Match ID , Hero Names and Hero Image URL
	 * @param link
	 * @return
	 */
	public static DownloadResult getMatchListAndInfo(String link) {

		if(link == null || link.isEmpty()) {
			System.out.println("The Document passed to getInfo is Empty or NULL");
			return null;
		}

		int status = downloadDocument(link);

		if(status != RESPONSE_FAILURE) {

			try {
				return getMatchLists(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Parsing the Document Failed.");
			}
		}

		return sendResponse(status, DownloadResult.RESULT_TYPE_MATCHLIST);
	}

	/**
	 * Extracts out the Image URLs, Hero Names and Match IDs.
	 * @param fetchedDocument
	 * @return
	 */
	private static DownloadResult getMatchLists(Document fetchedDocument) {

		int listSize = 0;

		if(fetchedDocument == null || !fetchedDocument.hasText()) {
			System.out.println("The Document passed to getInfo is Empty or NULL");
			return null;
		}

		HashMap<String, List<String>>  matchLists =  new HashMap<String, List<String>>();
		Elements images = fetchedDocument.getElementsByClass("image-hero");
		Elements otherData =  fetchedDocument.getElementsByClass("matchid");
		if(images.size() == otherData.size()) {
			if(images.size() >= 20) {
				listSize = 20;
			} else {
				listSize = images.size();
			}
		}

		String[] imgs = new String[20];
		String[] heronames = new String[20];
		String[] matchid = new String[20];

		for(int i=0; i < listSize; i++) {
			imgs[i] = images.get(i).attr("src");
			heronames[i] = images.get(i).attr("alt");
			matchid[i] = otherData.get(i).text();
		}

		matchLists.put("HEROIMAGES", Arrays.asList(imgs));
		matchLists.put("HERONAMES", Arrays.asList(heronames));
		matchLists.put("MATCHIDS", Arrays.asList(matchid));

		DownloadResult myResult = new DownloadResult();
		myResult.setFailure(false);
		myResult.setRedirected(false);
		myResult.setResultType(DownloadResult.RESULT_TYPE_MATCHLIST);
		myResult.setMatchList(matchLists);

		return myResult;
	}

	public static DownloadResult getUserRecords(String link) {
		if(link == null || link.isEmpty()) {
			System.out.println("Empty Link passed to get User Records");
			return null;
		}

		int status = downloadDocument(link); 

		if(status != RESPONSE_FAILURE) {
			try {
				return getRecords(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Get Users Failed.");
			}
		}

		return sendResponse(status, DownloadResult.RESULT_TYPE_RECORDS);
	}

	private static DownloadResult getRecords(Document fetchedDocument) {

		if(fetchedDocument == null || !fetchedDocument.hasText()) {
			System.out.println("The Document passed to getRecords is Empty or NULL");
			return null;
		}

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

		DownloadResult myResult = new DownloadResult();
		myResult.setFailure(false);
		myResult.setRedirected(false);
		myResult.setResultType(DownloadResult.RESULT_TYPE_RECORDS);
		myResult.setRecordList(records);

		return myResult;
	}

	public static DownloadResult getHerosPlayed(String link) {

		if(link == null || link.isEmpty()) {
			System.out.println("Empty Link passed to get User Records");
			return null;
		}
		System.out.println(link);

		int status = downloadDocument(link); 

		if(status != RESPONSE_FAILURE) {
			try {
				return getHeroes(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Get Users Failed.");
			}
		}

		return sendResponse(status, DownloadResult.RESULT_TYPE_HEROES);
	}

	private static DownloadResult getHeroes(Document fetchedDocument) {

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

		System.out.println("HEROES LIST SIZE = " + rows.size());

		DownloadResult myResult = new DownloadResult();
		myResult.setFailure(false);
		myResult.setRedirected(false);
		myResult.setResultType(DownloadResult.RESULT_TYPE_HEROES);
		myResult.setHeroesData(returnData);

		return myResult;
	}
}
