package com.hubahuma.mobile;

import java.util.Random;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.RestClientException;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthResp;
import com.hubahuma.mobile.entity.service.RegisterParent;
import com.hubahuma.mobile.entity.service.RegisterParentResp;
import com.hubahuma.mobile.entity.service.RegisterTeacher;
import com.hubahuma.mobile.entity.service.RegisterTeacherResp;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.SmsService;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_register)
public class RegisterActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	EditText username, password, password_again, name, phone;

	@ViewById
	TextView error_info;

	@ViewById
	Button btn_register;

	@ViewById
	RadioGroup user_type;

	// @RestService
	// SmsService smsService;

	@Pref
	SharedPrefs_ prefs;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	// @AfterInject
	// void afterInject() {
	// smsService.setRestErrorHandler(myErrorHandler);
	// }

	@AfterViews
	void init() {
		error_info.setText("");

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
	}

	@Click
	void btn_register() {
		if (checkInput()) {
			Intent intent = new Intent(this, AuthCodeActivity_.class);
			intent.putExtra("username", username.getText().toString().trim());
			intent.putExtra("password", password.getText().toString().trim());
			intent.putExtra("name", name.getText().toString().trim());
			intent.putExtra("phone", phone.getText().toString().trim());
			switch (user_type.getCheckedRadioButtonId()) {
			case R.id.radio_parent:
				intent.putExtra("type", UserType.PARENTS);
				break;
			case R.id.radio_teacher:
				intent.putExtra("type", UserType.TEACHER);
				break;
			}
			startActivityForResult(intent, ActivityCode.AUTH_CODE_ACTIVITY);
		}
	}

	private boolean checkInput() {
		if (UtilTools.isEmpty(username.getText().toString().trim())) {
			error_info.setText("用户名不能为空！");
			return false;
		}
		if (UtilTools.isEmpty(password.getText().toString())) {
			error_info.setText("密码不能为空！");
			return false;
		}
		if (UtilTools.isEmpty(password_again.getText().toString())) {
			error_info.setText("密码确认不能为空！");
			return false;
		}
		if (!password.getText().toString()
				.equals(password_again.getText().toString())) {
			error_info.setText("密码两次输入不一致，请重新输入！");
			return false;
		}
		if (UtilTools.isEmpty(name.getText().toString().trim())) {
			error_info.setText("姓名不能为空！");
			return false;
		}
		if (!UtilTools.isMobileNumber(phone.getText().toString().trim())) {
			error_info.setText("手机号码格式不正确！");
			return false;
		}
		error_info.setText("");
		return true;
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

	@OnActivityResult(ActivityCode.AUTH_CODE_ACTIVITY)
	void onReturnFromMainActivity(Intent intent, int resultCode) {
		if (resultCode == 1) {
			this.setResult(1, intent);
			this.finish();
		}
	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
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

}
