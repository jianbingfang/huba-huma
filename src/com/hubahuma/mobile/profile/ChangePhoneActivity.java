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

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.utils.ModelUtil;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_change_phone)
public class ChangePhoneActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	TextView hint, error_info;

	@ViewById
	EditText number;

	@ViewById
	ImageButton btn_submit;

	@Extra
	String currNumber;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean publishSucc = false;

	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
		hint.setText(hint.getText().toString() + currNumber);
	}

	boolean checkNumber() {
		if (!UtilTools.isMobileNumber(number.getText().toString().trim())) {
			error_info.setText("手机号码格式不正确，请重新输入！");
			return false;
		}
		error_info.setText("");
		return true;
	}

	@Click
	void btn_add_img() {

	}

	@Click
	void btn_submit() {

		if (!checkNumber()) {
			return;
		}

		showLoadingDialog();
		handleChangePhone();
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
			showPromptDialog("提示", "修改成功！");
		} else {
			showPromptDialog("错误", "修改失败！");
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