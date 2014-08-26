package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;

import android.app.Activity;
import android.content.Intent;

import com.hubahuma.mobile.R;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_profile_teacher)
public class ProfileTeacherActivity extends Activity {

	
	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from TeachingDiaryActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
