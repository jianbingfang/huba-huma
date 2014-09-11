package com.hubahuma.mobile.entity.service;

import java.util.List;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.User;

public class FetchDetailOrgResp {

	private List<Organization> organizationObjects;
	private List<User> userObjects;

	public List<Organization> getOrganizationObjects() {
		return organizationObjects;
	}

	public void setOrganizationObjects(List<Organization> organizationObjects) {
		this.organizationObjects = organizationObjects;
	}

	public List<User> getUserObjects() {
		return userObjects;
	}

	public void setUserObjects(List<User> userObjects) {
		this.userObjects = userObjects;
	}

}
