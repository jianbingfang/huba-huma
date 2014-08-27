package com.hubahuma.mobile.news.contacts;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.managebook.ManageBookViewAdapter;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_contacts)
public class ContactsActivity extends FragmentActivity {

	private List<String> groupList;
	private List<List<UserEntity>> childList;
	private List<List<UserEntity>> filteredChildList;

	private ContactsViewAdapter adapter;

	@ViewById
	ExpandableListView list;
	
	@ViewById(R.id.search_input)
	EditText searchBox;

	@AfterViews
	void init() {
		groupList = new ArrayList<String>();
		groupList.add("群");
		groupList.add("学校");
		groupList.add("教师");
		groupList.add("家长");

		childList = getTestData();
		filteredChildList = new ArrayList<List<UserEntity>>(childList);

		adapter = new ContactsViewAdapter(getApplicationContext(), groupList,
				filteredChildList);

		list.setAdapter(adapter);
		expandAllGroup();

		list.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.d("test", "GroupPosition is " + groupPosition);
				Log.d("test", "ChildPosition is" + childPosition);
				return false;
			}
		});
	}

	@AfterTextChange(R.id.search_input)
	void afterTextChangedOnHelloTextView(Editable text) {

		String word = text.toString().trim();
		filteredChildList.clear();

		for (List<UserEntity> group : childList) {
			List<UserEntity> userList = new ArrayList<UserEntity>();
			for (UserEntity user : group) {
				if (user.getUsername() != null
						&& user.getUsername().contains(word)) {
					userList.add(user);
				}
			}

			filteredChildList.add(userList);

		}

		adapter.notifyDataSetChanged();

		expandAllGroup();
	}

	private List<List<UserEntity>> getTestData() {
		List<List<UserEntity>> result = new ArrayList<List<UserEntity>>();

		//群
		List<UserEntity> childList1 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity child1Data = new UserEntity();
			child1Data.setId("group#" + i);
			child1Data.setType(0);
			child1Data.setUsername("快乐城堡儿童英语群" + i);
			child1Data.setRemark(i*10+"人");
			childList1.add(child1Data);
		}
		result.add(childList1);

		// 学校
		List<UserEntity> childList2 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 1; i++) {
			UserEntity child1Data = new UserEntity();
			child1Data.setId("school#" + i);
			child1Data.setType(1);
			child1Data.setUsername("快乐城堡儿童英语");
			child1Data.setRemark("");
			childList2.add(child1Data);
		}
		result.add(childList2);
		
		// 教师
		List<UserEntity> childList3 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity child1Data = new UserEntity();
			child1Data.setId("teacher#" + i);
			child1Data.setType(2);
			child1Data.setUsername("王萍" + i);
			child1Data.setRemark("北京市第" + i + "中学");
			childList3.add(child1Data);
		}
		result.add(childList3);
		
		// 家长
		List<UserEntity> childList4 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 6; i++) {
			UserEntity child1Data = new UserEntity();
			child1Data.setId("user#" + i);
			child1Data.setType(3);
			child1Data.setUsername("李国成" + i);
			child1Data.setRemark("李小丽父亲" + i);
			childList4.add(child1Data);
		}
		result.add(childList4);
		
		return result;
	}
	
	@Click
	void btn_add(){
		Intent intent = new Intent();
		intent.setClass(this, AddContactActivity_.class);
		startActivityForResult(intent, ActivityCode.ADD_CONTACT_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.ADD_CONTACT_ACTIVITY)
	void onTeachingDiaryActivityResult(int resultCode, Intent data) {
		searchBox.setText("");
	}
	
	@Click
	void btn_back() {

		String str = searchBox.getText().toString().trim();
		if (!UtilTools.isEmpty(str)) {
			searchBox.getText().clear();
			return;
		}

		Intent intent = getIntent();
		intent.putExtra("result", "returned from ContactsActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	private void expandAllGroup() {
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i, true);
		}
		list.setSelectedChild(0, 0, true);
	}
}
