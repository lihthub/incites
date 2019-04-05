package com.example.incites.util;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;

public class HttpUtils {
	public static Log log = LogFactory.getLog(HttpUtils.class);
	
	public static String httpGet(final String url) {
		if (url == null || url.length() == 0) {
			log.error("httpGet, url is null");
			return null;
		}
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse resp = httpClient.execute(httpGet);
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				log.error("httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
				return null;
			}
			byte[] buf = EntityUtils.toByteArray(resp.getEntity());
			return new String(buf, "utf-8");
		} catch (Exception e) {
			log.error("httpGet exception", e);
			return null;
		}
	}
	
	public static String httpPost(String url, List<NameValuePair> params) {
		if (url == null || url.length() == 0) {
			log.error("httpPost, url is null");
			return null;
		}
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
			httpPost.setEntity(entity);
//			httpPost.setHeader("Accept", "application/json");
//			httpPost.setHeader("Content-type", "application/json");
			HttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				log.error("httpPost fail, status code = " + resp.getStatusLine().getStatusCode());
				return null;
			}
			byte[] buf = EntityUtils.toByteArray(resp.getEntity());
			return new String(buf, "utf-8");
		} catch (Exception e) {
			log.error("httpPost exception", e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HttpResult connect(String url, Map<String, String> cookies) {
		HttpResult result = new HttpResult();
		int count = 0;
		try {
			Connection connect = Jsoup.connect(url);
			if (cookies != null && !cookies.isEmpty()) {
				connect.cookies(cookies);
			}
			Response res = connect.timeout(60000).execute();
			String body = res.body();
			while (body != null && body.contains("!DOCTYPE html")) {
				if (count > 6) {
					cookies = Collections.EMPTY_MAP;
					body = "{\"status\":\"FAILURE\"}";
					break;
				}
				log.warn("httpGet redirect, " + res.url().toString());
				cookies = res.cookies();
				res = Jsoup.connect(url).cookies(cookies).timeout(60000).execute();
				body = res.body();
				count++;
			}
			result.setBody(body != null ? StringUtil.normaliseWhitespace(body) : body);
			result.setCookies(cookies);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
