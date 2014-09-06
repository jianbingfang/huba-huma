package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@NoTitle
@EActivity(R.layout.activity_password_reset)
public class PasswordResetActivity extends FragmentActivity implements PromptDialogListener{

	@ViewById
	EditText old_password, new_password, new_password_again;

	@ViewById
	TextView error_info;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean publishSucc = false;
	
	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
		error_info.setText("");
	}
	
	@Click
	void btn_save() {
		if(check()){
			showLoadingDialog();
			handleResetPassword();
		}
	}
	
	@Background(delay = 1000)
	void handleResetPassword() {
		publishSucc = resetPassword();
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "密码修改成功!");
		} else {
			showPromptDialog("错误", "密码修改失败!");
		}
	}

	private boolean resetPassword() {
		// TODO Auto-generated method stub
		return true;
	}

	boolean check() {
		if (UtilTools.isEmpty(old_password.getText().toString())) {
			error_info.setText("原密码不能为空！");
			return false;
		}
		if (UtilTools.isEmpty(new_password.getText().toString())) {
			error_info.setText("新密码不能为空！");
			return false;
		}
		if (UtilTools.isEmpty(new_password_again.getText().toString())) {
			error_info.setText("重复输入不能为空！");
			return false;
		}
		if (!new_password.getText().toString()
				.equals(new_password_again.getText().toString())) {
			error_info.setText("新密码两次输入不一致，请重新输入！");
			return false;
		}
		if (new_password.getText().toString()
				.equals(old_password.getText().toString())) {
			error_info.setText("新密码与原密码一致，请重新输入！");
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

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from PasswordResetActivity");
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