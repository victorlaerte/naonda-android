package com.victorlaerte.na_onda.model.impl;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.model.CardinalPoints;
import com.victorlaerte.na_onda.model.Forecast;
import com.victorlaerte.na_onda.util.AndroidUtil;
import com.victorlaerte.na_onda.util.CharPool;
import com.victorlaerte.na_onda.util.StringPool;

public class ForecastImpl implements Forecast {

	private Calendar day;
	private int hour;

	/*
	 * Altura em metros das ondas
	 */
	private double waveHeight;

	/*
	 * Sigla do ponto cardeal, colateral ou subcolateral que indica a direção das ondas
	 */
	private CardinalPoints waveDirection;

	/*
	 * Velocidade do vento em km/h
	 */
	private double windSpeed;

	/*
	 * Sigla do ponto cardeal, colateral ou subcolateral que indica a direção do vento
	 */
	private CardinalPoints windDirection;

	/*
	 * Texto que exibe a agitação do mar no determinado período do dia. Pode ser Fraco, Moderado ou Forte.
	 */
	private String unrest;

	public ForecastImpl(Calendar day, int hour) {

		this.day = day;
		this.hour = hour;
	}

	@Override
	public void parseForecast(JSONObject jsonObject) throws JSONException {

		waveHeight = jsonObject.getDouble(WAVE_HEIGHT_KEY);
		waveDirection = getCardinalPointByString(jsonObject.getString(WAVE_DIRECTION_KEY));
		windSpeed = jsonObject.getDouble(WIND_SPEED_KEY);
		windDirection = getCardinalPointByString(jsonObject.getString(WIND_DIRECTION_KEY));
		unrest = jsonObject.getString(UNREST);
	}

	@Override
	public double getWaveHeight() {

		return waveHeight;
	}

	@Override
	public CardinalPoints getWaveDirection() {

		return waveDirection;
	}

	@Override
	public double getWindSpeed() {

		return windSpeed;
	}

	@Override
	public CardinalPoints getWindDirection() {

		return windDirection;
	}

	@Override
	public String getUnrest() {

		return unrest;
	}

	@Override
	public int getHour() {

		return hour;
	}

	protected ForecastImpl(Parcel in) {

		day = (Calendar) in.readValue(Calendar.class.getClassLoader());
		hour = in.readInt();
		waveHeight = in.readDouble();
		waveDirection = (CardinalPoints) in.readValue(CardinalPoints.class.getClassLoader());
		windSpeed = in.readDouble();
		windDirection = (CardinalPoints) in.readValue(CardinalPoints.class.getClassLoader());
		unrest = in.readString();
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeValue(day);
		dest.writeInt(hour);
		dest.writeDouble(waveHeight);
		dest.writeValue(waveDirection);
		dest.writeDouble(windSpeed);
		dest.writeValue(windDirection);
		dest.writeString(unrest);
	}

	private CardinalPoints getCardinalPointByString(String cardinalStr) {

		switch (cardinalStr) {

		case "N":
			return CardinalPoints.NORTH;
		case "S":
			return CardinalPoints.SOUTH;
		case "E":
			return CardinalPoints.EAST;
		case "W":
			return CardinalPoints.WEST;
		case "NE":
			return CardinalPoints.NORTHEAST;
		case "SE":
			return CardinalPoints.SOUTHEAST;
		case "SW":
			return CardinalPoints.SOUTHWEST;
		case "NW":
			return CardinalPoints.NORTHWEST;
		case "NNE":
			return CardinalPoints.NORTH_NORTHEAST;
		case "ENE":
			return CardinalPoints.EAST_NORTHEAST;
		case "ESE":
			return CardinalPoints.EAST_SOUTHEAST;
		case "SSE":
			return CardinalPoints.SOUTH_SOUTHEAST;
		case "SSW":
			return CardinalPoints.SOUTH_SOUTHWEST;
		case "WSW":
			return CardinalPoints.WEST_SOUTHWEST;
		case "WNW":
			return CardinalPoints.WEST_NORTHWEST;
		case "NNW":
			return CardinalPoints.NORTH_NORTHWEST;

		default:
			return CardinalPoints.NORTH;
		}

	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ForecastImpl> CREATOR = new Parcelable.Creator<ForecastImpl>() {

		@Override
		public ForecastImpl createFromParcel(Parcel in) {

			return new ForecastImpl(in);
		}

		@Override
		public ForecastImpl[] newArray(int size) {

			return new ForecastImpl[size];
		}
	};

	@Override
	public String getSharebleForecast(Context context, String label) {

		StringBuilder sb = new StringBuilder(label);

		sb.append(CharPool.COLON);
		sb.append(CharPool.SPACE);
		sb.append(AndroidUtil.getString(context, R.string.waveHeight));
		sb.append(CharPool.SPACE);
		sb.append(String.valueOf(waveHeight));
		sb.append(CharPool.LOWER_CASE_M);
		sb.append(StringPool.DASH_WITH_SPACE);
		sb.append(AndroidUtil.getString(context, R.string.waveDirection));
		sb.append(CharPool.SPACE);
		sb.append(waveDirection.getAcronym());
		sb.append(StringPool.DASH_WITH_SPACE);
		sb.append(AndroidUtil.getString(context, R.string.unrest));
		sb.append(CharPool.SPACE);
		sb.append(unrest);
		sb.append(StringPool.DASH_WITH_SPACE);
		sb.append(AndroidUtil.getString(context, R.string.windSpeed));
		sb.append(CharPool.SPACE);
		sb.append(String.valueOf(windSpeed));
		sb.append(CharPool.LOWER_CASE_K);
		sb.append(CharPool.LOWER_CASE_M);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(CharPool.LOWER_CASE_H);
		sb.append(StringPool.DASH_WITH_SPACE);
		sb.append(AndroidUtil.getString(context, R.string.windDirection));
		sb.append(CharPool.SPACE);
		sb.append(windDirection.getAcronym());

		return sb.toString();
	}
}
