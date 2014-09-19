package com.hubahuma.mobile.entity.service;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class WriteAnnouncementParam {

	private List<String> photos;
	
	private String text;
	
	private String title;
	
	private List<String> recepients;

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getRecepients() {
		return recepients;
	}

	public void setRecepients(List<String> recepients) {
		this.recepients = recepients;
	}
	
}
