package com.victorlaerte.na_onda.model.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.victorlaerte.na_onda.model.DayForecast;
import com.victorlaerte.na_onda.model.Forecast;

public class DayForecastImpl implements DayForecast {

	private Calendar day;

	private List<Forecast> forecastList;

	private List<String> graphUrlList;

	public DayForecastImpl(Calendar day, List<String> graphUrlList) {

		this.day = day;
		this.graphUrlList = graphUrlList;
		forecastList = new ArrayList<Forecast>();
	}

	@Override
	public void addForecast(JSONObject jsonObject, int hour) throws JSONException {

		Forecast forecast = new ForecastImpl(day, hour);
		forecast.parseForecast(jsonObject);

		forecastList.add(forecast);
	}

	@Override
	public Calendar getDay() {

		return day;
	}

	@Override
	public List<String> getGraphUrlList() {

		return graphUrlList;
	}

	protected DayForecastImpl(Parcel in) {

		day = (Calendar) in.readValue(Calendar.class.getClassLoader());
		if (in.readByte() == 0x01) {
			forecastList = new ArrayList<Forecast>();
			in.readList(forecastList, Forecast.class.getClassLoader());
		} else {
			forecastList = null;
		}
		if (in.readByte() == 0x03) {
			graphUrlList = new ArrayList<String>();
			in.readList(forecastList, String.class.getClassLoader());
		} else {
			graphUrlList = null;
		}
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeValue(day);
		if (forecastList == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(forecastList);
		}
		if (graphUrlList == null) {
			dest.writeByte((byte) (0x02));
		} else {
			dest.writeByte((byte) (0x03));
			dest.writeList(graphUrlList);
		}
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<DayForecastImpl> CREATOR = new Parcelable.Creator<DayForecastImpl>() {

		@Override
		public DayForecastImpl createFromParcel(Parcel in) {

			return new DayForecastImpl(in);
		}

		@Override
		public DayForecastImpl[] newArray(int size) {

			return new DayForecastImpl[size];
		}
	};

	@Override
	public List<Forecast> getForecast() {

		return forecastList;
	}
}
