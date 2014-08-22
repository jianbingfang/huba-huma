package com.hubahuma.mobile.news.contacts;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.NoTitle;
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
import android.widget.ListView;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_search_result)
public class SearchResultActivity extends Activity {

	private SearchResultViewAdapter adapter;

	private List<UserEntity> userList;

	@ViewById
	ListView search_result_list;

	@AfterViews
	void init() {
		userList = getTestData();
		adapter = new SearchResultViewAdapter(getApplicationContext(), userList);
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

		Intent intent = getIntent();
		intent.putExtra("result", "returned from SearchResultActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

}
