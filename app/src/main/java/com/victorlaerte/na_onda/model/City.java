package com.victorlaerte.na_onda.model;

import android.os.Parcelable;

public interface City extends Parcelable {

	String SHARED_PREFS_FAV_CITIES = "fav_cities";

	String SELECTED_CITY = "selectedCity";

	public String getName();

	public void setName(String name);

	public String getUf();

	public void setUf(String uf);

	public String getId();

	public String getRegionAlias();

	public String getKey();

	public String getResourceName();

	public String getSharedPreferencesId();

}
