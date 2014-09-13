package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.RestClientException;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthResp;
import com.hubahuma.mobile.entity.service.RegisterParent;
import com.hubahuma.mobile.entity.service.RegisterParentResp;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_register_parent)
public class RegisterParentActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	EditText username, password, password_again, name, phone;

	@ViewById
	TextView error_info;

	@ViewById
	Button btn_register;

	@RestService
	UserService userService;

	@App
	MyApplication myApp;

	@Pref
	SharedPrefs_ prefs;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
	}

	@AfterViews
	void init() {
		error_info.setText("");

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
	}

	@Click
	void btn_register() {
		if (checkInput())
			handleRegister();
	}

	private boolean checkInput() {
		if (UtilTools.isEmpty(username.getText().toString())) {
			error_info.setText("用户名不能为空！");
			return false;
		}
		if (UtilTools.isEmpty(password.getText().toString())) {
			error_info.setText("密码不能为空！");
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
	void handleRegister() {

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			return;
		}

		showLoadingDialog();
		RegisterParentResp resp = null;

		RegisterParent parent = new RegisterParent();

		parent.setUsername(username.getText().toString().trim());
		parent.setPassword(password.getText().toString().trim());
		parent.setName(name.getText().toString().trim());
		parent.setPhone(phone.getText().toString().trim());

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
			intent.putExtra("username", username.getText().toString().trim());
			this.setResult(1, intent);
			this.finish();

		} else {
			showPromptDialog("注册失败", resp.getReason());
		}
	}

	@OnActivityResult(ActivityCode.MAIN_ACTIVITY)
	void onReturnFromMainActivity() {
		this.finish();
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
