package com.hubahuma.mobile;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.baidu.mapapi.SDKInitializer;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthParam;
import com.hubahuma.mobile.entity.service.AuthResp;
import com.hubahuma.mobile.entity.service.FetchDetailParentParam;
import com.hubahuma.mobile.entity.service.FetchDetailParentResp;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherParam;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherResp;
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
import android.widget.CheckBox;
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
	CheckBox remember_check;

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

		RestTemplate tpl = userService.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);
		tpl.setRequestFactory(s);

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

		remember_check.setChecked(prefs.rememberMe().get());
		if (remember_check.isChecked()) {
			username.setText(prefs.username().get());
			password.setText(prefs.password().get());
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
			error_info.setText("用户名不能为空");
			return false;
		}
		if (UtilTools.isEmpty(password.getText().toString())) {
			error_info.setText("密码不能为空");
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
		if (!GlobalVar.testMode) {
			if (!UtilTools.isNetConnected(getApplicationContext())) {
				showToast("无法访问网络", Toast.LENGTH_LONG);
				return;
			}
		}

		showLoadingDialog();
		AuthResp resp = null;
		if (!GlobalVar.testMode) {
			try {
				AuthParam ap = new AuthParam();
				ap.setUsername(username.getText().toString());
				ap.setPassword(password.getText().toString());
				resp = userService.login(ap);
				if (resp == null) {
					showToast("服务器数据返回异常", Toast.LENGTH_LONG);
					dismissLoadingDialog();
					return;
				}

			} catch (RestClientException e) {
				dismissLoadingDialog();
				showToast("服务器连接异常", Toast.LENGTH_LONG);
				return;
			}
		} else {
			resp = new AuthResp();
			resp.setResult(true);
			resp.setToken("asfsdfsadfsaf");
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
		}
		
		Log.i("Login", UtilTools.object2json(resp));

		requestSucc = resp.isResult();
		if (requestSucc == true) {

			if (remember_check.isChecked()) {
				prefs.username().put(username.getText().toString());
				prefs.password().put(password.getText().toString());
				prefs.token().put(resp.getToken());
				prefs.lastTokenUpdated().put(System.currentTimeMillis());
			} else {
				prefs.username().remove();
				prefs.password().remove();
				prefs.token().remove();
				prefs.lastTokenUpdated().remove();
			}

			prefs.rememberMe().put(remember_check.isChecked());

			UserEntity user = new UserEntity();

			if (GlobalVar.testMode) {
				user.setUsername(username.getText().toString());
				user.setPassword(password.getText().toString());
				user.setUserId("000001");
				user.setName(resp.getType() + "用户");
				user.setRemark("我是一个" + resp.getType());
				user.setUsername("user_test");
				user.setPhone("18201014080");
				user.setType(resp.getType());
				myApp.setToken(resp.getToken());
				myApp.setCurrentUser(user);
			}

			Intent intent = new Intent();
			switch (resp.getType()) {
			case UserType.PARENTS:

				if (!GlobalVar.testMode) {

					FetchDetailParentResp parentResp = null;

					try {
						FetchDetailParentParam param = new FetchDetailParentParam();
						List<String> un = new ArrayList<String>();
						un.add(username.getText().toString());
						param.setUsername(un);
						param.setToken(resp.getToken());
						parentResp = userService.fetchDetailParent(param);
						Log.i("Fetch Req", UtilTools.object2json(param));
						Log.i("Fetch Res", UtilTools.object2json(parentResp));
						if (parentResp == null
								|| parentResp.getParentObjects() == null
								|| parentResp.getParentObjects().isEmpty()
								|| parentResp.getUserObjects() == null
								|| parentResp.getUserObjects().isEmpty()) {
							showToast("服务器数据返回异常", Toast.LENGTH_LONG);
							dismissLoadingDialog();
						} else {
							user.bind(parentResp.getParentObjects().get(0),
									parentResp.getUserObjects().get(0));
							myApp.setToken(resp.getToken());
							myApp.setCurrentUser(user);
						}
					} catch (RestClientException e) {
						dismissLoadingDialog();
						showToast("用户数据获取失败", Toast.LENGTH_LONG);
					}
				}

				intent.setClass(this, ParentMainActivity_.class);
				break;
			case UserType.TEACHER:
				if (GlobalVar.testMode) {
					FetchDetailTeacherResp teacherResp = null;

					try {
						FetchDetailTeacherParam param = new FetchDetailTeacherParam();
						List<String> un = new ArrayList<String>();
						un.add(username.getText().toString());
						param.setUsername(un);
						param.setToken(resp.getToken());
						teacherResp = userService.fetchDetailTeacher(param);
						if (teacherResp == null
								|| teacherResp.getTeacherObjects() == null
								|| teacherResp.getTeacherObjects().isEmpty()
								|| teacherResp.getUserObjects() == null
								|| teacherResp.getUserObjects().isEmpty()) {
							showToast("服务器数据返回异常", Toast.LENGTH_LONG);
							dismissLoadingDialog();
						} else {
							user.bind(teacherResp.getTeacherObjects().get(0),
									teacherResp.getUserObjects().get(0));
							myApp.setToken(resp.getToken());
							myApp.setCurrentUser(user);
						}
					} catch (RestClientException e) {
						dismissLoadingDialog();
						showToast("用户数据获取失败", Toast.LENGTH_LONG);
					}

				}

				intent.setClass(this, TeacherMainActivity_.class);
				break;
			default:
				intent.setClass(this, ParentMainActivity_.class);
				break;
			}
			startActivityForResult(intent, ActivityCode.MAIN_ACTIVITY);

		} else {
			showPromptDialog("提示", "用户名或密码不正确");
		}
	}

	@Click
	void btn_register() {
		Intent intent = new Intent(this, RegisterActivity_.class);
		startActivityForResult(intent, ActivityCode.REGISTER_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.REGISTER_ACTIVITY)
	void onReturnFromRegisterParentActivity(Intent intent, int resultCode) {
		if (resultCode == 1) {
			String name = intent.getStringExtra("username");
			username.setText(name);
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
