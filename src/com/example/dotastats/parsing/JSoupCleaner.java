package com.example.dotastats.parsing;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import android.os.StrictMode;

import com.example.dotastats.helperclasses.DownloadResult;
import com.example.dotastats.helperclasses.DownloadResult.DownloadResultBuilder;
import com.example.dotastats.helperclasses.DownloadResult.RESULT_TYPE;

/*
 * Cleaner Class to package all the extracted User information
 * into Download Result objects. Also includes method to download
 * the document.
 * 
 * @author swaroop
 */
public class JSoupCleaner {

	private static final String search_URL = "http://dotabuff.com/search?q=";
	private static Response response;

	// Using enum to keep track of response type.
	private enum RESPONSE {
		RESPONSE_SUCCESS,
		RESPONSE_REDIRECT,
		RESPONSE_FAILURE
	}

	private static final int TIMEOUT = 5000;// 5 second Wait for Download Timeout.
	
	/**
	 * Returns the Downloaded Document from the
	 * URL and User name provided.
	 * @param UserName
	 * @return
	 */
	private static RESPONSE downloadDocument(String link) {

		// This isnt running on the main thread. The parsing service that invokes this is running on a background
		// thread so this shouldnt affect the fluidity or UI thread.
		StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(threadPolicy);

		if(link == null || link.isEmpty()) {
			System.out.println("The Link provided to downloadDocument is Empty or NULL !");
			return RESPONSE.RESPONSE_FAILURE;
		}

		try {
			response = Jsoup.connect(link).followRedirects(false).timeout(TIMEOUT).execute();
			if(response != null) {
				if(response.statusCode() == HttpURLConnection.HTTP_MOVED_PERM || 
						response.statusCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
					return RESPONSE.RESPONSE_REDIRECT;
				} else {
					return RESPONSE.RESPONSE_SUCCESS;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Downloading the Document Failed !");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in trying to download Page !");
		}

		return RESPONSE.RESPONSE_FAILURE;
	}
	


	/**
	 * Returns the stripped Player Info as a DownloadResult.
	 * @param link
	 * @return
	 */
	public static DownloadResult getNameList(String UserName) {
		
		if(UserName == null || UserName.isEmpty()) {
			System.out.println("The UserName provided to getNameList is Empty or NULL !");
			return null;
		}

		RESPONSE status = downloadDocument(search_URL + UserName);

		if(status == RESPONSE.RESPONSE_SUCCESS) {

			try {
				return HtmlParser.stripNames(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Parsing the Document Failed.");
			}

		} 

		return sendResponse(status, RESULT_TYPE.RESULT_TYPE_NAMELIST);

	}

	/**
	 * Sends a Response if the result is a redirect or a Failure.
	 * If the result is a redirect, it also holds the redirect link.
	 * @param status
	 * @param resultType
	 * @return
	 */
	private static DownloadResult sendResponse(RESPONSE status, RESULT_TYPE resultType) {

		DownloadResultBuilder myResultBuilder =  new DownloadResult.DownloadResultBuilder().setResultType(resultType);
		if(status == RESPONSE.RESPONSE_REDIRECT) {
			myResultBuilder.redirected(true);
			myResultBuilder.setRedirectLink(response.header("location"));
		} else {
			myResultBuilder.failure(true);
		}
		return myResultBuilder.build();
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

		RESPONSE status = downloadDocument(link);

		if(status != RESPONSE.RESPONSE_FAILURE) {

			try {
				return HtmlParser.getInfo(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Parsing the Document Failed.");
			}
		} 

		return sendResponse(status, RESULT_TYPE.RESULT_TYPE_USERINFO);

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

		RESPONSE status = downloadDocument(link);

		if(status != RESPONSE.RESPONSE_FAILURE) {

			try {
				return HtmlParser.getMatchLists(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Parsing the Document Failed.");
			}
		}

		return sendResponse(status, RESULT_TYPE.RESULT_TYPE_MATCHLIST);
	}


	public static DownloadResult getUserRecords(String link) {
		if(link == null || link.isEmpty()) {
			System.out.println("Empty Link passed to get User Records");
			return null;
		}

		RESPONSE status = downloadDocument(link); 

		if(status != RESPONSE.RESPONSE_FAILURE) {
			try {
				return HtmlParser.getRecords(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Get User Records Failed.");
			}
		}

		return sendResponse(status, RESULT_TYPE.RESULT_TYPE_RECORDS);
	}


	public static DownloadResult getHerosPlayed(String link) {

		if(link == null || link.isEmpty()) {
			System.out.println("Empty Link passed to get User Records");
			return null;
		}
		System.out.println(link);

		RESPONSE status = downloadDocument(link); 

		if(status != RESPONSE.RESPONSE_FAILURE) {
			try {
				return HtmlParser.getHeroes(response.parse());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Get Users Failed.");
			}
		}

		return sendResponse(status, RESULT_TYPE.RESULT_TYPE_HEROES);
	}

}
