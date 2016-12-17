package com.victorlaerte.na_onda.tasks;

import java.lang.ref.WeakReference;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.util.AndroidUtil;
import com.victorlaerte.na_onda.util.DialogsUtil;
import com.victorlaerte.na_onda.util.StringPool;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.view.activities.LoginActivity;
import com.victorlaerte.na_onda.view.activities.SplashScreenActivity;

public class LoadAppComponentsTask extends AsyncTask<String, Integer, Boolean> {

	private WeakReference<SplashScreenActivity> weakActivity;
	private String errorMsg = StringPool.BLANK;

	public LoadAppComponentsTask(SplashScreenActivity activity) {

		weakActivity = new WeakReference<SplashScreenActivity>(activity);
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {

		SplashScreenActivity strongActivity = weakActivity.get();

		if (Validator.isNotNull(strongActivity)) {

			if (!AndroidUtil.isNetworkAvaliable(strongActivity)) {

				errorMsg = AndroidUtil.getString(strongActivity, R.string.error_no_internet_avaliable);
				return false;
			}
		} else {

			errorMsg = AndroidUtil.getString(strongActivity, R.string.error_unknow_error);
			return false;
		}

		try {

			Thread.sleep(3000);

		} catch (InterruptedException e) {

			Log.e(getClass().getName(), e.getMessage());

			errorMsg = AndroidUtil.getString(strongActivity, R.string.error_unknow_error);
			return false;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		final SplashScreenActivity strongActivity = weakActivity.get();

		if (Validator.isNotNull(strongActivity)) {

			if (result) {

				Intent it = new Intent(strongActivity, LoginActivity.class);

				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				strongActivity.startActivity(it);

				strongActivity.overridePendingTransition(R.anim.fadeout, R.anim.fadein);

				strongActivity.finish();

			} else {

				DialogsUtil.showDialog(strongActivity, R.string.error, errorMsg, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						strongActivity.finish();
					}
				});
			}
		}

		super.onPostExecute(result);
	}
}
