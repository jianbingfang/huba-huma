package com.hubahuma.mobile.entity;

import java.io.Serializable;

public class AuthResp implements Serializable{

	private boolean result;
	private String token;
	private String type;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
