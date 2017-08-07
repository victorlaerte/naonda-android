package com.victorlaerte.na_onda.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcelable;

public interface Forecast extends Parcelable {

	String DAY_KEY = "dia";
	String WIND_SPEED_KEY = "vento";
	String WIND_DIRECTION_KEY = "vento_dir";
	String WAVE_HEIGHT_KEY = "altura";
	String WAVE_DIRECTION_KEY = "direcao";
	String UNREST = "agitacao";

	void parseForecast(JSONObject jsonObject) throws JSONException;

	double getWaveHeight();

	CardinalPoints getWaveDirection();

	double getWindSpeed();

	CardinalPoints getWindDirection();

	String getUnrest();

	int getHour();

	String getSharebleForecast(Context context, String label);
}
