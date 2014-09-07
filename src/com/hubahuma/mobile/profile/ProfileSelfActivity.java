package com.hubahuma.mobile.profile;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.SelectPicPopupWindow;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.profile.ChangeInfoActivity.InfoType;
import com.hubahuma.mobile.profile.WriteCommentActivity.CommentType;
import com.hubahuma.mobile.utils.ModelUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@NoTitle
@EActivity(R.layout.activity_profile_self)
public class ProfileSelfActivity extends Activity {

	@ViewById
	ImageButton btn_setup, setup;

	@ViewById
	ImageView portrait;

	@ViewById
	TextView remark, qualification, phone, name_title, name, name2, address,
			id, email, tag, introduction_title, introduction, open_time;

	@ViewById
	CheckBox notify_switch, search_switch;

	@ViewById
	RelativeLayout introduction_layout, open_time_layout, address_layout,
			img_manage_layout;

	@ViewById
	ProgressBar progress_bar;

	ArrayList<String> tagList;

	SelectPicPopupWindow menuWindow;

	@AfterViews
	void init() {

		switch (ModelUtil.getCurrentUser().getType()) {
		case UserType.PARENTS:
			qualification.setVisibility(View.GONE);
			introduction_layout.setVisibility(View.GONE);
			open_time_layout.setVisibility(View.GONE);
			address_layout.setVisibility(View.GONE);
			img_manage_layout.setVisibility(View.GONE);

			name_title.setText("姓名");

			break;
		case UserType.TEACHER:
			open_time_layout.setVisibility(View.GONE);
			address_layout.setVisibility(View.GONE);
			img_manage_layout.setVisibility(View.GONE);

			name_title.setText("姓名");
			introduction_title.setText("教师介绍");
			break;

		case UserType.ORGANIZTION:
			remark.setVisibility(View.GONE);
			break;
		case UserType.ADMIN:
			break;
		}

		// clearFields();

		preLoadData();
		loadData();
	}

	void clearFields() {
		remark.setVisibility(View.GONE);
		qualification.setVisibility(View.GONE);
		phone.setText("");
		name.setText("");
		name2.setText("");
		address.setText("");
		id.setText("");
		email.setText("");
		tag.setText("");
		introduction.setText("");
		open_time.setText("");
	}

	@Background(delay = 1000)
	void loadData() {
		tagList = getTestData();
		postLoadData();
	}

	private ArrayList<String> getTestData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("英语");
		list.add("早教");
		list.add("少儿英语");
		return list;
	}

	@UiThread
	void preLoadData() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postLoadData() {
		name.setText(ModelUtil.getCurrentUser().getUsername());
		name2.setText(ModelUtil.getCurrentUser().getUsername());
		remark.setText(ModelUtil.getCurrentUser().getRemark());
		id.setText(ModelUtil.getCurrentUser().getId());

		tag.setText(transTagList(tagList));

		notify_switch.setChecked(true);
		search_switch.setChecked(true);

		progress_bar.setVisibility(View.GONE);
	}

	@CheckedChange
	void notify_switch(CompoundButton button, boolean isChecked) {

	}

	@CheckedChange
	void search_switch(CompoundButton button, boolean isChecked) {

	}

	@LongClick(R.id.portrait)
	void onPortraitLongClick() {
		menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
		menuWindow.showAtLocation(this.findViewById(R.id.profile_self),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	@LongClick(R.id.custom_bg)
	void onCustomBgLongClick() {
		menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
		menuWindow.showAtLocation(this.findViewById(R.id.profile_self),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

	@OnActivityResult(ActivityCode.LOCATION_ACTIVITY)
	void onReturnFromLocationActivity() {
		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void name_layout() {
		startChangeInfoActivity(InfoType.NAME, name.getText().toString());
	}

	@Click
	void email_layout() {
		startChangeInfoActivity(InfoType.EMAIL, email.getText().toString());
	}

	@Click
	void introduction_layout() {
		startChangeInfoActivity(InfoType.INTRODUCTION, introduction.getText()
				.toString());
	}

	@Click
	void open_time_layout() {
		startChangeInfoActivity(InfoType.OPENTIME, open_time.getText()
				.toString());
	}

	@Click
	void address_layout() {
		startChangeInfoActivity(InfoType.ADDRESS, address.getText().toString());
	}

	@Click
	void tag_layout() {
		Intent intent = new Intent(this, ChangeTagActivity_.class);
		intent.putStringArrayListExtra("tagList", tagList);
		startActivityForResult(intent, ActivityCode.CHANGE_TAG_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.CHANGE_TAG_ACTIVITY)
	void onReturnFromChangeTagActivit(int resultCode, Intent intent) {
		if (resultCode > 0) {
			ArrayList<String> list = intent
					.getStringArrayListExtra("newTagList");
			if (list != null) {
				tag.setText(transTagList(list));
				tagList = list;
			}
		}
	}

	private String transTagList(List<String> tags) {
		String str = "";
		if (tags != null && tags.size() > 0) {
			for (String t : tags) {
				str += t + "  ";
			}
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	@Click
	void img_manage_layout() {
		Intent intent = new Intent(this, SchoolImgManageActivity_.class);
		startActivityForResult(intent, ActivityCode.SCHOOL_IMG_MANAGE_ACTIVITY);
	}

	void startChangeInfoActivity(int type, String value) {
		Intent intent = new Intent(this, ChangeInfoActivity_.class);
		intent.putExtra("type", type);
		intent.putExtra("value", value);
		startActivityForResult(intent, ActivityCode.CHANGE_INFO_ACTIVITY);
	}

	@Click
	void password_reset_layout() {
		Intent intent = new Intent(this, PasswordResetActivity_.class);
		startActivityForResult(intent, ActivityCode.PASSWORD_RESET_ACTIVITY);
	}

	@Click
	void btn_change_phone() {
		Intent intent = new Intent(this, ChangePhoneActivity_.class);
		intent.putExtra("currNumber", phone.getText().toString());
		startActivityForResult(intent, ActivityCode.CHANGE_PHONE_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.CHANGE_PHONE_ACTIVITY)
	void onReturnFromChangePhoneActivity(int resultCode, Intent intent) {
		String newNumber = intent.getStringExtra("newNumber");
		if (newNumber != null)
			phone.setText(newNumber);
	}

	@OnActivityResult(ActivityCode.CHANGE_INFO_ACTIVITY)
	void onReturnFromChangeInfoActivity(int resultCode, Intent intent) {
		if (resultCode == 0) {
			return;
		}

		int infoType = intent.getIntExtra("type", -1);
		if (infoType == -1) {
			Toast.makeText(getApplicationContext(), "返回信息错误",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String newValue = intent.getStringExtra("newValue");
		switch (infoType) {
		case InfoType.NAME:
			name.setText(newValue);
			name2.setText(newValue);
			break;
		case InfoType.EMAIL:
			email.setText(newValue);
			break;
		case InfoType.INTRODUCTION:
			introduction.setText(newValue);
			break;
		case InfoType.OPENTIME:
			open_time.setText(newValue);
			break;
		case InfoType.ADDRESS:
			address.setText(newValue);
			break;
		default:
			break;
		}
	}

	@Click
	void btn_logout() {
		Toast.makeText(getApplicationContext(), "退出", Toast.LENGTH_SHORT)
				.show();
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ProfileOrganiztionActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
