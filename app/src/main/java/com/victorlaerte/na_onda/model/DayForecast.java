package com.victorlaerte.na_onda.model;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcelable;

public interface DayForecast extends Parcelable {

	public void addForecast(JSONObject jsonObject, int hour) throws JSONException;

	public Calendar getDay();

	public List<String> getGraphUrlList();

	public List<Forecast> getForecast();
}
