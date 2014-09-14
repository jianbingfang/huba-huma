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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.news.managebook.AddNewGroupDialog.AddGroupDialogListener;
import com.hubahuma.mobile.news.managebook.OneInputDialog.OnOneInputDialogConfirmListener;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_group_manage)
public class GroupManageActivity extends FragmentActivity implements
		OnOneInputDialogConfirmListener, AddGroupDialogListener {

	@ViewById(R.id.group_list_view)
	ListView list;
	@ViewById(R.id.search_input)
	EditText searchBox;

	private List<GroupEntity> groupList;
	private List<GroupEntity> filteredList;

	private int targetPos = -1;

	private GroupManageViewAdapter adapter;

	@AfterViews
	void init() {

		groupList = getTestData();
		filteredList = new ArrayList<GroupEntity>(groupList);

		adapter = new GroupManageViewAdapter(getApplicationContext(),
				filteredList);
		list.setAdapter(adapter);

		final GroupListViewGesture touchListener = new GroupListViewGesture(
				list, swipeListener, this, GroupListViewGesture.Double);

		list.setOnTouchListener(touchListener);

	}

	@AfterTextChange(R.id.search_input)
	void afterTextChangedOnHelloTextView(Editable text) {

		String word = text.toString().trim();

		filteredList.clear();

		for (GroupEntity entity : groupList) {
			if (entity.getGroupName() != null
					&& entity.getGroupName().contains(word)) {
				filteredList.add(entity);
			}
		}

		adapter.notifyDataSetChanged();

	}

	@Click
	void btn_add() {
		FragmentManager fm = getSupportFragmentManager();
		AddNewGroupDialog_ addGroupDialog = new AddNewGroupDialog_();
		addGroupDialog.show(fm, "dialog_add_group");
	}

	GroupListViewGesture.TouchCallbacks swipeListener = new GroupListViewGesture.TouchCallbacks() {

		@Override
		public void onDeleteItem(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			for (int pos : reverseSortedPositions) {
				Log.d("DELETE", "[" + pos + "]");
				if (pos >= filteredList.size() || pos < 0) {
					Log.d("delete group", "删除位置越界:pos=" + pos);
					continue;
				}
				GroupEntity deletedGroup = filteredList.remove(pos);
				if (!groupList.remove(deletedGroup)) {
					filteredList.add(pos, deletedGroup);
					Log.d("delete group",
							"原list删除失败：group-" + deletedGroup.getGroupName()
									+ "，删除操作撤销。");
					continue;
				}
				adapter.notifyDataSetChanged();
			}
			Toast.makeText(getApplicationContext(), "分组已删除", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void OnClickRename(int position) {
			targetPos = position;
			FragmentManager fm = getSupportFragmentManager();
			OneInputDialog_ changeGroupDialog = new OneInputDialog_();
			changeGroupDialog.show(fm, "dialog_group_rename");
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

		String str = searchBox.getText().toString().trim();
		if (!UtilTools.isEmpty(str)) {
			searchBox.getText().clear();
			return;
		}

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
			group.setGroupName("师范第" + i + "小学" + rand.nextInt(10) + "班");
			group.setGroupId("" + rand.nextInt(100000));
			tlist.add(group);
		}
		return tlist;
	}

	@Override
	public void onDialogConfirm(String groupName) {

		int pos = 0;
		for (; pos < filteredList.size(); pos++) {
			if (filteredList.get(pos).getGroupName().equals(groupName))
				break;
		}

		if (pos == targetPos) {
			Toast.makeText(getApplicationContext(), "名称未发生变化",
					Toast.LENGTH_LONG).show();
			return;
		} else if (pos < filteredList.size()) {
			Toast.makeText(getApplicationContext(), "该名称已存在，请重新改名",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			GroupEntity g = filteredList.get(targetPos);
			g.setGroupName(groupName);
			groupList.get(groupList.indexOf(g)).setGroupName(groupName);
			adapter.notifyDataSetChanged();
			Toast.makeText(getApplicationContext(), "名称更改成功",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onFinishAddGroupDialog(String groupName) {
		// TODO Auto-generated method stub
		for (GroupEntity group : groupList) {
			if (group.getGroupName().equals(groupName)) {
				Toast.makeText(getApplicationContext(), "该名称已存在，请更换名称",
						Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		GroupEntity group = new GroupEntity();
		//TODO 加入完整的组信息
		group.setGroupName(groupName);
		
		groupList.add(group);
		filteredList.add(group);
		adapter.notifyDataSetChanged();
	}
}
