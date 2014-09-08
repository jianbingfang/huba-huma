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
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.contacts.SearchResultViewAdapter.onFollowListener;
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
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_search_result)
public class SearchResultActivity extends Activity implements onFollowListener {

	private SearchResultViewAdapter adapter;

	private List<UserEntity> userList;

	@ViewById
	ListView search_result_list;

	@AfterViews
	void init() {
		userList = getTestData();
		adapter = new SearchResultViewAdapter(getApplicationContext(),
				userList, this);
		search_result_list.setAdapter(adapter);
	}

	private List<UserEntity> getTestData() {
		List<UserEntity> list = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity user = new UserEntity();
			user.setId("teacher#" + i);
			user.setType(UserType.TEACHER);
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

	@Override
	public void onFollowClicked(UserEntity user) {
		// TODO 做后台真实处理
		Toast.makeText(getApplicationContext(), "成功关注 " + user.getUsername(),
				Toast.LENGTH_SHORT).show();
		// btn_back();
	}

}
