package com.hubahuma.mobile.utils;

import com.hubahuma.mobile.entity.UserEntity;

public class GlobalVar {

	public static final String API_SERVER_DOMAIN = "";
	
	public static String token = null;
	
	public static UserEntity curUser = null;
	
	public static UserEntity getCurrentUser(){
		return curUser;
	}
	
	public static void setCurrentUser(UserEntity user){
		curUser = user;
	}
	
}
