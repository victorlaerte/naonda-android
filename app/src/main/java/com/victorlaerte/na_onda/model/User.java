package com.victorlaerte.na_onda.model;

import android.os.Parcelable;

public interface User extends Parcelable {

	public String getName();

	public String getUserName();

	public void setName(String name);

	public boolean isLoggedIn();

	public void setLoggedIn(boolean isLoggedIn);

	public String getId();

	public void setId(String id);
}
