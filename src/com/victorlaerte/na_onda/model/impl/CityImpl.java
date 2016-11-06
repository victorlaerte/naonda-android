package com.victorlaerte.na_onda.model.impl;

import org.apache.commons.lang3.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;

import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.util.CharPool;
import com.victorlaerte.na_onda.util.StringPool;

public class CityImpl implements City {

	private String id;
	private String name;
	private String uf;
	private String resourceName;
	private String regionAlias;

	public CityImpl(String id, String name, String uf, String resourceName, String regionAlias) {

		this.id = id;
		this.name = name;
		this.uf = uf;
		this.resourceName = resourceName;
		this.regionAlias = regionAlias;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public void setName(String name) {

		this.name = name;
	}

	@Override
	public String getUf() {

		return uf;
	}

	@Override
	public void setUf(String uf) {

		this.uf = uf;
	}

	@Override
	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	@Override
	public String getResourceName() {

		return resourceName;
	}

	public void setResourceName(String resourceName) {

		this.resourceName = resourceName;
	}

	@Override
	public String getRegionAlias() {

		return regionAlias;
	}

	public void setRegionAlias(String regionAlias) {

		this.regionAlias = regionAlias;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(uf);
		dest.writeString(resourceName);
		dest.writeString(regionAlias);
	}

	public static final Parcelable.Creator<CityImpl> CREATOR = new Parcelable.Creator<CityImpl>() {

		@Override
		public CityImpl createFromParcel(Parcel in) {

			return new CityImpl(in);
		}

		@Override
		public CityImpl[] newArray(int size) {

			return new CityImpl[size];
		}
	};

	private CityImpl(Parcel in) {

		id = in.readString();
		name = in.readString();
		uf = in.readString();
		resourceName = in.readString();
		regionAlias = in.readString();
	}

	@Override
	public String getKey() {

		String key = StringPool.BLANK;

		key = StringUtils.stripAccents(name).replaceAll("\\s", "%20");

		return key;
	}

	@Override
	public String getSharedPreferencesId() {

		StringBuilder sb = new StringBuilder();
		sb.append(resourceName);
		sb.append(CharPool.SEMICOLON);
		sb.append(getKey());

		return sb.toString();
	}
}
