package com.hubahuma.mobile.news.contacts;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.managebook.GroupManageViewAdapter;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

@EFragment(R.layout.fragment_search_result)
public class SearchResultFragment extends Fragment {

	private SearchResultViewAdapter adapter;

	private List<UserEntity> userList;

	@ViewById
	ExpandableListView search_result_list;

	@AfterViews
	void init() {
		userList = getTestData();
		adapter = new SearchResultViewAdapter(getActivity(), userList);
		search_result_list.setAdapter(adapter);
	}

	private List<UserEntity> getTestData() {
		List<UserEntity> list = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity user = new UserEntity();
			user.setId("teacher#" + i);
			user.setType(2);
			user.setUsername("王萍" + i);
			user.setRemark("北京市第" + i + "中学");
			list.add(user);
		}
		return list;
	}

	@Click
	void btn_back() {
		this.getActivity().onBackPressed();
	}

}
