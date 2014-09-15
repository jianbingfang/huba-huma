package com.hubahuma.mobile.entity.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.Parent;
import com.hubahuma.mobile.entity.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FetchDetailParentResp {

	private List<Parent> parentObjects;
	private List<User> userObjects;

	public List<Parent> getParentObjects() {
		return parentObjects;
	}

	public void setParentObjects(List<Parent> parentObjects) {
		this.parentObjects = parentObjects;
	}

	public List<User> getUserObjects() {
		return userObjects;
	}

	public void setUserObjects(List<User> userObjects) {
		this.userObjects = userObjects;
	}

}
