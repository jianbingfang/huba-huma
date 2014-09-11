package com.hubahuma.mobile.entity.service;

import java.util.List;

public class FetchDetailParentParam {

	List<String> parentId;
	List<String> userId;
	List<String> username;


	public List<String> getParentId() {
		return parentId;
	}

	public void setParentId(List<String> parentId) {
		this.parentId = parentId;
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
