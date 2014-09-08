package com.hubahuma.mobile.writing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.hubahuma.mobile.utils.GlobalVariable;

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
	TextView total_info;

	@ViewById
	ListView receipt_list;

	private List<UserEntity> dataList;

	private ReceiptListViewAdapter adapter;

	@AfterViews
	void init() {
		total_info.setText("");
		preLoadData();
		loadData();
	}

	@Background(delay = 1000)
	void loadData() {
		dataList = getTestData();
		postLoadData();
	}

	private List<UserEntity> getTestData() {

		List<UserEntity> testData = new ArrayList<UserEntity>();
		for (int i = 1; i <= 10; i++) {
			UserEntity item = new UserEntity();
			item.setId("0000"+i);
			item.setType(UserType.PARENTS);
			item.setUsername("高健"+i);
			item.setRemark("高晓东"+i+"爸爸");
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

		adapter = new ReceiptListViewAdapter(getApplicationContext(), dataList);
		receipt_list.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		total_info.setText("共" + dataList.size() + "位");
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
