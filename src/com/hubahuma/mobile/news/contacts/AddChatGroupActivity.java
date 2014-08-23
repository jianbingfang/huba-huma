package com.hubahuma.mobile.news.contacts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.PromptDialog;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.NewsActivity.ActivityCode;
import com.hubahuma.mobile.utils.UtilTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_add_chat_group)
public class AddChatGroupActivity extends FragmentActivity {

	@ViewById(R.id.search_input)
	EditText searchBox;

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	TextView prompt_info;

	private PromptDialog_ dialog;

	@AfterViews
	void init() {
		dialog = new PromptDialog_();
	}

	@SuppressLint("ShowToast")
	@Click
	void btn_add_chat_group() {
		String text = searchBox.getText().toString().trim();
		if (UtilTools.isEmpty(text)) {
			// Toast.makeText(getApplicationContext(), "群名称不能为空，请重新输入",
			// Toast.LENGTH_SHORT);
			showPrompt("群名称不能为空，请重新输入", null);
			return;
		}

		handleAddGroup(text);
	}

	@Background
	void handleAddGroup(String groupName) {
		showProgressBar();
		boolean result = createChatGroup(groupName, null);
		hideProgressBar();
		if (result) {
			Intent intent = new Intent();
			intent.setClass(this, InviteUserActivity_.class);
			startActivityForResult(intent, ActivityCode.INVITE_USER_ACTIVITY);
		} else {
			showPrompt("创建失败，该名称已存在", null);
		}
	}

	@OnActivityResult(ActivityCode.INVITE_USER_ACTIVITY)
	void onInviteUerActivityResult(int resultCode, Intent data) {
		searchBox.setText("");
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ContactsActivity");
		this.setResult(0, intent);
		this.finish();
		Log.d("Return", "return from InviteUserActivity");
	}

	boolean createChatGroup(String groupName, UserEntity admin) {
		// TODO 与后台通信
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@UiThread
	void showProgressBar() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void hideProgressBar() {
		progress_bar.setVisibility(View.GONE);
	}

	@UiThread
	void showPrompt(String title, String subTitle) {
		// dialog.setTitle(title);
		// dialog.setSubtitle(subTitle);
		// FragmentManager fm = getSupportFragmentManager();
		// dialog.show(fm, "prompt dialog");
		
		prompt_info.setText(title);
		prompt_info.setVisibility(View.VISIBLE);
	}

	@Click
	void btn_back() {

		// String str = searchBox.getText().toString().trim();
		// if (!UtilTools.isEmpty(str)) {
		// searchBox.getText().clear();
		// return;
		// }

		Intent intent = getIntent();
		intent.putExtra("result", "returned from ContactsActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

}
