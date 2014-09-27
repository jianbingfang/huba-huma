package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.entity.service.BindPhoneParam;
import com.hubahuma.mobile.entity.service.RegisterParentParam;
import com.hubahuma.mobile.entity.service.RegisterParentResp;
import com.hubahuma.mobile.entity.service.RegisterTeacherParam;
import com.hubahuma.mobile.entity.service.RegisterTeacherResp;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

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

	private String userId = null;
	private String token = null;

	@RestService
	UserService userService;

	@Pref
	SharedPrefs_ prefs;

	@Bean
	MyErrorHandler myErrorHandler;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

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
		error_info.setText("");

		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
	}

	@Click
	void btn_register() {
		if (checkInput()) {
			btn_register.setEnabled(false);

			switch (user_type.getCheckedRadioButtonId()) {
			case R.id.radio_parent:
				handleRegisterParent();
				break;
			case R.id.radio_teacher:
				handleRegisterTeacher();
				break;
			}

		}
	}

	@UiThread
	void afterSmsSendFail() {
		btn_register.setEnabled(true);
	}

	@UiThread
	void afterSmsSendSucc() {
		btn_register.setEnabled(true);
		Intent intent = new Intent(this, AuthCodeActivity_.class);
		intent.putExtra("username", username.getText().toString().trim());
		intent.putExtra("password", password.getText().toString().trim());
		intent.putExtra("name", name.getText().toString().trim());
		intent.putExtra("phone", phone.getText().toString().trim());
		intent.putExtra("userId", userId);
		intent.putExtra("token", token);

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

	@Background
	void sendAuthCode() {
		showLoadingDialog();

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		BindPhoneParam bindPhoneParam = new BindPhoneParam();
		bindPhoneParam.setPhone(phone.getText().toString().trim());
		bindPhoneParam.setUserId(userId);
		bindPhoneParam.setToken(token);
		Log.d("bindPhoneParam", UtilTools.object2json(bindPhoneParam));
		try {
			userService.bindPhone(bindPhoneParam);
		} catch (RestClientException e) {
			showToast("服务器连接异常", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		dismissLoadingDialog();
		showToast("验证短信已发送", Toast.LENGTH_SHORT);
		afterSmsSendSucc();

	}

	@Background
	void handleRegisterParent() {

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			return;
		}

		showLoadingDialog();
		RegisterParentResp resp = null;

		RegisterParentParam parent = new RegisterParentParam();

		parent.setUsername(username.getText().toString().trim());
		parent.setPassword(password.getText().toString().trim());
		parent.setName(name.getText().toString().trim());
		parent.setPhone(phone.getText().toString().trim());

		try {
			resp = userService.registerParent(parent);
		} catch (RestClientException e) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			afterSmsSendFail();
			Log.e("Rest Error", e.getMessage() + this.getClass().getName());
			return;
		}

		if (resp == null) {
			showToast("数据返回错误", Toast.LENGTH_LONG);
			afterSmsSendFail();
			dismissLoadingDialog();
			return;
		}

		dismissLoadingDialog();
		if (resp.isOk()) {
			userId = resp.getUser().getUserId();
			token = resp.getToken();
			afterSmsSendSucc();
		} else {
			showPromptDialog("注册失败", resp.getReason());
			afterSmsSendFail();
		}
	}

	@Background
	void handleRegisterTeacher() {

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			return;
		}

		showLoadingDialog();
		RegisterTeacherResp resp = null;

		RegisterTeacherParam teacher = new RegisterTeacherParam();

		teacher.setUsername(username.getText().toString().trim());
		teacher.setPassword(password.getText().toString().trim());
		teacher.setName(name.getText().toString().trim());
		teacher.setPhone(phone.getText().toString().trim());

		try {
			resp = userService.registerTeacher(teacher);
		} catch (RestClientException e) {
			showToast("服务器验证错误", Toast.LENGTH_LONG);
			afterSmsSendFail();
			dismissLoadingDialog();
			Log.e("Rest Error", e.getMessage() + ". "
					+ this.getClass().getName());
			return;
		}

		if (resp == null) {
			showToast("数据返回错误", Toast.LENGTH_LONG);
			afterSmsSendFail();
			dismissLoadingDialog();
			return;
		}

		dismissLoadingDialog();
		if (resp.isOk()) {
			userId = resp.getUser().getUserId();
			token = resp.getToken();
			afterSmsSendSucc();
		} else {
			showPromptDialog("注册失败", resp.getReason());
			afterSmsSendFail();
		}
	}

	private boolean checkInput() {
		if (UtilTools.isEmpty(username.getText().toString().trim())) {
			error_info.setText("用户名不能为空");
			return false;
		}
		if (UtilTools.isEmpty(password.getText().toString())) {
			error_info.setText("密码不能为空");
			return false;
		}
		if (UtilTools.isEmpty(password_again.getText().toString())) {
			error_info.setText("密码确认不能为空");
			return false;
		}
		if (!password.getText().toString()
				.equals(password_again.getText().toString())) {
			error_info.setText("密码两次输入不一致，请重新输入");
			return false;
		}
		if (UtilTools.isEmpty(name.getText().toString().trim())) {
			error_info.setText("姓名不能为空");
			return false;
		}
		if (!UtilTools.isMobileNumber(phone.getText().toString().trim())) {
			error_info.setText("手机号码格式不正确");
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
		btn_register.setEnabled(true);
		if (resultCode == 1) {
			this.setResult(1, intent);
			intent.putExtra("username", username.getText().toString().trim());
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
