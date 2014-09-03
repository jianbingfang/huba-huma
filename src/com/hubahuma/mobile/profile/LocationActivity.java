package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
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

	@ViewById(R.id.map_view)
	MapView mMapView;

	@Extra
	String name, address, phone;

	@AfterInject
	void preProc(){
		SDKInitializer.initialize(getApplicationContext());
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@AfterViews
	void init() {
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
