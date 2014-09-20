package com.hubahuma.mobile.entity;

import java.io.Serializable;
import java.util.List;

import com.hubahuma.mobile.UserType;

public class UserEntity implements Serializable, Comparable<UserEntity> {

	private String username;
	private String password;
	private String userId;
	private String name;
	private String phone;
	private String remark;
	private String type;
	private String portrait;
	private String address;
	private String description;
	private List<String> tags;
	private boolean verified;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@Override
	public int compareTo(UserEntity another) {
		return this.name.compareTo(another.getName());
	}
	
	public void bind(Teacher t){
		this.name = t.getName();
		this.portrait = t.getPhoto();
		this.phone = t.getPhone();
		this.remark = t.getSignature();
		this.description = t.getDescription();
		this.tags = t.getTags();
		this.type = UserType.TEACHER;
		this.userId = t.getUserId();
		this.verified = t.isVerified();
	}
	
	public void bind(Teacher t, User user){
		this.bind(t);
		this.username = user.getUsername();
		this.password = user.getPassword();
	}
	
	public void bind(Parent p){
		this.name = p.getName();
		this.portrait = p.getPhoto();
		this.address = p.getLocation();
		this.phone = p.getPhone();
		this.remark = p.getSignature();
		this.type = UserType.PARENTS;
		this.userId = p.getUserId();
	}

	public void bind(Parent p, User user){
		this.bind(p);
		this.username = user.getUsername();
		this.password = user.getPassword();
	}
	
}
