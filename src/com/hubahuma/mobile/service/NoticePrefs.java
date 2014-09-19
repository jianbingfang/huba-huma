package com.hubahuma.mobile.service;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.ACTIVITY_DEFAULT)
public interface NoticePrefs {

	String localNoticeJson();
	
}
