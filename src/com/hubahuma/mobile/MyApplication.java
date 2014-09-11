package com.hubahuma.mobile;

import org.androidannotations.annotations.EApplication;

import com.hubahuma.mobile.entity.UserEntity;

import android.app.Application;

@EApplication
public class MyApplication extends Application {

	public static final boolean testMode = false;

	public String token = null;

	private UserEntity curUser = null;

	public UserEntity getCurrentUser() {
		return curUser;
	}

	public void setCurrentUser(UserEntity user) {
		curUser = user;
	}

}
