package com.victorlaerte.na_onda.tasks;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.events.ForecastLoadEvent;
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

	WeakReference<Context> wContext;
	ProgressDialog dialog;
	City city;
	String msgError = StringPool.BLANK;
	CompleteForecast completeForecast;
	private static final String LOG_TAG = ForecastTask.class.getName();

	public ForecastTask(Context context, City city) {

		wContext = new WeakReference<Context>(context);
		this.city = city;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		Context context = wContext.get();

		if (Validator.isNotNull(context)) {

			dialog = new ProgressDialog(context);
			dialog.setTitle(R.string.wait);
			dialog.setMessage(AndroidUtil.getString(context, R.string.getting_forecast));
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	@Override
	protected Boolean doInBackground(String... params) {

		String baseUrl = params[0];
		String suffixUrl = params[1];

		Context context = wContext.get();

		if (Validator.isNotNull(context)) {

			if (AndroidUtil.isNetworkAvaliable(context)) {

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
					msgError = AndroidUtil.getString(context, R.string.error_unknow_contact_admin_with_msg) + msgError;
				}

			} else {

				msgError = AndroidUtil.getString(context, R.string.error_no_internet_avaliable);
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

		Context context = wContext.get();

		if (Validator.isNotNull(context)) {

			if (result) {

				EventBus.getDefault().post(new ForecastLoadEvent(completeForecast));

			} else {

				DialogsUtil.showDialog(context, R.string.error, msgError, null);
			}
		}
	}
}
