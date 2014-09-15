package com.hubahuma.mobile;

import java.io.IOException;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.baidu.mapapi.SDKInitializer;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthParam;
import com.hubahuma.mobile.entity.service.AuthResp;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

	@RestService
	UserService userService;

	@Pref
	SharedPrefs_ prefs;

	@Bean
	MyErrorHandler myErrorHandler;

	@App
	MyApplication myApp;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
		RestTemplate tpl = userService.getRestTemplate();

		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(5000);
		s.setReadTimeout(1000);
		tpl.setRequestFactory(s);

		// SDKInitializer.initialize(getApplicationContext());
	}

	@AfterViews
	void init() {

		System.out.println("username:" + prefs.username().get());
		System.out.println("password:" + prefs.password().get());
		System.out.println("token:" + prefs.token().get());
		System.out.println("time:" + prefs.lastTokenUpdated().get());

		preProc();
	}

	@Background(delay = 2000)
	void preProc() {

		if (GlobalVar.testMode) {
			startLoginActivity();
			showToast("当前为演示模式", Toast.LENGTH_LONG);
			return;
		}

		// 检查网络状况
		if (!UtilTools.isNetConnected(getApplicationContext())) {
			startLoginActivity();
			showToast("无法访问网络", Toast.LENGTH_LONG);
			return;
		}

		if (prefs.token().exists() && prefs.lastTokenUpdated().exists()
				&& false) {
			long timestamp = prefs.lastTokenUpdated().get();
			long duration = System.currentTimeMillis() - timestamp;
			long oneMonth = 30 * 24 * 60 * 60 * 1000;
			if (duration < oneMonth && !UtilTools.isEmpty(prefs.token().get())) {
				startLoginActivity();
			} else {
				if (prefs.username().exists() && prefs.password().exists()) {
					Log.d("debug", "attempt to login - "
							+ prefs.username().get() + ":"
							+ prefs.password().get());
					AuthResp resp = null;
					try {
						AuthParam ap = new AuthParam();
						ap.setUsername(prefs.username().get());
						ap.setPassword(prefs.password().get());
						resp = userService.login(ap);
					} catch (RestClientException e) {
						showToast("服务器连接异常", Toast.LENGTH_LONG);
						startLoginActivity();
						return;
					}

					if (resp != null && resp.isResult()) {
						Log.d("debug", "attempt login succ");

						UserEntity user = new UserEntity();
						user.setId("000001");
						user.setRemark("none");
						user.setUsername("当前用户");
						user.setType(resp.getType());

						switch (prefs.username().get()) {
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

						myApp.token = resp.getToken();
						myApp.setCurrentUser(user);

						prefs.edit().token().put(resp.getToken())
								.lastTokenUpdated()
								.put(System.currentTimeMillis());
						startMainActivity();
						return;
					}
				}
				startLoginActivity();
			}

		} else {
			startLoginActivity();
		}
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
	}

	void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity_.class);
		startActivityForResult(intent, ActivityCode.LOGIN_ACTIVITY);
	}

	void startMainActivity() {

		Intent intent = new Intent();
		switch (myApp.getCurrentUser().getType()) {
		case UserType.PARENTS:
			intent.setClass(this, ParentMainActivity_.class);
			break;
		case UserType.TEACHER:
			intent.setClass(this, TeacherMainActivity_.class);
			break;
		default:
			intent.setClass(this, ParentMainActivity_.class);
			break;
		}
		startActivityForResult(intent, ActivityCode.MAIN_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.MAIN_ACTIVITY)
	void onReturnFromMainActivity() {
		this.finish();
	}

	@OnActivityResult(ActivityCode.LOGIN_ACTIVITY)
	void onReturnFromLoginActivity() {
		this.finish();
	}
}
