package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.service.BindPhoneParam;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_change_phone)
public class ChangePhoneActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	TextView info, error_info, hint;

	@ViewById
	EditText number, auth_code;

	@ViewById
	Button send_code;

	@ViewById
	ImageButton btn_submit;

	@Extra
	String currNumber;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean publishSucc = false;

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
		info.setText(info.getText().toString() + currNumber);
		error_info.setText("");
		send_code.setVisibility(View.VISIBLE);
		hint.setVisibility(View.GONE);
	}

	@UiThread
	void afterSmsSendFail() {
		send_code.setVisibility(View.VISIBLE);
		hint.setVisibility(View.GONE);
	}

	private int timeCount = 60;

	@UiThread
	void afterSmsSendSucc() {
		send_code.setVisibility(View.GONE);
		hint.setVisibility(View.VISIBLE);

		timeCount = 60;
		hint.setText("接收短信大约需要" + timeCount + "秒");

		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				--timeCount;
				hint.setText("接收短信大约需要" + timeCount + "秒");
				if (timeCount > 0) {
					handler.postDelayed(this, 1000);
				} else {
					handler.removeCallbacks(this);
					send_code.setVisibility(View.VISIBLE);
					hint.setVisibility(View.GONE);
				}
			}
		};

		handler.postDelayed(runnable, 1000);
	}

	boolean checkNumber() {
		if (!UtilTools.isMobileNumber(number.getText().toString().trim())) {
			error_info.setText("手机号码格式不正确，请重新输入");
			return false;
		}
		error_info.setText("");
		return true;
	}

	boolean checkCode() {
		if (UtilTools.isEmpty(auth_code.getText().toString().trim())) {
			error_info.setText("验证码不能为空");
			return false;
		}
		error_info.setText("");
		return true;
	}

	@Click
	void send_code() {
		if (checkNumber()) {
			sendAuthCode();
		}
	}

	@Background
	void sendAuthCode() {
		showLoadingDialog();

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			afterSmsSendFail();
			return;
		}

		BindPhoneParam bindPhoneParam = new BindPhoneParam();
		bindPhoneParam.setPhone(number.getText().toString().trim());

		try {
			userService.bindPhone(bindPhoneParam);
		} catch (RestClientException e) {
			dismissLoadingDialog();
			showToast("连接异常，短信发送失败", Toast.LENGTH_LONG);
			afterSmsSendFail();
			return;
		}

		dismissLoadingDialog();
		showToast("验证短信已发送", Toast.LENGTH_SHORT);
		afterSmsSendSucc();

	}

	@Click
	void btn_add_img() {

	}

	@Click
	void btn_submit() {

		if (checkNumber() && checkCode()) {
			showLoadingDialog();
			handleChangePhone();
		}
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

	@Background(delay = 1000)
	void handleChangePhone() {
		publishSucc = changePhone();
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "修改成功");
		} else {
			showPromptDialog("错误", "修改失败");
		}
	}

	boolean changePhone() {
		// TODO 发送notice数据给后台
		return true;
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ChangePhoneActivity");
		if (publishSucc)
			intent.putExtra("newNumber", number.getText().toString());
		this.setResult(0, intent);
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
}