package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.resp.AuthResp;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

	@Pref
	SharedPrefs_ prefs;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean requestSucc = false;

	@AfterViews
	void init() {
		error_info.setText("");

		System.out.println("username:"+prefs.username().get());
		System.out.println("password:"+prefs.password().get());
		System.out.println("token:"+prefs.token().get());
		System.out.println("time:"+prefs.lastTokenUpdated().get());
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
		AuthResp resp = userService.login(username.getText().toString(),
				password.getText().toString());
		resp.setResult(true);
		resp.setToken("asfsdfsadfsaf");
		resp.setType(UserType.TEACHER);
		dismissLoadingDialog();
		requestSucc = resp.isResult();
		if (requestSucc == true) {
			GlobalVar.token = resp.getToken();
			Intent intent = new Intent(this, MainActivity_.class);
			startActivityForResult(intent, ActivityCode.MAIN_ACTIVITY);

			prefs.username().put(username.getText().toString());
			prefs.password().put(password.getText().toString());
			prefs.token().put(resp.getToken());
			prefs.lastTokenUpdated().put(System.currentTimeMillis());
		} else {
			showPromptDialog("错误", "用户名或密码错误");
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
}
