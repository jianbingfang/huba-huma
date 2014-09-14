package com.hubahuma.mobile;

import java.util.Random;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
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
import org.springframework.web.client.RestClientException;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.service.RegisterParent;
import com.hubahuma.mobile.entity.service.RegisterParentResp;
import com.hubahuma.mobile.entity.service.RegisterTeacher;
import com.hubahuma.mobile.entity.service.RegisterTeacherResp;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SmsService;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_auth_code)
public class AuthCodeActivity extends FragmentActivity implements
		PromptDialogListener {

	@Extra
	String username, password, name, phone, type;

	@ViewById
	TextView error_info, phone_number, hint;

	@ViewById
	EditText auth_code;

	@ViewById
	Button send_code;

	@RestService
	UserService userService;

	@RestService
	SmsService smsService;

	@StringRes
	String uid, key;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private String code = "";

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
		smsService.setRestErrorHandler(myErrorHandler);
	}

	private int timeCount = 60;

	@AfterViews
	void init() {
		error_info.setText("");
		send_code.setVisibility(View.GONE);
		hint.setVisibility(View.VISIBLE);

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		sendAuthCode();
	}

	@UiThread
	void afterSmsSend() {
		phone_number.setText("+86 " + phone.substring(0, 3) + " "
				+ phone.substring(3, 7) + " " + phone.substring(7));
		send_code.setVisibility(View.GONE);
		hint.setVisibility(View.VISIBLE);

		hint.setText("接收短信大约需要" + timeCount + "秒");

		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				--timeCount;
				hint.setText("接收短信大约需要" + timeCount + "秒");
				if (timeCount > 0) {
					handler.postDelayed(this, 1000);
				} else {
					handler.removeCallbacks(this);
					send_code.setVisibility(View.VISIBLE);
					hint.setVisibility(View.GONE);
				}
			}
		};

		handler.postDelayed(runnable, 1000);
	}

	@Click
	void send_code(){
		sendAuthCode();
	}
	
	@Click
	void btn_submit() {

		if (checkCode()) {
			switch (type) {
			case UserType.PARENTS:
				handleRegisterParent();
				break;
			case UserType.TEACHER:
				handleRegisterTeacher();
				break;
			}
		}

	}

	private boolean checkCode() {
		if (code.equals(auth_code.getText().toString().trim())) {
			error_info.setText("验证码错误！");
			return false;
		}
		error_info.setText("");
		return true;
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

	@Background
	void sendAuthCode() {
		showLoadingDialog();

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		code = "";
		Random rand = new Random();
		for (int i = 0; i < 4; i++) {
			code += rand.nextInt(10);
		}

		String result = null;

		String content = "短信验证码：" + code + "。【虎爸虎妈公司】";
		try {
			result = smsService.sendSMS(uid, key, phone, content);
		} catch (RestClientException e) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			Log.e("Rest Error", e.getMessage() + ". - "
					+ this.getClass().getName());
			return;
		}

		if (result == null || Integer.parseInt(result) < 0) {
			dismissLoadingDialog();
			showPromptDialog("失败", "验证码发送失败，请重试。");
			return;
		} else {
			dismissLoadingDialog();
			showToast("验证短信已发送", Toast.LENGTH_SHORT);
			afterSmsSend();
		}

	}

	@Background
	void handleRegisterParent() {

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			return;
		}

		showLoadingDialog();
		RegisterParentResp resp = null;

		RegisterParent parent = new RegisterParent();

		parent.setUsername(username);
		parent.setPassword(password);
		parent.setName(name);
		parent.setPhone(phone);

		try {
			resp = userService.registerParent(parent);
		} catch (RestClientException e) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			Log.e("Rest Error", e.getMessage() + this.getClass().getName());
			return;
		}

		if (resp == null) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		dismissLoadingDialog();
		if (resp.isOk()) {

			Intent intent = getIntent();
			intent.putExtra("username", username);
			this.setResult(1, intent);
			this.finish();

		} else {
			showPromptDialog("注册失败", resp.getReason());
		}
	}

	@Background
	void handleRegisterTeacher() {

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			return;
		}

		showLoadingDialog();
		RegisterTeacherResp resp = null;

		RegisterTeacher teacher = new RegisterTeacher();

		teacher.setUsername(username);
		teacher.setPassword(password);
		teacher.setName(name);
		teacher.setPhone(phone);

		try {
			resp = userService.registerTeacher(teacher);
		} catch (RestClientException e) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			Log.e("Rest Error", e.getMessage() + ". "
					+ this.getClass().getName());
			return;
		}

		if (resp == null) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		dismissLoadingDialog();
		if (resp.isOk()) {

			Intent intent = getIntent();
			intent.putExtra("username", username);
			this.setResult(1, intent);
			this.finish();

		} else {
			showPromptDialog("注册失败", resp.getReason());
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
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
