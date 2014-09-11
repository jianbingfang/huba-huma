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

import com.baidu.mapapi.SDKInitializer;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthResp;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	EditText username, password;

	@ViewById
	TextView error_info;

	@ViewById
	Button btn_login;

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
	}

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean requestSucc = false;

	@AfterViews
	void init() {
		error_info.setText("");

		System.out.println("username:" + prefs.username().get());
		System.out.println("password:" + prefs.password().get());
		System.out.println("token:" + prefs.token().get());
		System.out.println("time:" + prefs.lastTokenUpdated().get());
		if (prefs.username().exists()) {
			username.setText(prefs.username().get());
		}

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
	}

	@Click
	void btn_login() {
		if (checkInput())
			handleLogin();
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
	void handleLogin() {
		showLoadingDialog();
		AuthResp resp = null;
		if (!GlobalVar.testMode) {
			try {
				resp = userService.login(username.getText().toString(),
						password.getText().toString());
			} catch (RestClientException e) {
				showToast("服务器验证错误", Toast.LENGTH_LONG);
				dismissLoadingDialog();
				Log.e("Rest Error", e.getMessage() + this.getClass().getName());
				return;
			}
		} else {
			resp = new AuthResp();
			resp.setResult(true);
			resp.setToken("asfsdfsadfsaf");
		}

		if (resp == null) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		switch (username.getText().toString()) {
		case "1":
			resp.setType(UserType.PARENTS);
			break;
		case "2":
			resp.setType(UserType.TEACHER);
			break;
		case "3":
			resp.setType(UserType.ORGANIZTION);
			break;
		case "4":
			resp.setType(UserType.ADMIN);
			break;
		default:
			resp.setType(UserType.TEACHER);
			resp.setResult(false);
			break;
		}
		dismissLoadingDialog();
		requestSucc = resp.isResult();
		if (requestSucc == true) {
			
			UserEntity user = new UserEntity();
			user.setId("000001");
			user.setRemark("none");
			user.setUsername("当前用户");
			user.setType(resp.getType());
			myApp.token = resp.getToken();
			myApp.setCurrentUser(user);

			prefs.username().put(username.getText().toString());
			prefs.password().put(password.getText().toString());
			prefs.token().put(resp.getToken());
			prefs.lastTokenUpdated().put(System.currentTimeMillis());

			Intent intent = new Intent(this, MainActivity_.class);
			startActivityForResult(intent, ActivityCode.MAIN_ACTIVITY);

		} else {
			showPromptDialog("提示", "用户名或密码不正确");
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

	@Override
	public void onBackPressed() {
		this.finish();
	}
}
