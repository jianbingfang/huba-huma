package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.profile.WriteCommentActivity.CommentType;

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
import android.widget.Switch;
import android.widget.TextView;

@NoTitle
@EActivity(R.layout.activity_profile_organizaion_self)
public class ProfileOrganizaionSelfActivity extends Activity {

	@ViewById
	ImageButton btn_setup, setup;

	@ViewById
	ImageView portrait;

	@ViewById
	TextView phone, name, name2, address, id, email, tag, introduce, open_time;

	@ViewById
	CheckBox notify_switch, search_switch;

	@ViewById
	ProgressBar progress_bar;

	@AfterViews
	void init() {
		notify_switch.setChecked(true);
		search_switch.setChecked(true);
	}

	@OnActivityResult(ActivityCode.LOCATION_ACTIVITY)
	void onReturnFromLocationActivity() {
		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void name_panel() {

	}

	@Click
	void img_manage_panel() {

	}

	@Click
	void password_reset_panel() {
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
