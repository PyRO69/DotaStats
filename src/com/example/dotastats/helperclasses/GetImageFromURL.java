package com.example.dotastats.helperclasses;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

public class GetImageFromURL {

	public static Bitmap getImageFromURL(String url) {

		if(url == null || url.isEmpty()) {
			System.out.println("URL passed to getImageFromURL is Null or Empty.");
			return null;
		}

		StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(threadPolicy);

		Bitmap img = null;
		try {

			HttpURLConnection myConnection = (HttpURLConnection) new URL(url).openConnection();
			myConnection.connect();
			InputStream input = myConnection.getInputStream();
			img = BitmapFactory.decodeStream(input);
			myConnection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("Malformed URL Exception while getting Image !");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO Exception while getting Image !");
		}

		return img;
	}
}
