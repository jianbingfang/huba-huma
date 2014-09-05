package com.hubahuma.mobile.writing;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.rest.Accept;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryActivity_;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_writing)
public class WritingActivity extends Activity {

	@Click
	void btn_writing_diary() {
		Intent intent = new Intent();
		intent.setClass(this, TeachingDiaryActivity_.class);
		intent.putExtra("isSelf", true);
		startActivityForResult(intent, ActivityCode.TEACHING_DIARY_ACTIVITY);
	}

	@Click
	void btn_writing_notice() {
		Intent intent = new Intent();
		intent.setClass(this, PublishNoticeActivity_.class);
		startActivityForResult(intent, ActivityCode.PUBLISH_NOTICE_ACTIVITY);
	}

	@Click
	void btn_writing_info() {

	}

	@OnActivityResult(ActivityCode.PUBLISH_NOTICE_ACTIVITY)
	void onPublishNoticeActivityReturn(int resultCode, Intent data) {
		String result = data.getStringExtra("result");
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
				.show();
	}

}
