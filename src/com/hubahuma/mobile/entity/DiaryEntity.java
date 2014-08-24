package com.hubahuma.mobile.entity;

import java.util.Date;

public class DiaryEntity {

	private UserEntity author;
	private String img;
	private String content;
	private Date date;
	private int collectNum;
	private int commentNum;
	private boolean isLiked;
	
	public UserEntity getAuthor() {
		return author;
	}
	public void setAuthor(UserEntity author) {
		this.author = author;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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
	public int getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public boolean isLiked() {
		return isLiked;
	}
	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
	
}
