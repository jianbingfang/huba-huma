package com.hubahuma.mobile.entity.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FetchDetailOrgParam {

	List<String> orgId;
	List<String> userId;
	List<String> username;
	
	@JsonProperty("_accessToken")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getOrgId() {
		return orgId;
	}

	public void setOrgId(List<String> orgId) {
		this.orgId = orgId;
	}

	public List<String> getUserId() {
		return userId;
	}

	public void setUserId(List<String> userId) {
		this.userId = userId;
	}

	public List<String> getUsername() {
		return username;
	}

	public void setUsername(List<String> username) {
		this.username = username;
	}

}
