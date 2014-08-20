package com.hubahuma.mobile.entity;

import java.util.ArrayList;
import java.util.List;

public class GroupEntity {

	private String groupId;
	private String groupName;
	private UserEntity admin;
	private List<UserEntity> memberList;
	private int population;

	public GroupEntity() {
	}

	public GroupEntity(GroupEntity group) {
		this.groupId = group.getGroupId();
		this.groupName = group.getGroupName();
		this.admin = group.getAdmin();
		this.memberList = new ArrayList<UserEntity>(group.getMemberList());
		// this.population = group.getPopulation();
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String userId) {
		this.groupId = userId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<UserEntity> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<UserEntity> memberList) {
		this.memberList = memberList;
	}

	public UserEntity getAdmin() {
		return admin;
	}

	public void setAdmin(UserEntity admin) {
		this.admin = admin;
	}

	public int getPopulation() {
		if (memberList == null)
			return 0;
		else
			return memberList.size();
	}

}
