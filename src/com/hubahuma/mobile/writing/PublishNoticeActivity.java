package com.hubahuma.mobile.writing;

import java.util.Date;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.SelectImgPopupWindow;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.utils.GlobalVar;
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

	@ViewById
	ImageButton btn_submit;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean publishSucc = false;

	SelectImgPopupWindow menuWindow;
	
	private NoticeEntity newNotice;

	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
		btn_submit.setVisibility(View.GONE);
		name.setText(GlobalVar.getCurrentUser().getUsername());
		date.setText(UtilTools.parseDate(new Date()));
	}

	@AfterTextChange(R.id.content)
	void onContentTextChange(Editable text) {
		if (UtilTools.isEmpty(text.toString())) {
			btn_submit.setVisibility(View.GONE);
		} else {
			btn_submit.setVisibility(View.VISIBLE);
		}
	}

	@Click
	void btn_add_img() {
		menuWindow = new SelectImgPopupWindow(this, itemsOnClick);
		menuWindow.showAtLocation(this.findViewById(R.id.publish_notice_layout),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	@Click
	void btn_submit() {
		NoticeEntity notice = new NoticeEntity();
		notice.setUser(GlobalVar.getCurrentUser());
		notice.setTitle(title.getText().toString().trim());
		notice.setContent(content.getText().toString().trim());
		notice.setDate(new Date());
		notice.setImage(null);
		showLoadingDialog();
		handlePublishNotice(notice);
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
	void handlePublishNotice(NoticeEntity notice) {
		publishSucc = publishNotice(notice);
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "发布成功!");
			newNotice = notice;
		} else {
			showPromptDialog("错误", "发布失败!");
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				Toast.makeText(getApplicationContext(), "拍照",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_pick_photo:
				Toast.makeText(getApplicationContext(), "选取照片",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	boolean publishNotice(NoticeEntity notice) {
		// TODO 发送notice数据给后台
		return true;
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from PublishNoticeActivity");
		if (publishSucc) {
			intent.putExtra("newNotice", newNotice);
			this.setResult(1, intent);
		}else{
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
}
