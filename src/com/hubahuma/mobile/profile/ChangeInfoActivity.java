package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_change_info)
public class ChangeInfoActivity extends FragmentActivity implements
		PromptDialogListener {

	public class InfoType {
		public static final int NAME = 0;
		public static final int EMAIL = 1;
		public static final int INTRODUCTION = 2;
		public static final int OPENTIME = 3;
		public static final int ADDRESS = 4;
	}

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean publishSucc = false;

	@App
	MyApplication myApp;
	
	@Extra
	int type;

	@Extra
	String value;

	@ViewById
	EditText input;

	@ViewById
	ImageButton btn_clear;

	@ViewById
	TextView title, error_info, hint;

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
	
	@AfterViews
	void init() {

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		switch (type) {
		case InfoType.NAME:
			if (myApp.getCurrentUser().getType() == UserType.ORGANIZTION) {
				title.setText("编辑机构名称");
				hint.setText("4-30个字符，支持中英文、数字");
			} else {
				title.setText("编辑姓名");
				hint.setText("");
			}
			break;
		case InfoType.EMAIL:
			title.setText("编辑电子邮箱");
			hint.setText("");
			break;
		case InfoType.INTRODUCTION:
			if (myApp.getCurrentUser().getType() == UserType.ORGANIZTION) {
				title.setText("编辑机构说明");
				hint.setText("4-30个字符，支持中英文、数字");
			} else {
				title.setText("编辑教师介绍");
				hint.setText("");
			}
			input.setSingleLine(false);
			input.setHeight(200);
			input.setMaxHeight(200);
			input.setMinHeight(200);
			break;
		case InfoType.OPENTIME:
			title.setText("编辑开放时间");
			hint.setText("");
			break;
		case InfoType.ADDRESS:
			title.setText("编辑地点");
			hint.setText("");
			break;
		}

		input.setText(value);
		error_info.setText("");
	}

	@Click
	void btn_submit() {

		switch (type) {
		case InfoType.NAME:
			if (!checkName())
				return;
			break;
		case InfoType.EMAIL:
			if (!checkEmail())
				return;
			break;
		case InfoType.INTRODUCTION:
			if (!checkIntroduction())
				return;
			break;
		case InfoType.OPENTIME:
			if (!checkOpentime())
				return;
			break;
		case InfoType.ADDRESS:
			if (!checkAddress())
				return;
			break;
		}

		showLoadingDialog();
		handleChangeInfo();
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

	@Background(delay = 1000)
	void handleChangeInfo() {
		publishSucc = changeInfo(type);
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "修改成功");
		} else {
			showPromptDialog("错误", "修改失败");
		}
	}

	boolean changeInfo(int infoType) {
		switch (infoType) {
		case InfoType.NAME:
			break;
		case InfoType.EMAIL:
			break;
		case InfoType.INTRODUCTION:
			break;
		case InfoType.OPENTIME:
			break;
		case InfoType.ADDRESS:
			break;
		}
		return true;
	}

	@AfterTextChange(R.id.input)
	void onInputChange(Editable text) {
		if (UtilTools.isEmpty(text.toString())) {
			btn_clear.setVisibility(View.GONE);
		} else {
			btn_clear.setVisibility(View.VISIBLE);
		}
	}

	@Click
	void btn_clear() {
		input.setText("");
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		if (publishSucc) {
			intent.putExtra("type", type);
			intent.putExtra("newValue", input.getText().toString().trim());
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

	private boolean checkName() {
		if (UtilTools.isEmpty(input.getText().toString().trim())) {
			error_info.setText("输入不能为空");
			return false;
		}
		if (input.getText().toString().trim().equals(value)) {
			error_info.setText("输入与原值一样，请重新输入");
			return false;
		}
		if (input.getText().toString().trim().length() > 30) {
			error_info.setText("长度不能超过30字符");
			return false;
		}
		error_info.setText("");
		return true;
	}

	private boolean checkEmail() {
		if (UtilTools.isEmpty(input.getText().toString().trim())) {
			error_info.setText("输入不能为空");
			return false;
		}
		if (input.getText().toString().trim().equals(value)) {
			error_info.setText("输入与原值一样，请重新输入");
			return false;
		}
		if (!UtilTools.isEmail(input.getText().toString().trim())) {
			error_info.setText("邮箱格式错误");
			return false;
		}
		error_info.setText("");
		return true;
	}

	private boolean checkIntroduction() {
		if (input.getText().toString().trim().equals(value)) {
			error_info.setText("输入与原值一样，请重新输入");
			return false;
		}
		if (input.getText().toString().trim().length() > 200) {
			error_info.setText("长度不能超过200字符");
			return false;
		}
		error_info.setText("");
		return true;
	}

	private boolean checkOpentime() {
		if (input.getText().toString().trim().equals(value)) {
			error_info.setText("输入与原值一样，请重新输入");
			return false;
		}
		if (input.getText().toString().trim().length() > 30) {
			error_info.setText("长度不能超过30字符");
			return false;
		}
		error_info.setText("");
		return true;
	}

	private boolean checkAddress() {
		if (UtilTools.isEmpty(input.getText().toString().trim())) {
			error_info.setText("输入不能为空");
			return false;
		}
		if (input.getText().toString().trim().equals(value)) {
			error_info.setText("输入与原值一样，请重新输入");
			return false;
		}
		if (input.getText().toString().trim().length() > 30) {
			error_info.setText("长度不能超过30字符");
			return false;
		}
		error_info.setText("");
		return true;
	}
}
