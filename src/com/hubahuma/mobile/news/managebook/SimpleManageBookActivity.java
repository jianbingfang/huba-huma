package com.hubahuma.mobile.news.managebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.managebook.ChangeGroupDialog.EditNameDialogListener;
import com.hubahuma.mobile.news.managebook.SimpleManageBookViewAdapter.SimpleManageBookListener;
import com.hubahuma.mobile.news.message.MessageActivity_;
import com.hubahuma.mobile.profile.ProfileOrganizationActivity_;
import com.hubahuma.mobile.profile.ProfileParentsActivity_;
import com.hubahuma.mobile.profile.ProfileTeacherActivity_;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_manage_book)
public class SimpleManageBookActivity extends FragmentActivity implements
		SimpleManageBookListener, EditNameDialogListener, PromptDialogListener {

	@RestService
	UserService userService;

	@App
	MyApplication myApp;

	@Pref
	SharedPrefs_ prefs;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private SimpleManageBookViewAdapter adapter;

	private List<GroupEntity> groupList;
	private List<GroupEntity> filteredGroupList;
	private int targetGroupPos = -1;
	private int targetChildPos = -1;

	@ViewById
	ExpandableListView list;
	@ViewById(R.id.search_input)
	EditText searchBox;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);

		RestTemplate tpl = userService.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);
		tpl.setRequestFactory(s);

	}

	@AfterViews
	void init() {

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		groupList = new ArrayList<GroupEntity>();
		setTestData();

		Collections.sort(groupList.get(1).getMemberList());

		filteredGroupList = new ArrayList<GroupEntity>(groupList);

		adapter = new SimpleManageBookViewAdapter(getApplicationContext(),
				filteredGroupList, this);

		list.setAdapter(adapter);
		list.expandGroup(0);
		expandAllGroup();

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

		final SimpleManageBookListViewGesture touchListener = new SimpleManageBookListViewGesture(
				list, swipeListener, this, 1);
		touchListener.itemNum = 1;

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
				if (user.getName() != null
						&& user.getName().contains(word)) {
					userList.add(user);
				} else if (user.getRemark() != null
						&& user.getRemark().contains(word)) {
					userList.add(user);
				}
			}

			tempGroup.setMemberList(userList);
			filteredGroupList.add(tempGroup);

		}

		adapter.notifyDataSetChanged();

		expandAllGroup();
	}

	@UiThread
	void showLoadingDialog() {
		loadingDialog.show(getSupportFragmentManager(), "dialog_loading");
	}

	@UiThread
	void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	@UiThread
	void showPromptDialog(String title, String content) {
		promptDialog.setTitle(title);
		promptDialog.setContent(content);
		promptDialog.show(getSupportFragmentManager(), "dialog_prompt");
	}

	@UiThread
	void dismissPromptDialog() {
		promptDialog.dismiss();
	}

	SimpleManageBookListViewGesture.TouchCallbacks swipeListener = new SimpleManageBookListViewGesture.TouchCallbacks() {

		@Override
		public void OnClickChangeGroup(int position) {
			// TODO Auto-generated method stub
			long pos = list.getExpandableListPosition(position);
			int childPos = ExpandableListView.getPackedPositionChild(pos);// 获取第一行child的id
			int groupPos = ExpandableListView.getPackedPositionGroup(pos);// 获取第一行group的id
			Log.d("Change group", "target: g" + groupPos + "c" + childPos);

			targetGroupPos = groupPos;
			targetChildPos = childPos;

			FragmentManager fm = getSupportFragmentManager();
			ChangeGroupDialog_ changeGroupDialog = new ChangeGroupDialog_();
			changeGroupDialog.show(fm, "dialog_change_group");
		}

		@Override
		public void LoadDataForScroll(int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onDelete(ListView listView, int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				long pos = list.getExpandableListPosition(position);
				int childPos = ExpandableListView.getPackedPositionChild(pos);// 获取第一行child的id
				int groupPos = ExpandableListView.getPackedPositionGroup(pos);// 获取第一行group的id
				Log.d("DELETE", "pos=" + pos + " -> [" + groupPos + ","
						+ childPos + "]");

				// handle delete contact

				UserEntity deletedUser = filteredGroupList.get(groupPos)
						.getMemberList().remove(childPos);
				groupList.get(groupPos).getMemberList().remove(deletedUser);

				Toast.makeText(getApplicationContext(),
						"已删除：" + deletedUser.getName(), Toast.LENGTH_SHORT)
						.show();
				adapter.notifyDataSetChanged();

			}
			return true;
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
		Intent intent = new Intent(Intent.ACTION_DIAL, callToUri);
		startActivity(intent);
	}

	private void expandAllGroup() {
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i, true);
		}

		list.setSelectedChild(0, 0, true);
	}

	@Override
	public void onFinishChangeGroupDialog(String text) {

		String groupName = text.trim();

		int pos = 0;
		for (; pos < filteredGroupList.size(); pos++) {
			if (filteredGroupList.get(pos).getGroupName().equals(groupName))
				break;
		}

		if (pos >= filteredGroupList.size()) {
			Toast.makeText(getApplicationContext(),
					"不存在此分组，请确认分组名称正确" + groupName, Toast.LENGTH_LONG).show();
			return;
		} else {
			if (pos == targetGroupPos) {
				Toast.makeText(getApplicationContext(), "分组与之前一致" + groupName,
						Toast.LENGTH_LONG).show();
				return;
			} else {
				UserEntity user = filteredGroupList.get(targetGroupPos)
						.getMemberList().remove(targetChildPos);
				filteredGroupList.get(pos).getMemberList().add(user);
				for (GroupEntity group : groupList) {
					if (group.getMemberList().contains(user)) {
						group.getMemberList().remove(user);
					} else if (group.getGroupName().equals(groupName)) {
						group.getMemberList().add(user);
					}
				}
				adapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "分组更改成功",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void ignoreReceipt(Button ignore, Button accept) {
		// showLoadingDialog();
		ignore.setVisibility(View.GONE);
		accept.setText("已忽略");
		accept.setEnabled(false);
		accept.setBackgroundResource(R.drawable.btn_grey_bg);
	}

	@Override
	public void acceptReceipt(Button ignore, Button accept) {
		// TODO Auto-generated method stub

		UserEntity user = (UserEntity) accept.getTag();
		groupList.get(1).getMemberList().add(user);
		Collections.sort(groupList.get(1).getMemberList());
//		filteredGroupList.get(1).getMemberList().add(user);
//		Collections.sort(filteredGroupList.get(1).getMemberList());
		adapter.notifyDataSetChanged();

		ignore.setVisibility(View.GONE);
		accept.setText("已通过");
		accept.setEnabled(false);
		accept.setBackgroundResource(R.drawable.btn_dark_green_bg);

	}

	@Override
	public void onPortraitClick(UserEntity user) {
		Intent intent = new Intent();
		intent.putExtra("user", user);

		switch (user.getType()) {
		case UserType.ORGANIZTION:
			intent.setClass(SimpleManageBookActivity.this,
					ProfileOrganizationActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_ORGANIZATION_ACTIVITY);
			break;

		case UserType.TEACHER:
			intent.setClass(SimpleManageBookActivity.this,
					ProfileTeacherActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_TEACHER_ACTIVITY);
			break;

		case UserType.PARENTS:
			intent.setClass(SimpleManageBookActivity.this,
					ProfileParentsActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_PARENTS_ACTIVITY);
			break;

		}
	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
	}

	private void setTestData() {
		GroupEntity toVerify = new GroupEntity();
		toVerify.setGroupName("待验证家长");
		List<UserEntity> childList1 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity child1Data = new UserEntity();
			child1Data.setUserId("user#" + i);
			child1Data.setType(UserType.PARENTS);
			child1Data.setName("王萍" + i);
			child1Data.setRemark("赵林母亲" + i);
			childList1.add(child1Data);
		}
		toVerify.setMemberList(childList1);
		groupList.add(toVerify);

		GroupEntity verified = new GroupEntity();
		verified.setGroupName("通过验证本班家长");
		List<UserEntity> childList2 = new ArrayList<UserEntity>();
		for (int i = 1; i <= 6; i++) {
			UserEntity child2Data = new UserEntity();
			child2Data.setUserId("user#" + i * 10);
			child2Data.setType(UserType.PARENTS);
			child2Data.setName("李国成" + i);
			child2Data.setRemark("李小丽父亲" + i);
			childList2.add(child2Data);
		}
		verified.setMemberList(childList2);
		groupList.add(verified);
	}

}
