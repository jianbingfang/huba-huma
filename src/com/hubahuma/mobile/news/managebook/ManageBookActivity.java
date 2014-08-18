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
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.hubahuma.mobile.R;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_manage_book)
public class ManageBookActivity extends ExpandableListActivity {

	private ManageBookViewAdapter adapter;

	private List<Map<String, Object>> groupList;
	private List<List<Map<String, Object>>> childList;

	@ViewById
	ExpandableListView list;

	@ViewById
	EditText search_input;

	@AfterViews
	void init() {

		closeInputMethod();
		
		groupList = new ArrayList<Map<String, Object>>();
		childList = new ArrayList<List<Map<String, Object>>>();

		adapter = new ManageBookViewAdapter(getApplicationContext(), groupList,
				childList);
		setListAdapter(adapter);

		setTestData();

		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i, true);
		}
		list.setSelection(0);

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

	private void closeInputMethod() {
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
	        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
}
