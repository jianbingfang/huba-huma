package com.hubahuma.mobile.news.contacts;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.contacts.InviteViewAdapter.OnUserCheckedListener;
import com.hubahuma.mobile.news.message.ChatActivity_;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_invite_user)
public class InviteUserActivity extends Activity implements
		OnUserCheckedListener {

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
	private List<List<UserEntity>> childList;
	private InviteViewAdapter adapter;
	private List<UserEntity> invitedList;
	private LayoutInflater mInflater;
	
	@Extra
	String groupName;

	@ViewById
	ExpandableListView list;

	@ViewById
	Button btn_confirm;
	
	@ViewById
	LinearLayout invited_list;

	@AfterViews
	void init() {
		
		mInflater = LayoutInflater.from(this);
		
		invitedList = new ArrayList<UserEntity>();

		groupList = new ArrayList<String>();
		groupList.add("教师");
		groupList.add("家长");

		childList = getTestData();
		childList = new ArrayList<List<UserEntity>>(childList);

		adapter = new InviteViewAdapter(getApplicationContext(), groupList,
				childList, this);

		list.setAdapter(adapter);
		list.expandGroup(0);

		list.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.d("test", "GroupPosition is " + groupPosition);
				Log.d("test", "ChildPosition is" + childPosition);
				// boolean checked = childList.get(groupPosition)
				// .get(childPosition).isChecked();
				// childList.get(groupPosition).get(childPosition)
				// .setChecked(!checked);
				// adapter.notifyDataSetChanged();
				return false;
			}
		});

		expandAllGroup();
	}

	private void expandAllGroup() {
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i, true);
		}
		list.setSelectedChild(0, 0, true);
	}

	@Click
	void btn_confirm() {

		// List<UserEntity> invitedList = new ArrayList<UserEntity>();
		//
		// for (List<UserEntity> subList : childList) {
		// for (UserEntity user : subList) {
		// if (user.isChecked())
		// invitedList.add(user);
		// }
		// }

		if (invitedList.isEmpty()) {
			Toast.makeText(getApplicationContext(), "您还未选择被邀请用户",
					Toast.LENGTH_SHORT);
			return;
		} else {
			handleAddCharGroupMember(groupName, invitedList);
		}
	}

	@Background
	void handleAddCharGroupMember(String groupName, List<UserEntity> invitedList) {
		// TODO 与后台做实际通信
		Intent intent = new Intent();
		intent.putExtra("name", groupName);
		intent.setClass(this, ChatActivity_.class);
		startActivityForResult(intent, ActivityCode.CHAT_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.CHAT_ACTIVITY)
	void onInviteUerActivityResult(int resultCode, Intent data) {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from InviteUserActivity");
		this.setResult(0, intent);
		this.finish();
		Log.d("Return", "return from ChatActivity");
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from InviteUserActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	private List<List<UserEntity>> getTestData() {
		List<List<UserEntity>> result = new ArrayList<List<UserEntity>>();

		// 教师
		List<UserEntity> childList3 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity child1Data = new UserEntity();
			child1Data.setId("teacher#" + i);
			child1Data.setType(2);
			child1Data.setUsername("王萍" + i);
			child1Data.setRemark("北京市第" + i + "中学");
			// child1Data.setChecked(false);
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
			// child1Data.setChecked(false);
			childList4.add(child1Data);
		}
		result.add(childList4);

		return result;
	}

	@Override
	public void onUserCheckedChanged(UserEntity user, boolean isChecked) {
		if (isChecked) {
			invitedList.add(user);
		} else {
			invitedList.remove(user);
		}
		updateInvitedUserView();
	}

	private void updateInvitedUserView() {
		invited_list.removeAllViewsInLayout();;
		for (UserEntity user : invitedList) {
			View view = mInflater.inflate(R.layout.item_footbar_invited_user,
					invited_list, false);
			ImageView img = (ImageView) view
					.findViewById(R.id.portrait);
			// TODO 获取真实头像
			img.setImageResource(R.drawable.default_portrait);
			invited_list.addView(view);
		}
	}

}
