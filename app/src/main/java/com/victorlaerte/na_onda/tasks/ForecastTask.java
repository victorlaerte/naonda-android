package com.victorlaerte.na_onda.tasks;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.model.CompleteForecast;
import com.victorlaerte.na_onda.model.impl.CompleteForecastImpl;
import com.victorlaerte.na_onda.util.AndroidUtil;
import com.victorlaerte.na_onda.util.DialogsUtil;
import com.victorlaerte.na_onda.util.StringPool;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.util.WebServiceData;
import com.victorlaerte.na_onda.view.activities.MainViewActivity;
import com.victorlaerte.na_onda.view.fragments.ForecastFragment;
import com.victorlaerte.na_onda.view.fragments.SelectionFragment;

public class ForecastTask extends AsyncTask<String, Integer, Boolean> {

	WeakReference<MainViewActivity> wActivity;
	ProgressDialog dialog;
	City city;
	String msgError = StringPool.BLANK;
	CompleteForecast completeForecast;
	private static final String LOG_TAG = ForecastTask.class.getName();

	public ForecastTask(MainViewActivity activity, City city) {

		wActivity = new WeakReference<MainViewActivity>(activity);
		this.city = city;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		MainViewActivity activity = wActivity.get();

		if (Validator.isNotNull(activity)) {

			dialog = new ProgressDialog(activity);
			dialog.setTitle(R.string.wait);
			dialog.setMessage(AndroidUtil.getString(activity, R.string.getting_forecast));
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	@Override
	protected Boolean doInBackground(String... params) {

		String baseUrl = params[0];
		String suffixUrl = params[1];

		MainViewActivity activity = wActivity.get();

		if (Validator.isNotNull(activity)) {

			if (AndroidUtil.isNetworkAvaliable(activity)) {

				try {

					String readStringResponse = WebServiceData.readStringResponse(baseUrl + city.getId() + suffixUrl);

					JSONObject jsonObj = XML.toJSONObject(readStringResponse);

					Log.d(LOG_TAG, jsonObj.toString());

					completeForecast = new CompleteForecastImpl(city);

					completeForecast.parseWebServiceResponse(jsonObj);

					return true;

				} catch (IOException | ParserConfigurationException | SAXException | JSONException | ParseException e) {

					msgError = e.getMessage();
					Log.e(LOG_TAG, msgError);
					msgError = AndroidUtil.getString(activity, R.string.error_unknow_contact_admin_with_msg) + msgError;
				}

			} else {

				msgError = AndroidUtil.getString(activity, R.string.error_no_internet_avaliable);
			}
		}

		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		super.onPostExecute(result);

		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		MainViewActivity activity = wActivity.get();

		if (Validator.isNotNull(activity)) {

			if (result) {

				/*
				 * XXX
				 * Bad Pratice, avoid performing transactions inside asynchronous callback methods
				 */
				ForecastFragment forecastFragment = new ForecastFragment();

				Bundle args = new Bundle();

				args.putParcelable(CompleteForecast.ID, completeForecast);

				forecastFragment.setArguments(args);

				if (!activity.isFinishing()) {

					FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();

					transaction.replace(R.id.content_frame, forecastFragment, forecastFragment.getClass().getName());
					transaction.addToBackStack(SelectionFragment.class.getName());
					transaction.commitAllowingStateLoss();
				}

			} else {

				DialogsUtil.showDialog(activity, R.string.error, msgError, null);
			}
		}
	}
}
