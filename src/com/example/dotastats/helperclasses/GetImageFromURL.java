package com.example.dotastats.helperclasses;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.graphics.Bitmap;

import com.example.dotastats.parsing.DownloadImageCallable;

public class GetImageFromURL {

	private static ExecutorService myService = Executors.newFixedThreadPool(1);

	public static Bitmap getImageFromURL(String url) {

		if(url == null || url.isEmpty()) {
			System.out.println("URL passed to getImageFromURL is Null or Empty.");
			return null;
		}

		Future<Bitmap> imageDownload = myService.submit(new DownloadImageCallable(url));

		try {
			Bitmap myBitmap = imageDownload.get();
			return myBitmap;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return null;

		/*StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
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

		return img;*/
	}
}
