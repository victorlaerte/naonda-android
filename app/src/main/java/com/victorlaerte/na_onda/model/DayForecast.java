package com.victorlaerte.na_onda.model;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcelable;

public interface DayForecast extends Parcelable {

	void addForecast(JSONObject jsonObject, int hour) throws JSONException;

	Calendar getDay();

	List<String> getGraphUrlList();

	List<Forecast> getForecast();
}
