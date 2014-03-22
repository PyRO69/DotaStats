package com.example.dotastats.parsing;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownloadImageCallable implements Callable<Bitmap> {

	private String downloadLink;

	public DownloadImageCallable(String link) {
		downloadLink = link;
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
