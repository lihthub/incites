package com.example.incites.util;

import java.util.Map;

public class HttpResult {
	private String body;
	private Map<String, String> cookies;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

}
