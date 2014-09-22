package com.hubahuma.mobile.entity.service;

import org.codehaus.jackson.annotate.JsonProperty;

public class PassVerificationRequestParentParam {

	private String id;

	@JsonProperty("_accessToken")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
