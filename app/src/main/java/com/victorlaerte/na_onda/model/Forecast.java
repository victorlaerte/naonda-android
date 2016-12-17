package com.victorlaerte.na_onda.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcelable;

public interface Forecast extends Parcelable {

	public static final String DAY_KEY = "dia";
	public static final String WIND_SPEED_KEY = "vento";
	public static final String WIND_DIRECTION_KEY = "vento_dir";
	public static final String WAVE_HEIGHT_KEY = "altura";
	public static final String WAVE_DIRECTION_KEY = "direcao";
	public static final String UNREST = "agitacao";

	public void parseForecast(JSONObject jsonObject) throws JSONException;

	public double getWaveHeight();

	public CardinalPoints getWaveDirection();

	public double getWindSpeed();

	public CardinalPoints getWindDirection();

	public String getUnrest();

	public int getHour();

	public String getSharebleForecast(Context context, String label);
}
