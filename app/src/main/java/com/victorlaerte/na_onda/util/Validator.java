package com.victorlaerte.na_onda.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @author Thiago Andrade
 */
public class Validator {

	public static boolean equals(boolean boolean1, boolean boolean2) {

		if (boolean1 == boolean2) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(byte byte1, byte byte2) {

		if (byte1 == byte2) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(char char1, char char2) {

		if (char1 == char2) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(double double1, double double2) {

		if (Double.compare(double1, double2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(float float1, float float2) {

		if (Float.compare(float1, float2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(int int1, int int2) {

		if (int1 == int2) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(long long1, long long2) {

		if (long1 == long2) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(Object obj1, Object obj2) {

		if ((obj1 == null) && (obj2 == null)) {
			return true;
		} else if ((obj1 == null) || (obj2 == null)) {
			return false;
		} else {
			return obj1.equals(obj2);
		}
	}

	public static boolean equals(short short1, short short2) {

		if (short1 == short2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns <code>true</code> if the string is a valid host name.
	 * 
	 * @param name
	 *            the string to check
	 * @return <code>true</code> if the string is a valid host name; <code>false</code> otherwise
	 */
	public static boolean isHostName(String name) {

		if (isNull(name)) {
			return false;
		}

		char[] nameCharArray = name.toCharArray();

		if ((nameCharArray[0] == CharPool.DASH) || (nameCharArray[0] == CharPool.PERIOD)
				|| (nameCharArray[nameCharArray.length - 1] == CharPool.DASH)) {

			return false;
		}

		for (char c : nameCharArray) {
			if (!isChar(c) && !isDigit(c) && (c != CharPool.CLOSE_BRACKET) && (c != CharPool.COLON) && (c != CharPool.DASH)
					&& (c != CharPool.OPEN_BRACKET) && (c != CharPool.PERIOD)) {

				return false;
			}
		}

		return true;
	}

	public static boolean isAddress(String address) {

		if (isNull(address)) {
			return false;
		}

		String[] tokens = address.split("@");

		if (tokens.length != 2) {
			return false;
		}

		for (String token : tokens) {
			for (char c : token.toCharArray()) {
				if (Character.isWhitespace(c)) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean isAscii(char c) {

		return CharUtils.isAscii(c);
	}

	/**
	 * Returns <code>true</code> if c is a letter between a-z and A-Z.
	 * 
	 * @return <code>true</code> if c is a letter between a-z and A-Z
	 */
	public static boolean isChar(char c) {

		return CharUtils.isAsciiAlpha(c);
	}

	/**
	 * Returns <code>true</code> if s is a string of letters that are between
	 * a-z and A-Z.
	 * 
	 * @return <code>true</code> if s is a string of letters that are between
	 *         a-z and A-Z
	 */
	public static boolean isChar(String s) {

		return StringUtils.isAlpha(s);
	}

	public static boolean isDate(int month, int day, int year) {

		return isGregorianDate(month, day, year);
	}

	/**
	 * Returns <code>true</code> if c is a digit between 0 and 9.
	 * 
	 * @return <code>true</code> if c is a digit between 0 and 9
	 */
	public static boolean isDigit(char c) {

		return CharUtils.isAsciiNumeric(c);
	}

	/**
	 * Returns <code>true</code> if s is a string of letters that are between 0
	 * and 9.
	 * 
	 * @return <code>true</code> if s is a string of letters that are between 0
	 *         and 9
	 */
	public static boolean isDigit(String s) {

		return NumberUtils.isDigits(s);
	}

	public static boolean isEmailAddressSpecialChar(char c) {

		// LEP-1445
		for (int i = 0; i < _EMAIL_ADDRESS_SPECIAL_CHAR.length; i++) {
			if (c == _EMAIL_ADDRESS_SPECIAL_CHAR[i]) {
				return true;
			}
		}

		return false;
	}

	public static boolean isGregorianDate(int month, int day, int year) {

		if ((month < 0) || (month > 11)) {
			return false;
		}

		int[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (month == 1) {
			int febMax = 28;

			if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0)) {

				febMax = 29;
			}

			if ((day < 1) || (day > febMax)) {
				return false;
			}
		} else if ((day < 1) || (day > months[month])) {
			return false;
		}

		return true;
	}

	public static boolean isHex(String s) {

		if (isNull(s)) {
			return false;
		}

		return true;
	}

	public static boolean isHTML(String s) {

		if (isNull(s)) {
			return false;
		}

		if (((s.indexOf("<html>") != -1) || (s.indexOf("<HTML>") != -1)) && ((s.indexOf("</html>") != -1) || (s.indexOf("</HTML>") != -1))) {

			return true;
		}

		return false;
	}

	public static boolean isIPAddress(String ipAddress) {

		Matcher matcher = _ipAddressPattern.matcher(ipAddress);

		return matcher.matches();
	}

	public static boolean isJulianDate(int month, int day, int year) {

		if ((month < 0) || (month > 11)) {
			return false;
		}

		int[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (month == 1) {
			int febMax = 28;

			if ((year % 4) == 0) {
				febMax = 29;
			}

			if ((day < 1) || (day > febMax)) {
				return false;
			}
		} else if ((day < 1) || (day > months[month])) {
			return false;
		}

		return true;
	}

	public static boolean isName(String name) {

		if (isNull(name)) {
			return false;
		}

		for (char c : name.trim().toCharArray()) {
			if (((!isChar(c)) && (!Character.isWhitespace(c))) || (c == CharPool.COMMA)) {

				return false;
			}
		}

		return true;
	}

	public static boolean isNotNull(Long l) {

		return !isNull(l);
	}

	public static boolean isNotNull(Object obj) {

		return !isNull(obj);
	}

	public static boolean isNotNull(Object[] array) {

		return !isNull(array);
	}

	public static boolean isNotNull(String s) {

		return !isNull(s);
	}

	public static boolean isNull(Long l) {

		if ((l == null) || (l.longValue() == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNull(Object obj) {

		if (obj instanceof Long) {
			return isNull((Long) obj);
		} else if (obj instanceof String) {
			return isNull((String) obj);
		} else if (obj == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNull(Object[] array) {

		if ((array == null) || (array.length == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNull(String s) {

		if (StringUtils.stripToNull(s) == null) {
			return true;
		}

		int counter = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == CharPool.SPACE) {
				continue;
			} else if (counter > 3) {
				return false;
			}

			if (counter == 0) {
				if (c != CharPool.LOWER_CASE_N) {
					return false;
				}
			} else if (counter == 1) {
				if (c != CharPool.LOWER_CASE_U) {
					return false;
				}
			} else if ((counter == 2) || (counter == 3)) {
				if (c != CharPool.LOWER_CASE_L) {
					return false;
				}
			}

			counter++;
		}

		if ((counter == 0) || (counter == 4)) {
			return true;
		}

		return false;
	}

	public static boolean isNumber(String number) {

		return NumberUtils.isNumber(number);
	}

	public static boolean isPassword(String password) {

		if (isNull(password)) {
			return false;
		}

		if (password.length() < 4) {
			return false;
		}

		for (char c : password.toCharArray()) {
			if (!isChar(c) && !isDigit(c)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isUrl(String url) {

		if (Validator.isNotNull(url)) {
			try {
				new URL(url);

				return true;
			} catch (MalformedURLException murle) {
			}
		}

		return false;
	}

	public static boolean isVariableName(String variableName) {

		if (isNull(variableName)) {
			return false;
		}

		Matcher matcher = _variableNamePattern.matcher(variableName);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isVariableTerm(String s) {

		if (s.startsWith(_VARIABLE_TERM_BEGIN) && s.endsWith(_VARIABLE_TERM_END)) {

			return true;
		} else {
			return false;
		}
	}

	public static boolean isWhitespace(char c) {

		int i = c;

		if ((i == 0) || Character.isWhitespace(c)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isXml(String s) {

		if (s.startsWith(_XML_BEGIN) || s.startsWith(_XML_EMPTY)) {
			return true;
		} else {
			return false;
		}
	}

	private static final char[] _EMAIL_ADDRESS_SPECIAL_CHAR = new char[] { '.', '!', '#', '$', '%', '&', '\'', '*', '+', '-', '/', '=',
			'?', '^', '_', '`', '{', '|', '}', '~' };

	private static final String _VARIABLE_TERM_BEGIN = "[$";

	private static final String _VARIABLE_TERM_END = "$]";

	private static final String _XML_BEGIN = "<?xml";

	private static final String _XML_EMPTY = "<root />";

	private static Pattern _ipAddressPattern = Pattern.compile("\\b" + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
			+ "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
			+ "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])" + "\\b");
	private static Pattern _variableNamePattern = Pattern.compile("[_a-zA-Z]+[_a-zA-Z0-9]*");

}
