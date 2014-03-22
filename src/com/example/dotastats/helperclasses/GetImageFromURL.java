package com.example.dotastats.helperclasses;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.graphics.Bitmap;

import com.example.dotastats.parsing.DownloadImageCallable;

/*
 * This class downloads an image given the URL. This is offloaded to a
 * thread so the main thread is free. This uses the DownloadImage callable
 * to return the image.
 * 
 * @author swaroop
 */
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
	}
}
