package com.hubahuma.mobile.entity.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class DeleteContactsParam {

	private List<String> ids;

	@JsonProperty("_accessToken")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

}
