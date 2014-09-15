package com.hubahuma.mobile.writing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SmsService;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@NoTitle
@EActivity(R.layout.activity_receipt)
public class ReceiptActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	TextView read_info, unread_info;

	@ViewById
	ListView read_list, unread_list;

	@ViewById
	Button resend_app, resend_sms;

	@Extra
	String content, noticeId;

	private List<UserEntity> readDataList;

	private List<UserEntity> unreadDataList;

	private ReceiptListViewAdapter readAdapter;

	private ReceiptListViewAdapter unreadAdapter;

	@RestService
	UserService userService;

	@RestService
	SmsService smsService;

	@App
	MyApplication myApp;

	@StringRes
	String uid, key;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private String code = "";

	private int timeCount = 60;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
		smsService.setRestErrorHandler(myErrorHandler);

		RestTemplate tpl = userService.getRestTemplate();
		RestTemplate smsTpl = smsService.getRestTemplate();

		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);

		tpl.setRequestFactory(s);
		smsTpl.setRequestFactory(s);
	}

	@AfterViews
	void init() {
		unread_info.setText("未读：");
		read_info.setText("已读：");
		resend_app.setVisibility(View.GONE);
		resend_sms.setVisibility(View.GONE);

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		preLoadData();
		loadData();
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
	void showPromptDialog(String title, String content) {
		promptDialog.setTitle(title);
		promptDialog.setContent(content);
		promptDialog.show(getSupportFragmentManager(), "dialog_prompt");
	}

	@UiThread
	void dismissPromptDialog() {
		promptDialog.dismiss();
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

		unread_info.setText("未读：" + unreadDataList.size());
		read_info.setText("已读：" + readDataList.size());

		if (unreadDataList.size() > 0) {
			resend_app.setVisibility(View.VISIBLE);
			resend_sms.setVisibility(View.VISIBLE);
		}

		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void resend_app() {

	}

	@Click
	void resend_sms() {

	}

	@Background
	void resendSms() {
		showLoadingDialog();

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		String phone = "";
		for (UserEntity user : unreadDataList) {
			phone += "," + user.getPhone();
		}
		phone = phone.substring(1);

		String msg = "来自\"" + myApp.getCurrentUser().getUsername() + "\"的通知："
				+ content + " 【虎爸虎妈公司】";

		String result = null;
		try {
			result = smsService.sendSMS(uid, key, phone, msg);
			System.out.println("SMS result:" + result);
		} catch (RestClientException e) {
			showToast("服务器连接异常", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		if (result == null || Integer.parseInt(result) < 0) {
			dismissLoadingDialog();
			showPromptDialog("失败", "短信发送失败，请稍后重试。");
			return;
		} else {
			dismissLoadingDialog();
			showPromptDialog("成功", "短信已成功发送。");
			resend_sms.setVisibility(View.GONE);
			// showToast("验证短信已发送", Toast.LENGTH_SHORT);
		}

	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
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
