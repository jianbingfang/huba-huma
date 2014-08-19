package com.hubahuma.mobile.news.managebook;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.ChatMsgEntity;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.ChatMsgEntity.MsgState;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_group_manage)
public class GroupManageActivity extends Activity {

	@ViewById(R.id.group_list_view)
	ListView list;

	private List<GroupEntity> groupList;

	private GroupManageViewAdapter adapter;

	@AfterViews
	void init() {

		groupList = getTestData();
		
		adapter = new GroupManageViewAdapter(getApplicationContext(), groupList);
		list.setAdapter(adapter);
		
		final GroupListViewGesture touchListener = new GroupListViewGesture(
				list, swipeListener, this, GroupListViewGesture.Dismiss);

		list.setOnTouchListener(touchListener);
		
	}

	@AfterTextChange(R.id.search_input)
	void afterTextChangedOnHelloTextView(Editable text) {

		String word = text.toString().trim();

		List<GroupEntity> tempList = new ArrayList<GroupEntity>();

		for (GroupEntity entity : groupList) {
			if (entity.getGroupName() != null
					&& entity.getGroupName().contains(word)) {
				tempList.add(entity);
			}
		}

		adapter = new GroupManageViewAdapter(getApplicationContext(), tempList);
		list.setAdapter(adapter);
		
	}

	@Click
	void btn_add() {
		// TODO 处理添加事件
	}

	GroupListViewGesture.TouchCallbacks swipeListener = new GroupListViewGesture.TouchCallbacks() {

		@Override
		public void OnClickDelete(int position) {
			// DO NOTHING!
			groupList.remove(position);
			adapter.notifyDataSetChanged();
			Toast.makeText(getApplicationContext(),
					"Deleted:" + position,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void OnClickRename(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(),
					"Rename:" + position, Toast.LENGTH_SHORT)
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
			for (int pos : reverseSortedPositions) {
				Log.d("DELETE", "[" + pos + "]");
				groupList.remove(pos);
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub
		}

	};
	
	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from GroupManageActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
	
	private List<GroupEntity> getTestData() {
		List<GroupEntity> tlist = new LinkedList<GroupEntity>();
		Random rand = new Random();
		for (int i = 1; i <= 5; i++) {
			GroupEntity group = new GroupEntity();
			group.setGroupName("师范第"+i+"小学"+rand.nextInt(10)+"班");
			group.setPopulation(rand.nextInt(100));
			group.setUserId(""+rand.nextInt(100000));
			tlist.add(group);
		}
		return tlist;
	}
}
