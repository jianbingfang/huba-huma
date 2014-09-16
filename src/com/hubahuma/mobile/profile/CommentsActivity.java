package com.hubahuma.mobile.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.ArticleEntity;
import com.hubahuma.mobile.entity.CommentEntity;
import com.hubahuma.mobile.entity.UserEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_comments)
public class CommentsActivity extends Activity {

	@ViewById
	ListView comment_list;
	
	@ViewById
	ProgressBar progress_bar;
	
	private List<CommentEntity> dataList = new ArrayList<CommentEntity>();

	private CommentViewAdapter adapter;

	@AfterViews
	void init() {
		loadData();
	}

	@Background(delay = 2000)
	void loadData() {
		showProgressBar();
		dataList = getTestData();
		hideProgressBar();
	}

	@UiThread
	void showProgressBar() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void hideProgressBar() {
		adapter = new CommentViewAdapter(
				getApplicationContext(), dataList);
		comment_list.setAdapter(adapter);
		progress_bar.setVisibility(View.GONE);

	}

	@Click
	void btn_write_comment(){
		Intent intent = new Intent(this, WriteCommentActivity_.class);
		startActivityForResult(intent, ActivityCode.WRITE_COMMENT_ACTIVITY);
	}
	
	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from CommentsActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
	
	private List<CommentEntity> getTestData() {
		List<CommentEntity> commentList = new ArrayList<CommentEntity>();
		Random rand = new Random();

		UserEntity user = new UserEntity();
		user.setUserId("#0001");
		user.setType(UserType.PARENTS);
		user.setName("李晓龙");
		user.setRemark("李一贤家长");

		for (int i = 0; i < 4; i++) {
			CommentEntity comment = new CommentEntity();
			comment.setUser(user);
			comment.setContent("张老师的教学质量很不错。我儿子的英语成绩进步很快！");
			comment.setScore(rand.nextInt(5));
			comment.setDate(new Date());
			commentList.add(comment);
		}

		return commentList;
	}
	
}
