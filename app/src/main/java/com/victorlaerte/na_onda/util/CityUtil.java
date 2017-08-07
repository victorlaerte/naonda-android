package com.victorlaerte.na_onda.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.model.impl.CityImpl;

public class CityUtil {

	private static final String CITIES_KEY = "cidades";
	public static final String NAME_KEY = "nome";
	public static final String UF_KEY = "uf";
	public static final String ID_KEY = "id";
	public static final String REGION_KEY = "region";
	public static final String HASH_KEY = "key";
	private static final String LOG_TAG = CityUtil.class.getName();

	public static Map<String, City> findByResourceName(Context context, String resourceName) {

		Map<String, City> cityMap = new TreeMap<String, City>();

		InputStream is = null;

		try {

			is = context.getAssets().open(resourceName);

			String citiesFile = IOUtils.toString(is, CharsetUtil.DEFAULT_CHARSET);

			JSONObject jsonObj = new JSONObject(citiesFile);

			JSONArray jsonArray = jsonObj.getJSONArray(CITIES_KEY);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonCity = jsonArray.getJSONObject(i);
				String name = jsonCity.getString(NAME_KEY);
				String uf = jsonCity.getString(UF_KEY);
				String id = jsonCity.getString(ID_KEY);
				String region = jsonCity.getString(REGION_KEY);

				City city = new CityImpl(id, name, uf, resourceName, region);

				cityMap.put(city.getKey(), city);
			}

		} catch (Exception e) {

			Log.e(LOG_TAG, e.getMessage());

		} finally {

			IOUtils.closeQuietly(is);
		}

		return cityMap;
	}

	public static City findByResourceNameAndId(Context context, String resourceName, String cityKey) {

		Map<String, City> cityList = findByResourceName(context, resourceName);

		City city = null;

		if (cityList.containsKey(cityKey)) {

			city = cityList.get(cityKey);
		}

		return city;
	}

	public static List<City> getFavoriteCities(Context context, Set<String> favCitiesId) {

		List<City> cityList = new ArrayList<City>();

		for (String favCityId : favCitiesId) {

			String[] str = favCityId.split(";");

			String resourceName = str[0];
			String cityKey = str[1];

			City city = findByResourceNameAndId(context, resourceName, cityKey);

			if (Validator.isNotNull(city)) {
				cityList.add(city);
			}
		}

		return cityList;
	}
}
