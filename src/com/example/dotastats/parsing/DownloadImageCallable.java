package com.example.dotastats.parsing;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * Callable to implement the Image download functionality on a separate thread.
 * This ensures we can hand over the download task to a background thread and then
 * receive the image once it is downloaded. Ensures the main UI thread is not burdened.
 * 
 * @author swaroop
 */
public class DownloadImageCallable implements Callable<Bitmap> {

	private final String downloadLink;

	public DownloadImageCallable(String link) {
		this.downloadLink = link;
	}

	@Override
	public Bitmap call() {

		Bitmap img = null;
		try {

			HttpURLConnection myConnection = (HttpURLConnection) new URL(downloadLink).openConnection();
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
