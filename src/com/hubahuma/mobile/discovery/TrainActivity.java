package com.hubahuma.mobile.discovery;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.discovery.TrainOrgListViewAdapter.TrainOrgListViewListener;
import com.hubahuma.mobile.discovery.view.ExpandTabView;
import com.hubahuma.mobile.discovery.view.ViewChooseTag;
import com.hubahuma.mobile.discovery.view.ViewRegion;
import com.hubahuma.mobile.discovery.view.ViewSort;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.managebook.GroupListViewGesture;
import com.hubahuma.mobile.news.managebook.GroupManageViewAdapter;
import com.hubahuma.mobile.news.managebook.GroupRenameDialog_;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryViewAdapter;
import com.hubahuma.mobile.profile.ProfileOrganizationActivity_;
import com.hubahuma.mobile.profile.ProfileParentsActivity_;
import com.hubahuma.mobile.profile.ProfileTeacherActivity_;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_train)
public class TrainActivity extends Activity implements TrainOrgListViewListener {

	private ArrayList<View> mViewArray = new ArrayList<View>();
	private ViewSort viewSort;
	private ViewRegion viewRegion;
	private ViewChooseTag viewChooseTag;

	private TrainOrgListViewAdapter adapter;

	private List<UserEntity> dataList;

	@ViewById
	ListView list;

	@ViewById(R.id.expandtab_view)
	ExpandTabView expandTabView;

	@ViewById
	ProgressBar progress_bar;

	@AfterViews
	void init() {
		initView();
		initVaule();
		initListener();
		preProcess();
		loadData();
	}

	@Background(delay = 500)
	void loadData() {
		dataList = getTestData();
		postProcess();
	}

	private List<UserEntity> getTestData() {
		// 学校
		List<UserEntity> list = new ArrayList<UserEntity>();
		for (int i = 1; i <= 10; i++) {
			UserEntity data = new UserEntity();
			data.setId("school#" + i);
			data.setType(UserType.ORGANIZTION);
			data.setUsername("快乐城堡儿童英语");
			data.setRemark("");
			list.add(data);
		}
		return list;
	}

	@UiThread
	void preProcess() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postProcess() {
		adapter = new TrainOrgListViewAdapter(getApplicationContext(),
				dataList, this);
		list.setAdapter(adapter);

		/*final TrainOrgListViewGesture touchListener = new TrainOrgListViewGesture(
				list, swipeListener, this, 4);
		list.setOnTouchListener(touchListener);*/
		
		adapter.notifyDataSetChanged();
		progress_bar.setVisibility(View.GONE);
	}

	private void initView() {
		viewRegion = new ViewRegion(this);
		viewSort = new ViewSort(this);
		viewChooseTag = new ViewChooseTag(this);

	}

	private void initVaule() {
		mViewArray.add(viewRegion);
		mViewArray.add(viewSort);
		mViewArray.add(viewChooseTag);
		ArrayList<String> mTextArray = new ArrayList<String>();
		mTextArray.add("全城");
		mTextArray.add("排序");
		mTextArray.add("选择标签");
		expandTabView.setValue(mTextArray, mViewArray);
		expandTabView.setTitle(viewRegion.getShowText(), 0);
		expandTabView.setTitle(viewSort.getShowText(), 1);
		expandTabView.setTitle(viewChooseTag.getShowText(), 2);
	}

	private void initListener() {

		viewSort.setOnSelectListener(new ViewSort.OnSelectListener() {
			@Override
			public void getValue(String distance, String showText) {
				onRefresh(viewSort, showText);
			}
		});

		viewRegion.setOnSelectListener(new ViewRegion.OnSelectListener() {
			@Override
			public void getValue(String showText) {
				onRefresh(viewRegion, showText);
			}
		});

		viewChooseTag.setOnSelectListener(new ViewChooseTag.OnSelectListener() {
			@Override
			public void getValue(String distance, String showText) {
				onRefresh(viewChooseTag, showText);
			}
		});

	}

	private void onRefresh(View view, String showText) {

		expandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
			expandTabView.setTitle(showText, position);
		}
		Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();

	}

	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}

	@Click
	void btn_back() {
		if (!expandTabView.onPressBack()) {
			Intent intent = getIntent();
			intent.putExtra("result", "returned from TrainActivity");
			this.setResult(0, intent);
			this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	TrainOrgListViewGesture.TouchCallbacks swipeListener = new TrainOrgListViewGesture.TouchCallbacks() {

		@Override
		public void onDeleteItem(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			for (int pos : reverseSortedPositions) {
				Log.d("DELETE", "[" + pos + "]");
				if (pos >= dataList.size() || pos < 0) {
					Log.d("delete group", "删除位置越界:pos=" + pos);
					continue;
				}
				dataList.remove(pos);
				adapter.notifyDataSetChanged();
			}
			Toast.makeText(getApplicationContext(), "分组已删除", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void OnClickRename(int position) {

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

	@Override
	public void onPortraitClick(UserEntity user) {
		Intent intent = new Intent();
		intent.putExtra("user", user);

		switch (user.getType()) {
		case UserType.ORGANIZTION:
			intent.setClass(this, ProfileOrganizationActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_ORGANIZATION_ACTIVITY);
			break;

		case UserType.TEACHER:
			intent.setClass(this, ProfileTeacherActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_TEACHER_ACTIVITY);
			break;

		case UserType.PARENTS:
			intent.setClass(this, ProfileParentsActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_PARENTS_ACTIVITY);
			break;

		}

	}
}
