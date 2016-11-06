package com.victorlaerte.na_onda.model.impl;

import android.os.Parcel;
import android.os.Parcelable;

import com.victorlaerte.na_onda.model.User;

public class UserImpl implements User {

	private String id;
	private String name;
	private boolean isLoggedIn;
	private String userName;

	public UserImpl(String id, String userName, String name, boolean isLoggedIn) {

		this.id = id;
		this.userName = userName;
		this.name = name;
		this.isLoggedIn = isLoggedIn;
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
	public boolean isLoggedIn() {

		return isLoggedIn;
	}

	@Override
	public void setLoggedIn(boolean isLoggedIn) {

		this.isLoggedIn = isLoggedIn;
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public void setId(String id) {

		this.id = id;
	}

	@Override
	public String getUserName() {

		return userName;
	}
	
	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(userName);
		dest.writeByte((byte) (isLoggedIn ? 1 : 0));
	}

	public static final Parcelable.Creator<UserImpl> CREATOR = new Parcelable.Creator<UserImpl>() {

		@Override
		public UserImpl createFromParcel(Parcel in) {

			return new UserImpl(in);
		}

		@Override
		public UserImpl[] newArray(int size) {

			return new UserImpl[size];
		}
	};

	private UserImpl(Parcel in) {

		id = in.readString();
		name = in.readString();
		userName = in.readString();
		isLoggedIn = in.readByte() != 0;
	}

}
