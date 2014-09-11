package com.hubahuma.mobile.entity.service;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.Teacher;
import com.hubahuma.mobile.entity.User;


public class RegisterTeacherResp {

	private boolean ok;
	private Teacher teacher;
	private User user;
	private String token;
	private String reason;
	
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
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
