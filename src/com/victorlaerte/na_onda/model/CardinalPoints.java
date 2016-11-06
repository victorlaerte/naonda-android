package com.victorlaerte.na_onda.model;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.victorlaerte.na_onda.util.AndroidUtil;

public enum CardinalPoints implements Parcelable {

	NORTH("N"), SOUTH("S"), EAST("E"), WEST("W"), NORTHEAST("NE"), SOUTHEAST("SE"), NORTHWEST("NW"), SOUTHWEST("SW"), NORTH_NORTHEAST(
			"NNE"), EAST_NORTHEAST("ENE"), EAST_SOUTHEAST("ESE"), SOUTH_SOUTHEAST("SSE"), SOUTH_SOUTHWEST("SSW"), WEST_SOUTHWEST(
			"WSW"), WEST_NORTHWEST("WNW"), NORTH_NORTHWEST("NNW");

	private String acronym;

	CardinalPoints(String acronym) {

		this.acronym = acronym;
	}

	public String getAcronym() {

		return acronym;
	}

	public String getName(Activity activity) {

		int nameId = activity.getResources().getIdentifier(acronym, "string", activity.getPackageName());
		return AndroidUtil.getString(activity, nameId);
	}

	public Drawable getDrawable(Activity activity) {

		int drawableId = activity.getResources().getIdentifier(acronym.toLowerCase(), "drawable",
				activity.getPackageName());
		return activity.getResources().getDrawable(drawableId);
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(ordinal());
	}

	public static final Creator<CardinalPoints> CREATOR = new Creator<CardinalPoints>() {

		@Override
		public CardinalPoints createFromParcel(final Parcel source) {

			return CardinalPoints.values()[source.readInt()];
		}

		@Override
		public CardinalPoints[] newArray(final int size) {

			return new CardinalPoints[size];
		}
	};
}
