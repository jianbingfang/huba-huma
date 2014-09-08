package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

	@RestService
	UserService userService;

	@AfterViews
	void init() {
		checkPhoneState();
	}

	@Background(delay = 2000)
	void checkPhoneState() {
		// 检查网络状况
		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
		} else {
			showToast("网络访问正常", Toast.LENGTH_SHORT);
		}
		SharedPreferences authInfo = getSharedPreferences("authInfo",
				Context.MODE_PRIVATE);
		String token = authInfo.getString("token", "");
		long timestamp = authInfo.getLong("timestamp", Long.MAX_VALUE);
		long duration = System.currentTimeMillis() - timestamp;
		long oneMonth = 30 * 24 * 60 * 60 * 1000;
		if (duration > oneMonth || UtilTools.isEmpty(token)) {
			startLoginActivity();
		} else {
			startMainActivity();
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
