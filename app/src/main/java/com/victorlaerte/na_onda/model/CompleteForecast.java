package com.victorlaerte.na_onda.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcelable;

public interface CompleteForecast extends Parcelable {

	public static final String ID = "completeForecast";
	public static final String CITY_KEY = "cidade";
	public static final String LAST_UPDATE_KEY = "atualizacao";
	public static final String FORECAST_KEY = "previsao";
	public static final String DAY_KEY = "dia";

	public void parseWebServiceResponse(JSONObject jsonObject) throws JSONException, ParseException;

	public List<DayForecast> getForecastByDay();

	public Date getLastUpdate();

	public City getCity();

}
