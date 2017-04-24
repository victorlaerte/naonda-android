package com.victorlaerte.na_onda.util;

import org.apache.commons.lang3.StringUtils;

public class NaOndaUtil {

	public static String getStateKey(String stateName) {

		String key = StringPool.BLANK;

		key = StringUtils.stripAccents(stateName).toLowerCase().replaceAll("\\s", "_");

		return key;
	}
}
