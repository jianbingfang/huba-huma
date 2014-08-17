package com.hubahuma.mobile.news.managebook;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_manage_book)
public class ManageBookActivity extends ExpandableListActivity {

	private ManageBookViewAdapter adapter;

	private List<Map<String, Object>> groupList;
	private List<List<Map<String, Object>>> childList;

	@ViewById
	ExpandableListView list;

	@AfterViews
	void init() {
		adapter = new ManageBookViewAdapter(getApplicationContext(), groupList,
				childList);
		setListAdapter(adapter);
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

}
