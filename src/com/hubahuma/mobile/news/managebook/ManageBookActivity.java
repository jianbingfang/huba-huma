package com.hubahuma.mobile.news.managebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.NewsActivity.ActivityCode;
import com.hubahuma.mobile.news.managebook.ManageBookViewAdapter.PhoneOperationListener;
import com.hubahuma.mobile.news.message.MessageActivity_;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_manage_book)
public class ManageBookActivity extends ExpandableListActivity implements
		PhoneOperationListener {

	private ManageBookViewAdapter adapter;

	private List<GroupEntity> groupList;
	private List<GroupEntity> filteredGroupList;

	@ViewById
	ExpandableListView list;
	@ViewById(R.id.search_input)
	EditText searchBox;

	@AfterViews
	void init() {

		groupList = new ArrayList<GroupEntity>();
		setTestData();
		filteredGroupList = new ArrayList<GroupEntity>(groupList);

		adapter = new ManageBookViewAdapter(getApplicationContext(),
				filteredGroupList, this);

		setListAdapter(adapter);
		list.expandGroup(0);

		// 设置监听器
		list.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.d("test", "GroupPosition is " + groupPosition);
				Log.d("test", "ChildPosition is" + childPosition);
				return false;
			}
		});

		final ManageBookListViewGesture touchListener = new ManageBookListViewGesture(
				list, swipeListener, this, 2);
		touchListener.itemNum = 2;

		list.setOnTouchListener(touchListener);

	}

	@AfterTextChange(R.id.search_input)
	void afterTextChangedOnHelloTextView(Editable text) {

		String word = text.toString().trim();
		filteredGroupList.clear();

		for (GroupEntity group : groupList) {
			GroupEntity tempGroup = new GroupEntity(group);
			tempGroup.getMemberList().clear();
			List<UserEntity> userList = new ArrayList<UserEntity>();
			for (UserEntity user : group.getMemberList()) {
				if (user.getUsername() != null
						&& user.getUsername().contains(word)) {
					userList.add(user);
				} else if (user.getRemark() != null
						&& user.getRemark().contains(word)) {
					userList.add(user);
				}
			}

			if (!userList.isEmpty()) {
				tempGroup.setMemberList(userList);
				filteredGroupList.add(tempGroup);
			}

		}

		adapter.notifyDataSetChanged();

		expandAllGroup();
	}

	private void setTestData() {
		GroupEntity group1 = new GroupEntity();
		group1.setGroupName("师范小学11级二年一班");
		List<UserEntity> childList1 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity child1Data = new UserEntity();
			child1Data.setId("user#" + i);
			child1Data.setType(0);
			child1Data.setUsername("王萍" + i);
			child1Data.setRemark("赵林母亲" + i);
			childList1.add(child1Data);
		}
		group1.setMemberList(childList1);
		groupList.add(group1);

		GroupEntity group2 = new GroupEntity();
		group2.setGroupName("快乐城堡幼儿班09级");
		List<UserEntity> childList2 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 6; i++) {
			UserEntity child2Data = new UserEntity();
			child2Data.setId("user#" + i * 10);
			child2Data.setType(0);
			child2Data.setUsername("李国成" + i);
			child2Data.setRemark("李小丽父亲" + i);
			childList2.add(child2Data);
		}
		group2.setMemberList(childList2);
		groupList.add(group2);
	}

	ManageBookListViewGesture.TouchCallbacks swipeListener = new ManageBookListViewGesture.TouchCallbacks() {

		@Override
		public void OnClickManageGroup(int position) {
			// TODO Auto-generated method stub
			long pos = list.getExpandableListPosition(position);
			int childPos = ExpandableListView.getPackedPositionChild(pos);// 获取第一行child的id
			int groupPos = ExpandableListView.getPackedPositionGroup(pos);// 获取第一行group的id
			Toast.makeText(getApplicationContext(),
					"Manage: g" + groupPos + "c" + childPos, Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void LoadDataForScroll(int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDelete(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Delete",
					Toast.LENGTH_SHORT).show();
			for (int pos : reverseSortedPositions) {
				int childPos = ExpandableListView.getPackedPositionChild(pos);// 获取第一行child的id
				int groupPos = ExpandableListView.getPackedPositionGroup(pos);// 获取第一行group的id
				Log.d("DELETE", "pos="+pos+" -> [" + groupPos + "," + childPos + "]");
				UserEntity deletedUser = filteredGroupList.get(groupPos)
						.getMemberList().remove(childPos);
				for (GroupEntity group : groupList) {
					if (group.getMemberList().remove(deletedUser))
						break;
				}
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub
		}

	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}

	@Click
	void btn_group_manage() {
		Intent intent = new Intent();
		intent.setClass(this, GroupManageActivity_.class);
		startActivityForResult(intent, ActivityCode.GROUP_MANAGE_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.GROUP_MANAGE_ACTIVITY)
	void onGroupManageActivityResult(int resultCode, Intent data) {

	}

	@Click
	void btn_back() {

		String str = searchBox.getText().toString().trim();
		if (!UtilTools.isEmpty(str)) {
			searchBox.getText().clear();
			return;
		}

		Intent intent = getIntent();
		intent.putExtra("result", "returned from ManageBookActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	@Override
	public void sendSMS(String phoneNum, String smsContent) {
		Uri smsToUri = Uri.parse("smsto:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsContent);
		startActivity(intent);
	}

	@Override
	public void phoneCall(String phoneNum) {
		Uri callToUri = Uri.parse("tel:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_CALL, callToUri);
		startActivity(intent);
	}

	private void expandAllGroup() {
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i, true);
		}
		list.setSelection(0);
	}

}
