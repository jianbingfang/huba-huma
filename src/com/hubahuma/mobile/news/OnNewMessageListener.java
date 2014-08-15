package com.hubahuma.mobile.news;

public interface OnNewMessageListener {

	public static final int NOTICE_MESSAGE = 0;
	public static final int TEACHER_MESSAGE = 1;
	public static final int PARENTS_MESSAGE = 2;
	public static final int GROUP_MESSAGE = 3;
	
	public void OnNewMessageReady(int messageType);
	public void OnNewMessageShowed(int messageType);
	
}
