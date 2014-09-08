package com.hubahuma.mobile.profile;

import java.util.Date;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.MainActivity_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.CommentEntity;
import com.hubahuma.mobile.utils.GlobalVar;
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
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_write_comment)
public class WriteCommentActivity extends Activity {

	public interface CommentType {
		final static int TEACHER_COMMENT = 0;
		final static int ORGANIZATION_COMMENT = 1;
	}

	@ViewById
	TextView target_hint, score_hint_1, score_hint_2, score_hint_3,
			score_hint_4;

	@ViewById
	RatingBar rating_bar_1, rating_bar_2, rating_bar_3, rating_bar_4;

	@ViewById
	EditText comment;

	@ViewById
	ProgressBar progress_bar;

	@Extra
	int type;

	@AfterViews
	void init() {

		if (GlobalVar.getCurrentUser() != null) {
			switch (GlobalVar.getCurrentUser().getType()) {
			case CommentType.TEACHER_COMMENT:
				target_hint.setText("给教师打分");
				score_hint_1.setText("教学方法");
				score_hint_2.setText("品行态度");
				score_hint_3.setText("教学效果");
				score_hint_4.setText("教学内容");
				break;

			case CommentType.ORGANIZATION_COMMENT:
				target_hint.setText("给该学校/机构打分");
				score_hint_1.setText("教育水平");
				score_hint_2.setText("校园环境");
				score_hint_3.setText("生源质量");
				score_hint_4.setText("课程费用");
				break;
			}
		}

	}

	@Click
	void btn_submit() {
		String content = comment.getText().toString().trim();

		if (UtilTools.isEmpty(content))
			return;

		float score = (rating_bar_1.getRating() + rating_bar_2.getRating()
				+ rating_bar_3.getRating() + rating_bar_4.getRating()) / 4;

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
		rating_bar_1.setEnabled(false);
		rating_bar_2.setEnabled(false);
		rating_bar_3.setEnabled(false);
		rating_bar_4.setEnabled(false);
	}

	@UiThread
	void enableUI() {
		progress_bar.setVisibility(View.GONE);
		comment.setEnabled(true);
		rating_bar_1.setEnabled(true);
		rating_bar_2.setEnabled(true);
		rating_bar_3.setEnabled(true);
		rating_bar_4.setEnabled(true);
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
		intent.putExtra("result", "returned from WriteCommentActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
