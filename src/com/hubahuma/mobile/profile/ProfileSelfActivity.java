package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.profile.ChangeInfoActivity.InfoType;
import com.hubahuma.mobile.profile.WriteCommentActivity.CommentType;
import com.hubahuma.mobile.utils.ModelUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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
	TextView remark, qualification, phone, name_title, name, name2, address, id, email,
			tag, introduction_title, introduction, open_time;

	@ViewById
	CheckBox notify_switch, search_switch;

	@ViewById
	RelativeLayout introduction_layout, open_time_layout, address_layout,
			img_manage_layout;

	@ViewById
	ProgressBar progress_bar;

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

		name.setText(ModelUtil.getCurrentUser().getUsername());
		name2.setText(ModelUtil.getCurrentUser().getUsername());
		remark.setText(ModelUtil.getCurrentUser().getRemark());
		id.setText(ModelUtil.getCurrentUser().getId());
		
		notify_switch.setChecked(true);
		search_switch.setChecked(true);
	}

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
	}

	@Click
	void img_manage_layout() {
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
