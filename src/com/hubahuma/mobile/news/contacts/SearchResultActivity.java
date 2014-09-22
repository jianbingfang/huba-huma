package com.hubahuma.mobile.news.contacts;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.discovery.VerifyChildDialog.OnVerifyChildDialogConfirmListener;
import com.hubahuma.mobile.discovery.VerifyChildDialog_;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthParam;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherParam;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherResp;
import com.hubahuma.mobile.entity.service.SearchTeacherParam;
import com.hubahuma.mobile.entity.service.SearchTeacherResp;
import com.hubahuma.mobile.entity.service.SendVerificationRequestParentParam;
import com.hubahuma.mobile.news.contacts.SearchResultViewAdapter.SearchResultViewListener;
import com.hubahuma.mobile.news.managebook.GroupManageViewAdapter;
import com.hubahuma.mobile.news.managebook.OneInputDialog_;
import com.hubahuma.mobile.news.managebook.OneInputDialog.OnOneInputDialogConfirmListener;
import com.hubahuma.mobile.profile.ProfileOrganizationActivity_;
import com.hubahuma.mobile.profile.ProfileParentsActivity_;
import com.hubahuma.mobile.profile.ProfileTeacherActivity_;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_search_result)
public class SearchResultActivity extends FragmentActivity implements
		SearchResultViewListener, PromptDialogListener,
		OnVerifyChildDialogConfirmListener {

	private SearchResultViewAdapter adapter;

	private List<UserEntity> userList = new ArrayList<UserEntity>();

	@ViewById
	ListView search_result_list;

	@ViewById
	TextView no_data_hint;

	@ViewById
	ProgressBar progress_bar;

	@Extra
	String search_word;

	@App
	MyApplication myApp;

	@RestService
	UserService userService;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private ImageButton targetFollow;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
		RestTemplate tpl = userService.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);
		tpl.setRequestFactory(s);
	}

	@AfterViews
	void init() {
		preLoadData();
		loadData();
	}

	@Background(delay = 0)
	void loadData() {
		// userList = getTestData();

		UserEntity user = new UserEntity();

		try {
			SearchTeacherParam param = new SearchTeacherParam();
			param.setName(search_word);
			param.setToken(myApp.getToken());
			SearchTeacherResp teacherResp = userService.searchTeacher(param);
			if (teacherResp == null || teacherResp.getResult() == null
					|| teacherResp.getResult().isEmpty()) {
				showToast("服务器数据返回异常", Toast.LENGTH_LONG);
			} else {
				for (int i = 0; i < teacherResp.getResult().size(); i++) {
					user.bind(teacherResp.getResult().get(i));
					userList.add(user);
				}
			}
		} catch (RestClientException e) {
			showToast("用户数据获取失败", Toast.LENGTH_LONG);
		}

		postLoadData();
	}

	@UiThread
	void preLoadData() {
		search_result_list.setVisibility(View.GONE);
		no_data_hint.setVisibility(View.GONE);
		progress_bar.setVisibility(View.VISIBLE);

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
	}

	@UiThread
	void postLoadData() {
		adapter = new SearchResultViewAdapter(getApplicationContext(),
				userList, this);
		search_result_list.setAdapter(adapter);
		progress_bar.setVisibility(View.GONE);

		if (userList == null || userList.isEmpty()) {
			no_data_hint.setVisibility(View.VISIBLE);
			search_result_list.setVisibility(View.GONE);
		} else {
			no_data_hint.setVisibility(View.GONE);
			search_result_list.setVisibility(View.VISIBLE);
		}
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	@Override
	public void onFollowClicked(ImageButton btn) {
		targetFollow = btn;

		FragmentManager fm = getSupportFragmentManager();
		VerifyChildDialog_ verifyDialog = new VerifyChildDialog_();
		verifyDialog.show(fm, "dialog_verify_child");
	}

	@Override
	public void onDialogConfirm(String message) {
		showLoadingDialog();
		handleFollow(targetFollow, message);
	}

	@Background
	void handleFollow(ImageButton btn, String message) {

		if (btn == null) {
			showPromptDialog("失败", "关注失败");
			return;
		}

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			return;
		}

		try {
			SendVerificationRequestParentParam param = new SendVerificationRequestParentParam();
			UserEntity teacher = (UserEntity) btn.getTag();
			if (teacher != null) {
				param.setTeacherId(teacher.getUserId());
				param.setRelationship(message);
				param.setToken(myApp.getToken());
				userService.sendVerificationRequestParent(param);
			} else {
				dismissLoadingDialog();
				showPromptDialog("失败", "数据加载失败");
				return;
			}
		} catch (RestClientException e) {
			dismissLoadingDialog();
			showPromptDialog("失败", "服务器连接异常");
			return;
		}

		dismissLoadingDialog();
		showToast("关注成功", Toast.LENGTH_SHORT);
		btn.setImageResource(R.drawable.followed);
		btn.setClickable(false);
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
	}

	@UiThread
	void showLoadingDialog() {
		loadingDialog.show(getSupportFragmentManager(), "dialog_loading");
	}

	@UiThread
	void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	@UiThread
	void showPromptDialog(String title, String content) {
		promptDialog.setTitle(title);
		promptDialog.setContent(content);
		promptDialog.show(getSupportFragmentManager(), "dialog_prompt");
	}

	@UiThread
	void dismissPromptDialog() {
		promptDialog.dismiss();
	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
	}

	@Override
	public void onPortraitClick(UserEntity user) {
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

	private List<UserEntity> getTestData() {
		List<UserEntity> list = new ArrayList<UserEntity>();
		for (int i = 1; i <= 4; i++) {
			UserEntity user = new UserEntity();
			user.setUserId("teacher#" + i);
			user.setType(UserType.TEACHER);
			user.setName("王萍" + i);
			user.setRemark("北京市第" + i + "中学");
			list.add(user);
		}
		return list;
	}
}
