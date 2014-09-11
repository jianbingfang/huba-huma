package com.hubahuma.mobile.entity.service;

import java.util.List;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.Teacher;
import com.hubahuma.mobile.entity.User;

public class FetchDetailTeacherResp {

	private List<Teacher> teacherObjects;
	private List<User> userObjects;

	public List<Teacher> getTeacherObjects() {
		return teacherObjects;
	}

	public void setTeacherObjects(List<Teacher> teacherObjects) {
		this.teacherObjects = teacherObjects;
	}

	public List<User> getUserObjects() {
		return userObjects;
	}

	public void setUserObjects(List<User> userObjects) {
		this.userObjects = userObjects;
	}

}
