package com.hubahuma.mobile.writing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthParam;
import com.hubahuma.mobile.entity.service.FetchAnnouncementParam;
import com.hubahuma.mobile.entity.service.FetchAnnouncementResp;
import com.hubahuma.mobile.news.message.NoticeFragment;
import com.hubahuma.mobile.news.message.OnNewMessageListener;
import com.hubahuma.mobile.profile.ProfileOrganizationActivity_;
import com.hubahuma.mobile.profile.ProfileParentsActivity_;
import com.hubahuma.mobile.profile.ProfileTeacherActivity_;
import com.hubahuma.mobile.profile.TagListViewAdapter;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.writing.NoitceListViewAdapter.NoitceListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_notice)
public class MyNoticeActivity extends FragmentActivity implements
		NoitceListViewListener, OnNewMessageListener {

	private List<NoticeEntity> dataList = new ArrayList<NoticeEntity>();

	private MyNoitceListViewAdapter myAdapter;

	private NoitceListViewAdapter adapter;

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	Button btn_publish;

	@ViewById
	View timeline;

	@ViewById
	TextView no_data_hint;

	@ViewById
	PullToRefreshListView notice_list;

	@RestService
	UserService userService;

	@App
	MyApplication myApp;

	@Pref
	SharedPrefs_ prefs;

	@Bean
	MyErrorHandler myErrorHandler;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);

		RestTemplate tpl = userService.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);
		tpl.setRequestFactory(s);

	}

	private LoadingDialog_ loadingDialog;

	@AfterViews
	void init() {

		preLoadData();
		if (myApp.getCurrentUser().getType().equals(UserType.PARENTS)) {
			loadNotice();
		} else {
			loadMyNotice();
		}
	}

	@Background(delay = 1000)
	void loadNotice() {

		FetchAnnouncementParam param = new FetchAnnouncementParam();
		param.setUntilId("");

		FetchAnnouncementResp resp = null;

		try {
			resp = userService.fetchAnnouncement(param);
		} catch (RestClientException e) {
			showToast("数据获取失败", Toast.LENGTH_LONG);
			postLoadData();
			return;
		}

		if (resp == null) {
			showToast("数据加载失败", Toast.LENGTH_LONG);
			postLoadData();
			return;
		}

		if (resp.isHasMore()) {
			showToast("通知过多，未加载完全", Toast.LENGTH_LONG);
		}

		dataList = resp.getNoticeList();
		if (dataList == null) {
			dataList = new ArrayList<NoticeEntity>();
		}
		postLoadData();
	}

	@Background(delay = 1000)
	void loadMyNotice() {
		// TODO 加载老师自己发过的通知
		dataList = getTestData(5);
		postLoadData();
	}

	private List<NoticeEntity> getTestData(int num) {

		List<NoticeEntity> testData = new ArrayList<NoticeEntity>();
		Random rand = new Random();
		for (int i = 1; i <= num; i++) {
			NoticeEntity item = new NoticeEntity();
			item.setNoticeId("3124123123");
			item.setAuthor(myApp.getCurrentUser());
			item.setDate(new Date());
			item.setContent(rand.nextInt(100)
					+ "进一步做好民办教育机构的设置要严格审批权限及审批程序，各地在审批民办教育机构时，要严格执行设置标准。");
			item.setTitle("title");
			if (i % 2 == 0)
				item.setImage("has_a_image");
			testData.add(item);
		}
		return testData;
	}

	@UiThread
	void preLoadData() {
		loadingDialog = new LoadingDialog_();
		if (myApp.getCurrentUser().getType().equals(UserType.PARENTS)) {
			btn_publish.setVisibility(View.GONE);
		} else {
			btn_publish.setVisibility(View.VISIBLE);
		}

		no_data_hint.setVisibility(View.GONE);
		timeline.setVisibility(View.GONE);
		progress_bar.setVisibility(View.VISIBLE);

	}

	@UiThread
	void postLoadData() {

		notice_list
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						showToast("正在载入历史数据", Toast.LENGTH_SHORT);
					}
				});

		if (myApp.getCurrentUser().getType().equals(UserType.PARENTS)) {
			// 设定下拉监听函数
			notice_list.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					String label = DateUtils.formatDateTime(
							MyNoticeActivity.this.getApplicationContext(),
							System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME
									| DateUtils.FORMAT_SHOW_DATE
									| DateUtils.FORMAT_ABBREV_ALL);
					// Update the LastUpdatedLabel
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
							label);
					// Do work to refresh the list here.
					loadNewNotice();
				}
			});

			notice_list.setMode(Mode.PULL_FROM_START);// 设置底部下拉刷新模式
			adapter = new NoitceListViewAdapter(getApplicationContext(),
					dataList, this);
			notice_list.setClickable(false);
			ListView actualListView = notice_list.getRefreshableView();
			actualListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {

			notice_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(MyNoticeActivity.this,
							ReceiptActivity_.class);
					NoticeEntity notice = dataList.get(position);
					intent.putExtra("noticeId", notice.getNoticeId());
					intent.putExtra("content", notice.getContent());
					startActivity(intent);
				}
			});

			myAdapter = new MyNoitceListViewAdapter(getApplicationContext(),
					dataList);
			notice_list.setMode(Mode.MANUAL_REFRESH_ONLY);
			notice_list.setClickable(true);
			ListView actualListView = notice_list.getRefreshableView();
			actualListView.setAdapter(myAdapter);
			myAdapter.notifyDataSetChanged();
		}

		if (dataList == null || dataList.isEmpty()) {
			no_data_hint.setVisibility(View.VISIBLE);
			timeline.setVisibility(View.GONE);
		} else {
			no_data_hint.setVisibility(View.GONE);
			timeline.setVisibility(View.VISIBLE);
		}
		progress_bar.setVisibility(View.GONE);

	}

	@Background(delay = 2000)
	void loadNewNotice() {
		List<NoticeEntity> newResultList = null;

		FetchAnnouncementParam param = new FetchAnnouncementParam();
		if (dataList != null && !dataList.isEmpty())
			param.setUntilId(dataList.get(0).getNoticeId());

		FetchAnnouncementResp resp = null;
		try {
			resp = userService.fetchAnnouncement(param);
		} catch (RestClientException e) {
			showToast("数据获取失败", Toast.LENGTH_LONG);
			return;
		}

		if (resp == null) {
			showToast("数据加载失败", Toast.LENGTH_LONG);
			return;
		}

		if (resp.isHasMore()) {
			showToast("通知过多，未加载完全", Toast.LENGTH_LONG);
		}

		newResultList = resp.getNoticeList();
		afterNewDataReceived(newResultList);
	}

	@UiThread
	void afterNewDataReceived(List<NoticeEntity> resultList) {
		// 在头部增加新添内容
		if (resultList != null && !resultList.isEmpty()) {
			for (NoticeEntity result : resultList) {
				dataList.add(0, result);
			}
			// 通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
			adapter.notifyDataSetChanged();
			notice_list.onRefreshComplete();
			this.OnNewMessageShowed(OnNewMessageListener.NOTICE_MESSAGE);

		} else {
			showToast("没有更新的通知了", Toast.LENGTH_SHORT);
		}

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
				myAdapter.notifyDataSetChanged();
			}
		}
	}

	// @ItemClick
	// void notice_list(int position) {
	//
	// if (myApp.getCurrentUser().getType().equals(UserType.PARENTS)) {
	// return;
	// }
	//
	// Intent intent = new Intent(this, ReceiptActivity_.class);
	// NoticeEntity notice = dataList.get(position);
	// intent.putExtra("noticeId", notice.getNoticeId());
	// intent.putExtra("content", notice.getContent());
	// startActivity(intent);
	// }

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

	@UiThread
	void showLoadingDialog() {
		loadingDialog.show(getSupportFragmentManager(), "dialog_loading");
	}

	@UiThread
	void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
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

	@Override
	public void OnNewMessageReady(int messageType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnNewMessageShowed(int messageType) {
		// TODO Auto-generated method stub

	}
}
