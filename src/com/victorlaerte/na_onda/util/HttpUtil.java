package com.victorlaerte.na_onda.util;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author VictorOliveira
 */
public class HttpUtil {

	private static ClientConnectionManager connectionManager;

	public static JSONObject getJSON(boolean post, String url, Map<String, String> params, Map<String, String> headers,
			Map<String, String> cookies) throws IOException {

		int statusCode = 0;

		HttpParams httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 60000);

		HttpConnectionParams.setSoTimeout(httpParams, 60000);

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);

			if (cookies != null) {

				for (Map.Entry<String, String> entry : cookies.entrySet()) {

					BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), entry.getValue());

					cookie.setPath("/");
					cookie.setDomain(new URL(url).getHost());

					httpClient.getCookieStore().addCookie(cookie);
				}
			}

			HttpRequestBase request = null;

			if (post) {

				request = post(url, params, headers);

			} else {

				request = get(url, params, headers);

			}

			BasicHttpContext context = new BasicHttpContext();

			HttpResponse response = httpClient.execute(request, context);

			HttpEntity entity = response.getEntity();

			Logger.getLogger(HttpUtil.class.getName()).log(Level.INFO, response.toString());
			Logger.getLogger(HttpUtil.class.getName()).log(Level.INFO, response.getStatusLine().toString());

			if (entity == null) {

				StatusLine status = response.getStatusLine();
				JSONObject returnJson = new JSONObject();

				returnJson.put("statusCode", status.getStatusCode());
				returnJson.put("statusLine", status.toString());
				statusCode = status.getStatusCode();

				return returnJson;
			}

			String result = EntityUtils.toString(entity);

			Logger.getLogger(HttpUtil.class.getName()).log(Level.INFO, result);

			StatusLine status = response.getStatusLine();
			JSONObject returnJson = new JSONObject();

			returnJson.put("statusCode", status.getStatusCode());
			returnJson.put("statusLine", status.toString());
			statusCode = status.getStatusCode();

			if (!result.trim().equals("")) {

				try {

					returnJson.put("body", new JSONObject(result));

				} catch (JSONException e) {

					Logger.getLogger(HttpUtil.class.getName()).log(Level.WARNING, e.getMessage());
				}
			}

			JSONArray cookiesArray = new JSONArray();

			for (Cookie cookie : httpClient.getCookieStore().getCookies()) {

				JSONObject cookieJson = new JSONObject();

				cookieJson.put(cookie.getName(), cookie.getValue());

				cookiesArray.put(cookieJson);
			}

			returnJson.put("cookies", cookiesArray);

			return returnJson;

		} catch (UnknownHostException ex) {

			Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);

			throw new UnknownHostException("Code: " + statusCode + " " + getErrorMsg(statusCode));

		} catch (SocketTimeoutException ex) {

			Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);

			throw new SocketTimeoutException("Code: " + statusCode + " " + getErrorMsg(statusCode));

		} catch (SocketException ex) {

			Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);

			throw new SocketException("Code: " + statusCode + " " + getErrorMsg(statusCode));

		} catch (JSONException ex) {

			Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);

			throw new RuntimeException(ex);
		}

	}

	private static HttpPost post(String url, Map<String, String> params, Map<String, String> headers)
			throws IOException {

		HttpPost postMehod = new HttpPost(url);

		if (headers != null) {

			for (Map.Entry<String, String> entry : headers.entrySet()) {

				postMehod.addHeader(entry.getKey(), entry.getValue());
			}
		}

		if (params != null) {

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			for (Map.Entry<String, String> entry : params.entrySet()) {

				nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
			}

			postMehod.setEntity(new UrlEncodedFormEntity(nvps, Constants.UTF_8));
		}

		return postMehod;
	}

	public static HttpGet get(String url, Map<String, String> params, Map<String, String> headers) throws IOException {

		HttpGet getMehod = new HttpGet(url);

		if (headers != null) {

			for (Map.Entry<String, String> entry : headers.entrySet()) {

				// getMehod.setHeader(entry.getKey(), entry.getValue());
				getMehod.setHeader(entry.getKey(), entry.getValue());
			}
		}

		if (params != null) {

			HttpParams httpParams = new BasicHttpParams();

			for (Map.Entry<String, String> entry : params.entrySet()) {

				httpParams.setParameter(entry.getKey(), String.valueOf(entry.getValue()));
			}

			getMehod.setParams(httpParams);
		}

		return getMehod;
	}

	public static String getErrorMsg(int statusCode) {

		String info = null;

		switch (statusCode) {

		case 0:
			info = "O servidor não respondeu ou está inacessível. \n" + "Verifique sua conexão ou entre em contato com o administrador";
			break;

		case 100:
			info = "Continuar";
			break;

		case 101:
			info = "Mudando protocolos";
			break;

		case 200:
			info = "OK";
			break;

		case 201:
			info = "Criado";
			break;

		case 202:
			info = "Aceito";
			break;

		case 203:
			info = "Não autorizado";
			break;

		case 204:
			info = "Sem conteúdo";
			break;

		case 205:
			info = "Reset";
			break;

		case 206:
			info = "Conteúdo parcial";
			break;

		case 207:
			info = "Status Multi (WebDAV)(RFC4918)";
			break;

		case 300:
			info = "Múltipla escolha";
			break;

		case 301:
			info = "Movido";
			break;

		case 302:
			info = "Encontrado";
			break;

		case 303:
			info = "Ver outro";
			break;

		case 304:
			info = "Não modificado";
			break;

		case 305:
			info = "Use proxy";
			break;

		case 306:
			info = "Sem uso";
			break;

		case 307:
			info = "Redirecionamento temporário";
			break;

		case 400:
			info = "Requisição incompreensível";
			break;

		case 401:
			info = "Usuário não autorizado";
			break;

		case 402:
			info = "Pagamento necessário";
			break;

		case 403:
			info = "Proibido";
			break;

		case 404:
			info = "Não encontrado";
			break;

		case 405:
			info = "Método não permitido";
			break;

		case 406:
			info = "Não aceitável";
			break;

		case 407:
			info = "Autenticação de proxy necessária";
			break;

		case 408:
			info = "Tempo de conexão esgotado";
			break;

		case 409:
			info = "Conflito";
			break;

		case 410:
			info = "Não existe mais";
			break;

		case 411:
			info = "Comprimento necessário";
			break;

		case 412:
			info = "Pré-condição falhou";
			break;

		case 413:
			info = "Entidade de solicitação muito grande";
			break;

		case 414:
			info = "URL muito longa";
			break;

		case 415:
			info = "Tipo de mídia não suportada";
			break;

		case 416:
			info = "Intervalo requisitado não satisfatório";
			break;

		case 417:
			info = "Falha de expectativa";
			break;

		case 500:
			info = "Erro interno no servidor";
			break;

		case 501:
			info = "Não implementado no servidor";
			break;

		case 502:
			info = "Bad Gateway";
			break;

		case 503:
			info = "Serviço não disponível";
			break;

		case 504:
			info = "Gateway Timeout";
			break;

		case 505:
			info = "Versão HTTP não suportada";
			break;

		}

		return info;
	}
}