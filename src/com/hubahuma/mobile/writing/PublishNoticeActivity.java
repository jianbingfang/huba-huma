package com.hubahuma.mobile.writing;

import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.utils.ModelUtil;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_publish_notice)
public class PublishNoticeActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	ImageView portrait;

	@ViewById
	EditText title, content;

	@ViewById
	TextView name, date;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean publishSucc = false;

	@AfterViews
	void init() {
		// loadingDialog = new LoadingDialog_();
		// promptDialog = new PromptDialog_();
		// promptDialog.setDialogListener(this);
		name.setText(ModelUtil.getCurrentUser().getUsername());
		date.setText(UtilTools.ParseDate(new Date()));
	}

	@Click
	void btn_add_img() {

	}

	@Click
	void btn_submit() {
		showLoadingDialog();
		handlePublishNotice();
	}

	@UiThread
	void showLoadingDialog() {
		loadingDialog = new LoadingDialog_();
		loadingDialog.show(getSupportFragmentManager(), "dialog_loading");
	}

	@UiThread
	void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	@UiThread
	void showPromptDialog(String title, String content) {
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
		promptDialog.setTitle(title);
		promptDialog.setContent(content);
		promptDialog.show(getSupportFragmentManager(), "dialog_loading");
	}

	@UiThread
	void dismissPromptDialog() {
		promptDialog.dismiss();
	}

	@Background(delay = 1000)
	void handlePublishNotice() {
		publishSucc = publishNotice();
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "发布成功!");
		} else {
			showPromptDialog("错误", "发布失败!");
		}
	}

	boolean publishNotice() {
		// TODO 发送notice数据给后台
		return true;
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from PublishNoticeActivity");
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
