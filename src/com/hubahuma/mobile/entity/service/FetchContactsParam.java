package com.hubahuma.mobile.entity.service;

import org.codehaus.jackson.annotate.JsonProperty;

public class FetchContactsParam {

	@JsonProperty("_accessToken")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
