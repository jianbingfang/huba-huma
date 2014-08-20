package com.hubahuma.mobile.news.managebook;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.GroupEntity;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_group_manage)
public class GroupManageActivity extends Activity {

	@ViewById(R.id.group_list_view)
	ListView list;

	private List<GroupEntity> groupList;
	private List<GroupEntity> filteredList;

	private GroupManageViewAdapter adapter;

	@AfterViews
	void init() {

		groupList = getTestData();
		filteredList = new ArrayList<GroupEntity>(groupList);
		
		adapter = new GroupManageViewAdapter(getApplicationContext(), filteredList);
		list.setAdapter(adapter);
		
		final GroupListViewGesture touchListener = new GroupListViewGesture(
				list, swipeListener, this, GroupListViewGesture.Double);

		list.setOnTouchListener(touchListener);
		
	}

	@AfterTextChange(R.id.search_input)
	void afterTextChangedOnHelloTextView(Editable text) {

		String word = text.toString().trim();

		filteredList.clear();;

		for (GroupEntity entity : groupList) {
			if (entity.getGroupName() != null
					&& entity.getGroupName().contains(word)) {
				filteredList.add(entity);
			}
		}

		adapter = new GroupManageViewAdapter(getApplicationContext(), filteredList);
		list.setAdapter(adapter);
		
	}

	@Click
	void btn_add() {
		// TODO 处理添加事件
	}

	GroupListViewGesture.TouchCallbacks swipeListener = new GroupListViewGesture.TouchCallbacks() {
		
		@Override
		public void onDeleteItem(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Delete",
					Toast.LENGTH_SHORT).show();
			for (int pos : reverseSortedPositions) {
				Log.d("DELETE", "[" + pos + "]");
				GroupEntity deletedGroup = filteredList.remove(pos);
				adapter.notifyDataSetChanged();
				groupList.remove(deletedGroup);
			}
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
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub
			Log.d("Clicked", "list-[" + position + "]");
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
		for (int i = 1; i <= 10; i++) {
			GroupEntity group = new GroupEntity();
			group.setGroupName("师范第"+i+"小学"+rand.nextInt(10)+"班");
			group.setPopulation(rand.nextInt(100));
			group.setGroupId(""+rand.nextInt(100000));
			tlist.add(group);
		}
		return tlist;
	}
}
