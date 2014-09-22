package com.hubahuma.mobile.profile;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.discovery.VerifyChildDialog_;
import com.hubahuma.mobile.entity.service.AuthParam;
import com.hubahuma.mobile.entity.service.UpdateParentParam;
import com.hubahuma.mobile.entity.service.UpdateTeacherParam;
import com.hubahuma.mobile.profile.InputNewTagDialog.InputNewTagDialogConfirmListener;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.view.FlowLayout;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_change_tag)
public class ChangeTagActivity extends FragmentActivity implements
		PromptDialogListener, InputNewTagDialogConfirmListener {

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	EditText search_input;

	@ViewById
	ImageButton btn_submit;

	@ViewById
	FlowLayout added_tag_layout, hot_tag_layout;

	@App
	MyApplication myApp;

	@Extra
	ArrayList<String> tagList;

	ArrayList<String> hotTagList;

	@RestService
	UserService userService;

	@Bean
	MyErrorHandler myErrorHandler;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
		RestTemplate tpl = userService.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);
		tpl.setRequestFactory(s);

	}

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private NewTagButtonView newTagBtn;

	private boolean publishSucc = false;

	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		newTagBtn = NewTagButtonView_.build(getApplicationContext());
		newTagBtn.tag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				InputNewTagDialog_ dialog = new InputNewTagDialog_();
				dialog.show(fm, "dialog_new_tag");
			}
		});

		updateAddedListView();

		preLoadData();
		loadData();
	}

	@UiThread
	void updateAddedListView() {
		added_tag_layout.removeAllViews();
		if (tagList != null) {
			for (String tag : tagList) {
				TagItemView tagView = TagItemView_
						.build(getApplicationContext());
				tagView.bind(tag);
				tagView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TagItemView view = (TagItemView) v;
						tagList.remove(view.tag.getText().toString());
						hotTagList.add(view.tag.getText().toString());
						updateAddedListView();
						updateHotListView();
					}
				});
				added_tag_layout.addView(tagView);
			}
		}

		added_tag_layout.addView(newTagBtn);

	}

	@UiThread
	void updateHotListView() {
		hot_tag_layout.removeAllViews();
		if (hotTagList != null) {
			for (String tag : hotTagList) {
				TagItemView tagView = TagItemView_
						.build(getApplicationContext());
				tagView.bind(tag);
				tagView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TagItemView view = (TagItemView) v;
						hotTagList.remove(view.tag.getText().toString());
						tagList.add(view.tag.getText().toString());
						updateAddedListView();
						updateHotListView();
					}
				});
				hot_tag_layout.addView(tagView);
			}
		}
	}

	@Background(delay = 1000)
	void loadData() {
		hotTagList = getTestData();
		postLoadData();
	}

	private ArrayList<String> getTestData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("音乐");
		list.add("数学");
		list.add("英语");
		list.add("语文");
		list.add("体育");
		list.add("舞蹈");
		list.add("钢琴");
		list.add("吉他");

		list.removeAll(tagList);

		return list;
	}

	@UiThread
	void preLoadData() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postLoadData() {
		updateHotListView();
		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void btn_add_img() {

	}

	@Click
	void btn_submit() {
		showLoadingDialog();
		handleChangeTag();
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

	@Background(delay = 0)
	void handleChangeTag() {
		publishSucc = changeTag();
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "修改成功！");
		} else {
			showPromptDialog("错误", "修改失败！");
		}
	}

	private boolean changeTag() {
		if (UserType.TEACHER.equals(myApp.getCurrentUser().getType())) {
			try {
				UpdateTeacherParam param = new UpdateTeacherParam();
				param.setTeacherId(myApp.getCurrentUser().getUserId());
				param.setTags(tagList);
				param.setToken(myApp.getToken());
				userService.updateTeacher(param);
			} catch (RestClientException e) {
				dismissLoadingDialog();
				showToast("服务器连接异常", Toast.LENGTH_LONG);
				return false;
			}
		}
		/*else if(UserType.PARENTS.equals(myApp.getCurrentUser().getType())){
			try {
				UpdateParentParam param = new UpdateParentParam();
				param.setParentId(myApp.getCurrentUser().getUserId());
				param.setTags(tagList);
				userService.updateParent(param);
			} catch (RestClientException e) {
				dismissLoadingDialog();
				showToast("服务器连接异常", Toast.LENGTH_LONG);
				return false;
			}
		}*/
		return true;
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		if (publishSucc) {
			intent.putStringArrayListExtra("newTagList", tagList);
			this.setResult(1, intent);
		} else {
			this.setResult(0, intent);
		}
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
		if (publishSucc) {
			btn_back();
		}
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
	}

	@Override
	public void onDialogConfirm(String inputText) {

		if (tagList.contains(inputText)) {
			showToast("该标签已存在", Toast.LENGTH_SHORT);
			return;
		} else {
			tagList.add(inputText);
			updateAddedListView();
		}

		if (hotTagList.remove(inputText)) {
			updateHotListView();
		}

	}
}
