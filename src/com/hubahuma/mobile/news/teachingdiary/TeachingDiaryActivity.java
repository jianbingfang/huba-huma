package com.hubahuma.mobile.news.teachingdiary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.entity.DiaryEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryViewAdapter.TeachingDiaryViewListener;
import com.hubahuma.mobile.profile.ProfileOrganizationActivity_;
import com.hubahuma.mobile.profile.ProfileParentsActivity_;
import com.hubahuma.mobile.profile.ProfileTeacherActivity_;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_teaching_diary)
public class TeachingDiaryActivity extends Activity implements
		TeachingDiaryViewListener {

	@ViewById
	ListView diary_list;

	@ViewById
	Button btn_publish;

	@ViewById
	ProgressBar progress_bar;

	@Extra
	boolean isSelf;

	private List<DiaryEntity> dataList = new ArrayList<DiaryEntity>();

	private TeachingDiaryViewAdapter adapter;

	@AfterViews
	void init() {
		if (isSelf) {
			btn_publish.setVisibility(View.VISIBLE);
		}else{
			btn_publish.setVisibility(View.GONE);
		}
		showProgressBar();
		loadData();
	}

	@Background(delay = 1000)
	void loadData() {
		dataList = getTestData();
		hideProgressBar();
	}

	@UiThread
	void showProgressBar() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void hideProgressBar() {
		adapter = new TeachingDiaryViewAdapter(getApplicationContext(),
				dataList, this);
		diary_list.setAdapter(adapter);
		progress_bar.setVisibility(View.GONE);
		// adapter.notifyDataSetChanged();

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

	private List<DiaryEntity> getTestData() {

		List<DiaryEntity> dairyList = new ArrayList<DiaryEntity>();
		Random rand = new Random();

		UserEntity user = new UserEntity();
		user.setUserId("#0001");
		user.setType(UserType.TEACHER);
		user.setName("姚燕芬老师");
		user.setRemark("北京市第二幼儿园");

		for (int i = 0; i < 4; i++) {
			DiaryEntity diary = new DiaryEntity();
			diary.setAuthor(user);
			diary.setImg(null);
			diary.setContent("家长们，今天给学生布置的作业，我已上传至百度云盘，密码是：tcur，您下载后可以敦促孩子写完。");
			diary.setCommentNum(rand.nextInt(50));
			diary.setCollectNum(rand.nextInt(100));
			diary.setDate(new Date());
			diary.setLiked(rand.nextBoolean());
			dairyList.add(diary);
		}

		return dairyList;
	}

	@Override
	public void onAuthorClick(UserEntity user) {
		Intent intent = new Intent();
		intent.putExtra("user", user);

		switch (user.getType()) {
		case UserType.ORGANIZTION:
			intent.setClass(this, ProfileOrganizationActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_ORGANIZATION_ACTIVITY);
			break;

		case UserType.TEACHER:
			intent.setClass(this, ProfileTeacherActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_TEACHER_ACTIVITY);
			break;

		case UserType.PARENTS:
			intent.setClass(this, ProfileParentsActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_PARENTS_ACTIVITY);
			break;

		}
	}
}
