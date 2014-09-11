package com.hubahuma.mobile.entity.service;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.User;


public class RegisterOrgResp {

	private boolean ok;
	private Organization organization;
	private User user;
	private String token;
	private String reason;
	
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
