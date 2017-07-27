package com.victorlaerte.na_onda.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.util.Log;

public class WebServiceData {

	private static final String LOG_TAG = WebServiceData.class.getName();

	private static InputStream read(String uri) throws IOException {

		URL url = new URL(uri);

		Log.d(LOG_TAG, uri);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestProperty("Request-Method", "GET");
		connection.setDoInput(true);
		connection.setDoOutput(false);
		connection.connect();

		InputStream in = connection.getInputStream();
		return in;
	}

	public static Document readDocumentResponse(String uri) throws IOException, ParserConfigurationException,
			SAXException, JSONException {

		InputStream in = read(uri);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(in);

		return document;
	}

	public static String readStringResponse(String uri) throws IOException, ParserConfigurationException, SAXException,
			JSONException {

		InputStream in = read(uri);

		String str = IOUtils.toString(in);

		return str;
	}
}
