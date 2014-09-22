package com.hubahuma.mobile.entity.service;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FetchAnnouncementParam {

	private String beginId;

	private String untilId;

	@JsonProperty("_accessToken")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBeginId() {
		return beginId;
	}

	public void setBeginId(String beginId) {
		this.beginId = beginId;
	}

	public String getUntilId() {
		return untilId;
	}

	public void setUntilId(String untilId) {
		this.untilId = untilId;
	}

}
