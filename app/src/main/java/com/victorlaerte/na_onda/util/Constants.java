package com.victorlaerte.na_onda.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Constants {

	public static final String PUBLISH_ACTIONS = "publish_actions";
	public static final List<String> PERMISSIONS_AS_LIST = Arrays.asList(PUBLISH_ACTIONS);
	public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");
	public static final String UTF_8 = "UTF-8";

	public static final String USER_KEY = "user";
	public static final String CITY_KEY = "city";
	public static final String GRAPH_HEIGHT_TYPE_KEY = "altura";

	public static final String FACEBOOK_GRAPH_URL = "https://graph.facebook.com/";
	public static final String FACEBOOK_SMALL_PICTURE_URL_COMPLEMENT = "/picture?type=small";
	public static final String FACEBOOK_LARGE_PICTURE_URL_COMPLEMENT = "/picture?type=large";
	public static final String INPE_SERVICE_BASE_URL = "http://servicos.cptec.inpe.br/XML/cidade/";
	public static final String INPE_SERVICE_FORECAST_6DAYS_8HOURS_BY_DAY_SUFFIX = "/todos/tempos/ondas.xml";
	public static final String INPE_GRAPH_BASE_URL = "http://img0.cptec.inpe.br/~rgrafico/portal_ondas/wwatch/";
	public static final String MARKET_URL = "market://details?id=";
	public static final String GOOGLE_PLAY_URL = "http://play.google.com/store/apps/details?id=";
	public static final String FACEBOOK_NA_ONDA_PAGE_URL = "https://www.facebook.com/naondaapp";

	public static final String PREFS_FILE = "preferences";

}
