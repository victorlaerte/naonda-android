package com.victorlaerte.na_onda.tasks;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.victorlaerte.na_onda.util.Constants;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.view.activities.MainViewActivity;

public class LoadFacebookProfilePhoto extends AsyncTask<String, Integer, Boolean> {

	private static final String LOG_TAG = LoadFacebookProfilePhoto.class.getName();
	private WeakReference<MainViewActivity> weakActivity;
	private Bitmap bitmap;

	public LoadFacebookProfilePhoto(MainViewActivity activity) {

		weakActivity = new WeakReference<MainViewActivity>(activity);
	}

	@Override
	protected Boolean doInBackground(String... params) {

		String userId = params[0];

		try {

			URL imageUrl = new URL(
					Constants.FACEBOOK_GRAPH_URL + userId + Constants.FACEBOOK_SMALL_PICTURE_URL_COMPLEMENT);

			bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());

			return true;

		} catch (IOException e) {

			Log.e(LOG_TAG, e.getMessage());

			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {

		super.onPostExecute(result);

		final MainViewActivity strongActivity = weakActivity.get();

		if (Validator.isNotNull(strongActivity)) {

			if (result) {

				strongActivity.updateProfile(bitmap);
			}
		}
	}
}
