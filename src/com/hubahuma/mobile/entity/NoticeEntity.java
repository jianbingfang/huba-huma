package com.hubahuma.mobile.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NoticeEntity implements Serializable {

	@JsonProperty("_id")
	private String noticeId;

	private String author;

	private String authorId;

	private String authorPhoto;

	private String title;

	private String text;

	private Date date;

	private List<String> photos;

	@JsonProperty("_recipients")
	private List<String> recipients;

	@JsonProperty("_recipientsRead")
	private List<String> recipientsRead;

	@JsonProperty("_type")
	private String type;

	// private UserEntity author;

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorPhoto() {
		return authorPhoto;
	}

	public void setAuthorPhoto(String authorPhoto) {
		this.authorPhoto = authorPhoto;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public List<String> getRecipientsRead() {
		return recipientsRead;
	}

	public void setRecipientsRead(List<String> recipientsRead) {
		this.recipientsRead = recipientsRead;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
