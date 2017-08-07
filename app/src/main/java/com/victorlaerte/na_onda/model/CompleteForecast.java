package com.victorlaerte.na_onda.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcelable;

public interface CompleteForecast extends Parcelable {

	String ID = "completeForecast";
	String CITY_KEY = "cidade";
	String LAST_UPDATE_KEY = "atualizacao";
	String FORECAST_KEY = "previsao";
	String DAY_KEY = "dia";

	void parseWebServiceResponse(JSONObject jsonObject) throws JSONException, ParseException;

	List<DayForecast> getForecastByDay();

	Date getLastUpdate();

	City getCity();

}
