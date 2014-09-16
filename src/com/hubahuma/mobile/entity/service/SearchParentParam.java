package com.hubahuma.mobile.entity.service;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchParentParam {

	private String name;

	@JsonProperty("_accessToken")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
