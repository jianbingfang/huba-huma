package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.service.BindPhoneParam;
import com.hubahuma.mobile.entity.service.BoolResp;
import com.hubahuma.mobile.entity.service.VerifyBindPhoneParam;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_verify_code)
public class AuthCodeActivity extends FragmentActivity implements
		PromptDialogListener {

	// @Extra
	// String username, password, name, phone, type, trueCode;

	@Extra
	String phone, type, userId, token;

	@ViewById
	TextView error_info, phone_number, hint;

	@ViewById
	EditText auth_code;

	@ViewById
	Button send_code;

	@RestService
	UserService userService;

	@StringRes
	String uid, key;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
		RestTemplate tpl = userService.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);
		tpl.setRequestFactory(s);
	}

	private int timeCount = 60;

	@AfterViews
	void init() {
		error_info.setText("");
		hint.setText("");
		send_code.setVisibility(View.GONE);
		hint.setVisibility(View.VISIBLE);

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		afterSmsSendSucc();
	}

	@UiThread
	void afterSmsSendFail() {
		send_code.setVisibility(View.VISIBLE);
		hint.setVisibility(View.GONE);
	}

	@UiThread
	void afterSmsSendSucc() {
		phone_number.setText("+86-" + phone.substring(0, 3) + " "
				+ phone.substring(3, 7) + " " + phone.substring(7));
		send_code.setVisibility(View.GONE);
		hint.setVisibility(View.VISIBLE);

		timeCount = 60;
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
	void send_code() {
		sendAuthCode();
	}

	@Click
	void btn_submit() {

		VerifyBindPhoneParam verifyBindPhoneParam = new VerifyBindPhoneParam();
		verifyBindPhoneParam.setUserId(userId);
		verifyBindPhoneParam.setToken(token);
		BoolResp resp = null;
		try {
			resp = userService.verifyBindPhone(verifyBindPhoneParam);
		} catch (RestClientException e) {
			dismissLoadingDialog();
			showToast("连接异常，短信发送失败", Toast.LENGTH_LONG);
			afterSmsSendFail();
			return;
		}

		if (resp == null) {
			dismissLoadingDialog();
			showToast("连接异常，短信发送失败", Toast.LENGTH_LONG);
			afterSmsSendFail();
			return;
		}
		if (resp.isOk()) {
			Intent intent = getIntent();
			this.setResult(1, intent);
			this.finish();
		} else {
			dismissLoadingDialog();
			showToast("验证码错误", Toast.LENGTH_LONG);
			afterSmsSendFail();
		}

		// if (checkCode()) {
		// switch (type) {
		// case UserType.PARENTS:
		// handleRegisterParent();
		// break;
		// case UserType.TEACHER:
		// handleRegisterTeacher();
		// break;
		// }
		// } else {
		// error_info.setText("验证码错误");
		// return;
		// }

	}

	@AfterTextChange
	void auth_code() {
		error_info.setText("");
	}

	private boolean checkCode() {
		if (UtilTools.isEmpty(auth_code.getText().toString().trim())) {
			error_info.setText("验证码不能为空");
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
			afterSmsSendFail();
			return;
		}

		BindPhoneParam bindPhoneParam = new BindPhoneParam();
		bindPhoneParam.setPhone(phone);
		bindPhoneParam.setUserId(userId);
		bindPhoneParam.setToken(token);

		try {
			userService.bindPhone(bindPhoneParam);
		} catch (RestClientException e) {
			dismissLoadingDialog();
			showToast("连接异常，短信发送失败", Toast.LENGTH_LONG);
			afterSmsSendFail();
			return;
		}

		dismissLoadingDialog();
		showToast("验证短信已发送", Toast.LENGTH_SHORT);
		afterSmsSendSucc();

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
