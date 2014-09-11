package com.hubahuma.mobile.entity.service;

import java.util.List;

public class FetchDetailTeacherParam {

	List<String> teacherId;
	List<String> userId;
	List<String> username;

	public List<String> getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(List<String> teacherId) {
		this.teacherId = teacherId;
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
