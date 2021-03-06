package com.hubahuma.mobile.entity;

import java.io.Serializable;
import java.util.Date;

public class ChatMsgEntity implements Serializable {

	public interface MsgState {
		public static final int ARRIVED = 0;
		public static final int SENDING = 1;
		public static final int SENDED = 2;
	}

	private UserEntity user;
	private Date date;
	private String content;
	private boolean isComMsg;// 是否为收到的消息
	private int state;
	private boolean showDate;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isComMsg() {
		return isComMsg;
	}

	public void setComMsg(boolean isComMsg) {
		this.isComMsg = isComMsg;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isShowDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public ChatMsgEntity() {
	}

	public ChatMsgEntity(UserEntity user, Date date, String text, boolean isComMsg,
			boolean showDate) {
		this.user = user;
		this.date = date;
		this.content = text;
		this.isComMsg = isComMsg;
		this.showDate = showDate;
	}

}