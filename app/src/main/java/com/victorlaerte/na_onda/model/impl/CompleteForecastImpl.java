package com.victorlaerte.na_onda.model.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.model.CompleteForecast;
import com.victorlaerte.na_onda.model.DayForecast;
import com.victorlaerte.na_onda.util.CharPool;
import com.victorlaerte.na_onda.util.Constants;
import com.victorlaerte.na_onda.util.ExtensionUtil;
import com.victorlaerte.na_onda.util.Validator;

public class CompleteForecastImpl implements CompleteForecast {

	private Date lastUpdate;

	private List<DayForecast> dayForecastList;

	private City city;

	public CompleteForecastImpl(City city) {

		this.city = city;
	}

	@Override
	public void parseWebServiceResponse(JSONObject jsonObject) throws JSONException, ParseException {

		dayForecastList = new ArrayList<DayForecast>();

		JSONObject cityJson = jsonObject.getJSONObject(CITY_KEY);

		/*
		 * "atualizacao":"2015-05-13"
		 */
		String lastUpdateString = cityJson.getString(LAST_UPDATE_KEY);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		lastUpdate = df.parse(lastUpdateString);

		JSONArray forecastJsonArray = cityJson.getJSONArray(FORECAST_KEY);

		Calendar lastDayParsed = null;

		/*
		 * Dia e hora referente aos dados da previsão oceânica, no formato dd-mm-aaaa 00|03|06|09|12|15|18|21h Z
		 */
		DateFormat forecastDf = new SimpleDateFormat("dd-MM-yyyy HH'h' 'Z'");

		DayForecast dayForecast = null;

		int dayCount = 0;

		for (int i = 0; i < forecastJsonArray.length(); i++) {

			JSONObject jsonForecast = forecastJsonArray.getJSONObject(i);

			String forecastDayString = jsonForecast.getString(DAY_KEY);
			Date currentTimeDate = forecastDf.parse(forecastDayString);
			Calendar currentDay = Calendar.getInstance();
			currentDay.setTime(currentTimeDate);

			if (Validator.isNotNull(lastDayParsed)) {

				boolean sameDay = DateUtils.isSameDay(currentDay, lastDayParsed);

				if (sameDay) {

					dayForecast.addForecast(jsonForecast, currentDay.get(Calendar.HOUR_OF_DAY));

				} else {

					/*
					 * add the last forecast by day
					 */
					dayForecastList.add(dayForecast);

					/*
					 * start a forecast for a new day
					 */

					dayForecast = new DayForecastImpl(currentDay, getUrlListByDay(dayCount));
					dayForecast.addForecast(jsonForecast, currentDay.get(Calendar.HOUR_OF_DAY));
					lastDayParsed = currentDay;

					dayCount++;
				}

			} else {

				dayForecast = new DayForecastImpl(currentDay, getUrlListByDay(dayCount));
				dayForecast.addForecast(jsonForecast, currentDay.get(Calendar.HOUR_OF_DAY));
				lastDayParsed = currentDay;

				dayCount++;
			}
		}

	}

	@Override
	public List<DayForecast> getForecastByDay() {

		return dayForecastList;
	}

	@Override
	public Date getLastUpdate() {

		return lastUpdate;
	}

	@Override
	public City getCity() {

		return city;
	}

	protected CompleteForecastImpl(Parcel in) {

		long tmpLastUpdate = in.readLong();

		lastUpdate = tmpLastUpdate != -1 ? new Date(tmpLastUpdate) : null;

		if (in.readByte() == 0x01) {
			dayForecastList = new ArrayList<DayForecast>();
			in.readList(dayForecastList, DayForecast.class.getClassLoader());
		} else {
			dayForecastList = null;
		}
		city = (City) in.readValue(City.class.getClassLoader());
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(lastUpdate != null ? lastUpdate.getTime() : -1L);

		if (dayForecastList == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(dayForecastList);
		}
		dest.writeValue(city);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<CompleteForecastImpl> CREATOR = new Parcelable.Creator<CompleteForecastImpl>() {

		@Override
		public CompleteForecastImpl createFromParcel(Parcel in) {

			return new CompleteForecastImpl(in);
		}

		@Override
		public CompleteForecastImpl[] newArray(int size) {

			return new CompleteForecastImpl[size];
		}
	};

	private List<String> getUrlListByDay(int dayIndex) {

		/*
		 * index 0 = 01,02,03,04
		 * index 1 = 05,06,07,08
		 * index 2 = 09,10,11,12
		 * index 3 = 13,14,15,16
		 * index 4 = 17,18,19,20
		 */

		List<String> urlList = new ArrayList<String>();

		int initalPhotoIndex = 1;

		if (dayIndex == 0) {
			initalPhotoIndex = 1;
		} else if (dayIndex == 1) {
			initalPhotoIndex = 5;
		} else if (dayIndex == 2) {
			initalPhotoIndex = 9;
		} else if (dayIndex == 3) {
			initalPhotoIndex = 13;
		} else if (dayIndex == 4) {
			initalPhotoIndex = 17;
		}

		for (int i = 0; i < 3; i++) {

			StringBuilder sb = new StringBuilder(Constants.INPE_GRAPH_BASE_URL);
			sb.append(city.getRegionAlias());
			sb.append(CharPool.FORWARD_SLASH);
			sb.append(Constants.GRAPH_HEIGHT_TYPE_KEY);
			sb.append(CharPool.FORWARD_SLASH);
			sb.append(city.getRegionAlias());
			sb.append("_alt_");

			initalPhotoIndex += i;
			String indexImg = "0" + initalPhotoIndex;

			sb.append(indexImg);
			sb.append(ExtensionUtil.PNG);

			urlList.add(sb.toString());
		}

		return urlList;
	}
}
