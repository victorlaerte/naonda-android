package com.victorlaerte.na_onda.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

public class AndroidUtil {

	public static String getColor(Context context, int colorId) {

		return context.getString(colorId);
	}

	public static String getString(Context context, int stringId) {

		return context.getString(stringId);
	}

	public static String getString(Context context, int... stringId) {

		return getString(context, StringPool.SPACE, stringId);
	}

	public static String getString(Context context, String separator, int... stringId) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < stringId.length; i++) {

			if (i > 0) {
				sb.append(separator);
			}

			sb.append(context.getString(stringId[i]));
		}

		return sb.toString();
	}

	public static void keepScreenOn(WakeLock wakeLock) {

		if (wakeLock != null && wakeLock.isHeld() == false) {
			wakeLock.acquire();
		}
	}

	public static void turnScreenOff(WakeLock wakeLock) {

		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
		}
	}

	public static boolean isNetworkAvaliable(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}

		return false;
	}

	public static int convertDpToPixel(float dp, Context context) {

		// Get the screen's density scale
		final float scale = context.getResources().getDisplayMetrics().density;

		// Convert the dps to pixels, based on density scale
		int px = (int) (dp * scale + 0.5f);

		return px;
	}

	public static float convertPixelsToDp(float px, Context context) {

		Resources resources = context.getResources();

		DisplayMetrics metrics = resources.getDisplayMetrics();

		float dp = px / (metrics.densityDpi / 160f);

		return dp;
	}

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static void createNoMediaFile(Context applicationContext, String noMediaPath) {

		noMediaPath = noMediaPath + File.separator + ".nomedia";

		File noMediaFile = new File(noMediaPath);

		if (!noMediaFile.exists()) {

			try {

				String dirTreePath = noMediaFile.getParent();
				File dirTree = new File(dirTreePath);

				if (!dirTree.exists()) {
					dirTree.mkdirs();
				}

				noMediaFile.createNewFile();

				applicationContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
						.parse("file://" + Environment.getExternalStorageDirectory())));

			} catch (Exception e) {

				Logger.getLogger(DialogsUtil.class.getName()).log(Level.SEVERE, e.getMessage());
			}

		}
	}
}
