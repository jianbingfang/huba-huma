package com.hubahuma.mobile.news.managebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.net.Uri;
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
import com.hubahuma.mobile.news.managebook.ManageBookViewAdapter.PhoneOperationListener;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_manage_book)
public class ManageBookActivity extends ExpandableListActivity implements
		PhoneOperationListener {

	private ManageBookViewAdapter adapter;

	private List<Map<String, Object>> groupList;
	private List<List<Map<String, Object>>> childList;

	@ViewById
	ExpandableListView list;

	@ViewById
	EditText search_input;

	@AfterViews
	void init() {

		groupList = new ArrayList<Map<String, Object>>();
		childList = new ArrayList<List<Map<String, Object>>>();

		adapter = new ManageBookViewAdapter(getApplicationContext(), groupList,
				childList, this);

		setListAdapter(adapter);

		setTestData();

		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i, true);
		}
		list.setSelection(1);

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

		final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(
				list, swipeListener, this);
		touchListener.SwipeType = ListViewSwipeGesture.Double;

		list.setOnTouchListener(touchListener);

	}

	private void setTestData() {
		Map<String, Object> group1 = new HashMap<String, Object>();
		group1.put("title", "师范小学11级二年一班");
		groupList.add(group1);
		List<Map<String, Object>> child1 = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= 4; i++) {
			Map<String, Object> child1Data = new HashMap<String, Object>();
			child1Data.put("name", "王萍" + i);
			child1Data.put("remark", "赵林母亲" + i);
			child1.add(child1Data);
		}
		childList.add(child1);

		Map<String, Object> group2 = new HashMap<String, Object>();
		group2.put("title", "快乐城堡幼儿班09级");
		groupList.add(group2);
		List<Map<String, Object>> child2 = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= 6; i++) {
			Map<String, Object> child2Data = new HashMap<String, Object>();
			child2Data.put("name", "李国成" + i);
			child2Data.put("remark", "李小丽父亲" + i);
			child2.add(child2Data);
		}
		childList.add(child2);
	}

	ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void OnClickFirstItem(int position) {
			// TODO Auto-generated method stub
			long pos = list.getExpandableListPosition(position);
			int childPos = ExpandableListView.getPackedPositionChild(pos);// 获取第一行child的id
			int groupPos = ExpandableListView.getPackedPositionGroup(pos);// 获取第一行group的id
			Toast.makeText(getApplicationContext(),
					"Delete: g" + groupPos + "c" + childPos, Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void OnClickSecondItem(int position) {
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
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Delete",
					Toast.LENGTH_SHORT).show();
			for (int i : reverseSortedPositions) {
				Log.d("debug", "ATENTION:" + i);
				// childList.remove(i);
				adapter.notifyDataSetChanged();
			}
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
		// TODO
	}

	@Click
	void btn_back() {
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

}
