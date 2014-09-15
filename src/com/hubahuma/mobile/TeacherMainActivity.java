package com.hubahuma.mobile;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.RestClientException;

import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.hubahuma.mobile.discovery.DiscoveryActivity_;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.RegisterParentParam;
import com.hubahuma.mobile.entity.service.RegisterTeacherParam;
import com.hubahuma.mobile.entity.service.RegisterTeacherResp;
import com.hubahuma.mobile.info.InfoActivity_;
import com.hubahuma.mobile.news.NewsActivity_;
import com.hubahuma.mobile.news.managebook.ManageBookActivity_;
import com.hubahuma.mobile.news.managebook.SimpleManageBookActivity_;
import com.hubahuma.mobile.profile.ProfileSelfActivity_;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.writing.MyNoticeActivity_;
import com.hubahuma.mobile.writing.WritingActivity_;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_teacher_main)
public class TeacherMainActivity extends TabActivity {

	@ViewById(R.id.main_tab_news_counter)
	TextView mainTabNewsCounter;

	@ViewById(R.id.main_tab_group)
	RadioGroup radioGroup;

	TabHost tabHost;

	@Bean
	MyErrorHandler myErrorHandler;

	@RestService
	UserService userService;

	@App
	MyApplication myApp;
	
	@AfterInject
	void preProc() {
		userService.setRestErrorHandler(myErrorHandler);
	}

	@AfterViews
	void init() {

		mainTabNewsCounter.setVisibility(View.VISIBLE);
		mainTabNewsCounter.setText("10");

		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, SimpleManageBookActivity_.class);
		spec = tabHost.newTabSpec("managebook").setIndicator("managebook")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MyNoticeActivity_.class);
		spec = tabHost.newTabSpec("writing").setIndicator("writing")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ProfileSelfActivity_.class);
		spec = tabHost.newTabSpec("profile").setIndicator("profile")
				.setContent(intent);
		tabHost.addTab(spec);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.main_tab_manage_book:
					tabHost.setCurrentTabByTag("managebook");
					break;
				case R.id.main_tab_write_notice:
					tabHost.setCurrentTabByTag("writing");
					break;
				case R.id.main_tab_profile:
					tabHost.setCurrentTabByTag("profile");
					break;
				default:
					tabHost.setCurrentTabByTag("writing");
					break;
				}
			}
		});
		
		tabHost.setCurrentTabByTag("writing");

	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				this.exitApp();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private long exitTime = 0;

	/**
	 * 退出程序
	 */
	private void exitApp() {
		// 判断2次点击事件时间
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(TeacherMainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT)
					.show();
			exitTime = System.currentTimeMillis();
		} else {
			this.finish();
		}
	}
}
