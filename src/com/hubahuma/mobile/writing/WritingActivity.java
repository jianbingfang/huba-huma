package com.hubahuma.mobile.writing;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryActivity_;

import android.app.Activity;
import android.content.Intent;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_writing)
public class WritingActivity extends Activity {

	@Click
	void btn_writing_diary(){
		Intent intent = new Intent();
		intent.setClass(this, TeachingDiaryActivity_.class);
		intent.putExtra("isSelf", true);
		startActivityForResult(intent, ActivityCode.TEACHING_DIARY_ACTIVITY);
	}
	
	@Click
	void btn_writing_notice(){
		Intent intent = new Intent();
		intent.setClass(this, PublishNoticeActivity_.class);
		startActivityForResult(intent, ActivityCode.PUBLISH_NOTICE_ACTIVITY);
	}
	
	@Click
	void btn_writing_info(){
		
	}
	
}
