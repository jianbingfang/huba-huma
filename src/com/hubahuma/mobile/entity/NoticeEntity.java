package com.hubahuma.mobile.entity;

import java.io.Serializable;
import java.util.Date;

public class NoticeEntity implements Serializable{

	private UserEntity user;
	
	private String title;
	
	private String content;
	
	private Date date;
	
	private String image;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
}
