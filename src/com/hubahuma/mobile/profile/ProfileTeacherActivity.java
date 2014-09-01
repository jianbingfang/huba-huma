package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_profile_teacher)
public class ProfileTeacherActivity extends Activity {

	
	@Click
	void btn_comment(){
		Intent intent = new Intent(this, WriteTeacherCommentActivity_.class);
		startActivityForResult(intent, ActivityCode.WRITE_TEACHER_COMMENT_ACTIVITY);
	}

	@Click
	void comment_panel(){
		Intent intent = new Intent(this, CommentsActivity_.class);
		startActivityForResult(intent, ActivityCode.COMMENTS_ACTIVITY);
	}
	
	@Click
	void btn_add_to_managebook(){
		
	}
	
	@Click
	void btn_follow(){
		
	}
	
	@Click
	void latest_article_panel(){
		Intent intent = new Intent(this, PublishedArticleActivity_.class);
		startActivityForResult(intent, ActivityCode.PUBLISHED_ARTICLE_ACTIVITY);
	}
	
	@Click
	void btn_phone() {
		phone_panel();
	}

	@Click
	void phone_panel() {
		// TODO 加入真实号码
		Uri callToUri = Uri.parse("tel:" + "10086");
		Intent intent = new Intent(Intent.ACTION_CALL, callToUri);
		startActivity(intent);
	}

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
