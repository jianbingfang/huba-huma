package com.hubahuma.mobile.service;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface SharedPrefs {

	@DefaultBoolean(false)
	boolean rememberMe();
	
	@DefaultString("")
	String username();
	
	@DefaultString("")
	String password();
	
	@DefaultString("")
	String token();
	
	@DefaultLong(Long.MAX_VALUE)
	long lastTokenUpdated();
}
