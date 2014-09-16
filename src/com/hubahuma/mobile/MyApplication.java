package com.hubahuma.mobile;

import org.androidannotations.annotations.EApplication;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.Parent;
import com.hubahuma.mobile.entity.Teacher;
import com.hubahuma.mobile.entity.User;
import com.hubahuma.mobile.entity.UserEntity;

import android.app.Application;

@EApplication
public class MyApplication extends Application {

	public static final boolean testMode = false;

	private String token = null;

	private UserEntity currentUser = null;

//	private String userType = null;

	public UserEntity getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserEntity user) {
		currentUser = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

//	public String getUserType() {
//		return userType;
//	}
//
//	public void setUserType(String type) {
//		this.userType = type;
//	}

}
