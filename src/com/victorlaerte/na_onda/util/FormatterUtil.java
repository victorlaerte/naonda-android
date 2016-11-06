package com.victorlaerte.na_onda.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatterUtil {

	public static final String GENERAL_SHORT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final String FRIENDLY_DATE_FORMAT = "HH:mm dd/MM/yyyy";
	public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZ";

	public static final DateFormat getDateFormat() {

		return new SimpleDateFormat(DATE_PATTERN, Constants.LOCALE_PT_BR);
	}

	public static final DateFormat getFriendlyDateFormat() {

		return new SimpleDateFormat(FRIENDLY_DATE_FORMAT, Constants.LOCALE_PT_BR);
	}
	
	public static final DateFormat getGeneralShortDateFormat() {
		
		return new SimpleDateFormat(GENERAL_SHORT_DATE_FORMAT, Constants.LOCALE_PT_BR);
	}

	public static final String getFormattedDate(Date date) {

		if (date == null) {
			return StringPool.BLANK;
		}

		return getFriendlyDateFormat().format(date);
	}

}
