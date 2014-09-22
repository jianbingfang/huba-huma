package com.hubahuma.mobile.service;

import java.util.List;

import com.hubahuma.mobile.entity.VerificationRequestParent;

public class FetchVerificationRequestParentResp {

	private List<VerificationRequestParent> requests;

	public List<VerificationRequestParent> getRequests() {
		return requests;
	}

	public void setRequests(List<VerificationRequestParent> requests) {
		this.requests = requests;
	}

}
