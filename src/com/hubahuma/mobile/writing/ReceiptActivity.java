package com.hubahuma.mobile.writing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.utils.GlobalVar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

@NoTitle
@EActivity(R.layout.activity_receipt)
public class ReceiptActivity extends Activity {

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	TextView read_info, unread_info;

	@ViewById
	ListView read_list, unread_list;

	private List<UserEntity> readDataList;

	private List<UserEntity> unreadDataList;

	private ReceiptListViewAdapter readAdapter;

	private ReceiptListViewAdapter unreadAdapter;

	@AfterViews
	void init() {
		read_info.setText("");
		unread_info.setText("");
		preLoadData();
		loadData();
	}

	@Background(delay = 1000)
	void loadData() {
		readDataList = getTestData();
		unreadDataList = getTestData();
		postLoadData();
	}

	private List<UserEntity> getTestData() {

		Random rand = new Random();
		List<UserEntity> testData = new ArrayList<UserEntity>();
		for (int i = 1; i <= rand.nextInt(20); i++) {
			UserEntity item = new UserEntity();
			item.setId("0000" + i);
			item.setType(UserType.PARENTS);
			item.setUsername("高健" + i);
			item.setRemark("高晓东" + i + "爸爸");
			testData.add(item);
		}
		return testData;
	}

	@UiThread
	void preLoadData() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postLoadData() {

		readAdapter = new ReceiptListViewAdapter(getApplicationContext(),
				readDataList, true);
		read_list.setAdapter(readAdapter);
		readAdapter.notifyDataSetChanged();

		unreadAdapter = new ReceiptListViewAdapter(getApplicationContext(),
				unreadDataList, false);
		unread_list.setAdapter(unreadAdapter);
		unreadAdapter.notifyDataSetChanged();

		unread_info.setText("共" + unreadDataList.size() + "位家长未读通知");
		read_info.setText("共" + readDataList.size() + "位家长已读通知");

		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from PublishNoticeActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

}
