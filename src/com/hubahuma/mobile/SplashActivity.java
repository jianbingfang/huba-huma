package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;

import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

	@AfterViews
	void init() {
		checkPhoneState();
	}

	@Background(delay = 2000)
	void checkPhoneState() {
		// 检查网络状况
		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
		}
		startLoginActivity();
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
	}

	void startLoginActivity() {
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
