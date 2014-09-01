package com.hubahuma.mobile.profile;

import java.util.Date;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.CommentEntity;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_write_teacher_comment)
public class WriteTeacherCommentActivity extends Activity {

	@ViewById
	RatingBar rating_bar_method, rating_bar_attitude, rating_bar_content,
			rating_bar_effect;

	@ViewById
	EditText comment;

	@ViewById
	ProgressBar progress_bar;

	@AfterViews
	void init() {

	}

	@Click
	void btn_submit() {
		String content = comment.getText().toString().trim();

		if (UtilTools.isEmpty(content))
			return;

		float score = (rating_bar_method.getRating()
				+ rating_bar_attitude.getRating()
				+ rating_bar_content.getRating() + rating_bar_effect
				.getRating()) / 4;

		CommentEntity cmt = new CommentEntity();
		cmt.setContent(content);
		cmt.setDate(new Date());
		cmt.setScore(score);
		// TODO 加入真实用户
		cmt.setUser(null);

		disableUI();
		handleSubmit(cmt);
	}

	boolean publishComment(CommentEntity comment) {
		// TODO 处理发布逻辑
		return true;
	}

	@Background(delay = 1000)
	void handleSubmit(CommentEntity comment) {
		if (publishComment(comment)) {
			onPublishSucc();
		} else {
			onPublishFail();
		}
		enableUI();
	}

	@UiThread
	void disableUI() {
		progress_bar.setVisibility(View.VISIBLE);
		comment.setEnabled(false);
		rating_bar_method.setEnabled(false);
		rating_bar_attitude.setEnabled(false);
		rating_bar_content.setEnabled(false);
		rating_bar_effect.setEnabled(false);
	}

	@UiThread
	void enableUI() {
		progress_bar.setVisibility(View.GONE);
		comment.setEnabled(true);
		rating_bar_method.setEnabled(true);
		rating_bar_attitude.setEnabled(true);
		rating_bar_content.setEnabled(true);
		rating_bar_effect.setEnabled(true);
	}

	@UiThread
	void onPublishSucc() {
		Toast.makeText(getApplicationContext(), "评论已发布", Toast.LENGTH_SHORT)
				.show();
		Intent intent = new Intent(this, CommentsActivity_.class);
		startActivityForResult(intent, ActivityCode.COMMENTS_ACTIVITY);
	}

	@UiThread
	void onPublishFail() {
		Toast.makeText(getApplicationContext(), "评论发布失败", Toast.LENGTH_SHORT)
				.show();
	}

	@AfterTextChange(R.id.comment)
	void onCommentTextChanged(Editable text) {
		if (text.toString().trim().length() > 190) {
			Toast.makeText(getApplicationContext(), "您已超过190字，请注意字数",
					Toast.LENGTH_SHORT).show();
		}
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
