package com.hubahuma.mobile.writing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.profile.TagListViewAdapter;
import com.hubahuma.mobile.utils.GlobalVar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_my_notice)
public class MyNoticeActivity extends Activity {

	private List<NoticeEntity> dataList;

	private MyNoitceListViewAdapter adapter;

	@App
	MyApplication myApp;
	
	@ViewById
	ProgressBar progress_bar;

	@ViewById
	View timeline;

	@ViewById
	ListView notice_list;

	@AfterViews
	void init() {
		preLoadData();
		loadData();
	}

	@Background(delay = 1000)
	void loadData() {
		dataList = getTestData();
		postLoadData();
	}

	private List<NoticeEntity> getTestData() {

		List<NoticeEntity> testData = new ArrayList<NoticeEntity>();
		for (int i = 1; i <= 5; i++) {
			NoticeEntity item = new NoticeEntity();
			item.setUser(myApp.getCurrentUser());
			item.setDate(new Date());
			item.setContent("进一步做好民办教育机构的设置要严格审批权限及审批程序，各地在审批民办教育机构时，要严格执行设置标准。");
			item.setTitle("title");
			if (i % 2 == 0)
				item.setImage("has_a_image");
			testData.add(item);
		}
		return testData;
	}

	@UiThread
	void preLoadData() {
		timeline.setVisibility(View.GONE);
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postLoadData() {

		adapter = new MyNoitceListViewAdapter(getApplicationContext(), dataList);
		notice_list.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		timeline.setVisibility(View.VISIBLE);
		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void btn_publish() {
		Intent intent = new Intent();
		intent.setClass(this, PublishNoticeActivity_.class);
		startActivityForResult(intent, ActivityCode.PUBLISH_NOTICE_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.PUBLISH_NOTICE_ACTIVITY)
	void onReturnFromPublishNoticeActivity(int resultCode, Intent intent) {
		if (resultCode == 1) {
			NoticeEntity newNotice = (NoticeEntity) intent
					.getSerializableExtra("newNotice");
			if (newNotice != null) {
				dataList.add(0, newNotice);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@ItemClick
	void notice_list(int position) {
		Intent intent = new Intent(this, ReceiptActivity_.class);
		startActivity(intent);
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ChangeTagActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
