package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_location)
public class LocationActivity extends Activity {

	@ViewById
	TextView org_name, org_addr, org_phone;
	
	@Extra
	String name, address, phone;
	
	@AfterViews
	void init(){
		org_name.setText(name);
		org_addr.setText(address);
		org_phone.setText(phone);
	}
	
	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from LocationActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
	
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// }
}
