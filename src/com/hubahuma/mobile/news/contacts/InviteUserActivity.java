package com.hubahuma.mobile.news.contacts;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.UserEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_invite_user)
public class InviteUserActivity extends Activity {

	public class CheckableUserEntity extends UserEntity {
		private boolean checked;

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

	}

	private List<String> groupList;
	private List<List<CheckableUserEntity>> childList;
	private InviteViewAdapter adapter;

	@ViewById
	ExpandableListView list;

	@AfterViews
	void init() {
		groupList = new ArrayList<String>();
		groupList.add("教师");
		groupList.add("家长");

		childList = getTestData();
		childList = new ArrayList<List<CheckableUserEntity>>(childList);

		adapter = new InviteViewAdapter(getApplicationContext(), groupList,
				childList);

		list.setAdapter(adapter);
		list.expandGroup(0);

		list.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.d("test", "GroupPosition is " + groupPosition);
				Log.d("test", "ChildPosition is" + childPosition);
				boolean checked = childList.get(groupPosition)
						.get(childPosition).isChecked();
				childList.get(groupPosition).get(childPosition)
						.setChecked(!checked);
				adapter.notifyDataSetChanged();
				return false;
			}
		});

		expandAllGroup();
	}

	private void expandAllGroup() {
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i, true);
		}
		list.setSelection(0);
	}

	@Click
	void btn_confirm() {

	}

	@Click
	void btn_back() {

		Intent intent = getIntent();
		intent.putExtra("result", "returned from ContactsActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	private List<List<CheckableUserEntity>> getTestData() {
		List<List<CheckableUserEntity>> result = new ArrayList<List<CheckableUserEntity>>();

		// 教师
		List<CheckableUserEntity> childList3 = new ArrayList<CheckableUserEntity>();
		for (int i = 1; i <= 4; i++) {
			CheckableUserEntity child1Data = new CheckableUserEntity();
			child1Data.setId("teacher#" + i);
			child1Data.setType(2);
			child1Data.setUsername("王萍" + i);
			child1Data.setRemark("北京市第" + i + "中学");
			child1Data.setChecked(false);
			childList3.add(child1Data);
		}
		result.add(childList3);

		// 家长
		List<CheckableUserEntity> childList4 = new ArrayList<CheckableUserEntity>();
		for (int i = 1; i <= 6; i++) {
			CheckableUserEntity child1Data = new CheckableUserEntity();
			child1Data.setId("user#" + i);
			child1Data.setType(3);
			child1Data.setUsername("李国成" + i);
			child1Data.setRemark("李小丽父亲" + i);
			child1Data.setChecked(false);
			childList4.add(child1Data);
		}
		result.add(childList4);

		return result;
	}

}
