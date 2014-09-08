package com.hubahuma.mobile.utils;

import com.hubahuma.mobile.entity.UserEntity;

public class GlobalVariable {

	private static UserEntity curUser = null;
	
	public static UserEntity getCurrentUser(){
		return curUser;
	}
	
	public static void setCurrentUser(UserEntity user){
		curUser = user;
	}
	
}
