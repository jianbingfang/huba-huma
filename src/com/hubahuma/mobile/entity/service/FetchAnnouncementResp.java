package com.hubahuma.mobile.entity.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.hubahuma.mobile.entity.NoticeEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FetchAnnouncementResp {

	@JsonProperty("announcements")
	private List<NoticeEntity> noticeList;

	private boolean hasMore;

	public List<NoticeEntity> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<NoticeEntity> noticeList) {
		this.noticeList = noticeList;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

}
