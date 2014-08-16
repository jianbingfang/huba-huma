package com.hubahuma.mobile.entity;

import java.util.Date;

public class ChatMsgEntity {

	private String name;
	private Date date;
	private String content;
	private boolean isComMsg = true;// 是否为收到的消息

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public ChatMsgEntity() {
	}

	public ChatMsgEntity(String name, Date date, String text, boolean isComMsg) {
		this.name = name;
		this.date = date;
		this.content = text;
		this.isComMsg = isComMsg;
	}

}