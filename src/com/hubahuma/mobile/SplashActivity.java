package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import com.hubahuma.mobile.entity.resp.AuthResp;
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
@Fullscreen
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

	@RestService
	UserService userService;

	@Pref
	SharedPrefs_ prefs;

	@AfterViews
	void init() {
		
		System.out.println("username:"+prefs.username().get());
		System.out.println("password:"+prefs.password().get());
		System.out.println("token:"+prefs.token().get());
		System.out.println("time:"+prefs.lastTokenUpdated().get());
		
		preProc();
	}

	@Background(delay = 2000)
	void preProc() {
		// 检查网络状况
		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
		} else {
			if (prefs.token().exists() && prefs.lastTokenUpdated().exists()) {
				long timestamp = prefs.lastTokenUpdated().get();
				long duration = System.currentTimeMillis() - timestamp;
				long oneMonth = 30 * 24 * 60 * 60 * 1000;
				if (duration < oneMonth
						&& !UtilTools.isEmpty(prefs.token().get())) {
					startLoginActivity();
				} else {
					if (prefs.username().exists() && prefs.password().exists()) {
						Log.d("debug", "attempt to login - "+prefs.username().get() + ":" + prefs.password().get());
						AuthResp resp = userService.login(prefs.username()
								.get(), prefs.password().get());
						if (resp.isResult()) {
							Log.d("debug","attempt login succ");
							GlobalVar.token = resp.getToken();
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
		Intent intent = new Intent(this, MainActivity_.class);
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
