package com.victorlaerte.na_onda.model;

import android.os.Parcelable;

public interface City extends Parcelable {

	String SHARED_PREFS_FAV_CITIES = "fav_cities";

	String SELECTED_CITY = "selectedCity";

	String getName();

	void setName(String name);

	String getUf();

	void setUf(String uf);

	String getId();

	String getRegionAlias();

	String getKey();

	String getResourceName();

	String getSharedPreferencesId();

}
