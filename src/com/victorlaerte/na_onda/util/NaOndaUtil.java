package com.victorlaerte.na_onda.util;

import org.apache.commons.lang3.StringUtils;

import com.victorlaerte.na_onda.exception.AuthenticationException;
import com.victorlaerte.na_onda.model.User;

public class NaOndaUtil {

	private static NaOndaUtil instance = new NaOndaUtil();

	private User user;

	public static NaOndaUtil getInstance() {

		return instance;
	}

	public User getUser() throws AuthenticationException {

		if (Validator.isNull(user)) {

			throw new AuthenticationException("User was not set");

		} else {

			return user;
		}
	}

	public void setUser(User user) {

		this.user = user;
	}

	public String getStateKey(String stateName) {

		String key = StringPool.BLANK;

		key = StringUtils.stripAccents(stateName).toLowerCase().replaceAll("\\s", "_");

		return key;
	}
}
