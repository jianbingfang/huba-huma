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
import com.hubahuma.mobile.entity.service.RegisterParent;
import com.hubahuma.mobile.entity.service.RegisterTeacher;
import com.hubahuma.mobile.entity.service.RegisterTeacherResp;
import com.hubahuma.mobile.info.InfoActivity_;
import com.hubahuma.mobile.news.NewsActivity_;
import com.hubahuma.mobile.profile.ProfileSelfActivity_;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.writing.WritingActivity_;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_main)
public class MainActivity extends TabActivity {

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

		System.out.println("NewsActivit:"+myApp.getCurrentUser());
		
		mainTabNewsCounter.setVisibility(View.VISIBLE);
		mainTabNewsCounter.setText("10");

		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, NewsActivity_.class);
		spec = tabHost.newTabSpec("news").setIndicator("news")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, WritingActivity_.class);
		spec = tabHost.newTabSpec("writing").setIndicator("writing")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, DiscoveryActivity_.class);
		spec = tabHost.newTabSpec("discovery").setIndicator("discovery")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, InfoActivity_.class);
		spec = tabHost.newTabSpec("info").setIndicator("info")
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
				case R.id.main_tab_news:
					tabHost.setCurrentTabByTag("news");
					break;
				case R.id.main_tab_writing:
					tabHost.setCurrentTabByTag("writing");
					break;
				case R.id.main_tab_discovery:
					tabHost.setCurrentTabByTag("discovery");
					break;
				case R.id.main_tab_info:
					tabHost.setCurrentTabByTag("info");
					break;
				case R.id.main_tab_profile:
					tabHost.setCurrentTabByTag("profile");
					break;
				default:
					tabHost.setCurrentTabByTag("news");
					break;
				}
			}
		});

//		registerTeacher();
	}

	@Background
	void registerTeacher() {
		// TODO Auto-generated method stub
		RegisterTeacher teacher = new RegisterTeacher();
		List<String> classes = new ArrayList<String>();
		classes.add("class-1");
		classes.add("class-2");
		teacher.setClasses(classes);
		teacher.setContactInfo("contactInfo");
		teacher.setDescription("description");
		teacher.setName("teacher-name");
		teacher.setPassword("password");
		try {
			RegisterTeacherResp resp = userService.registerTeacher(teacher);
			System.out.println("=============================="
					+ resp.getToken());

		} catch (RestClientException e) {
			Log.e("Rest Error", e.getMessage());
		}
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
			Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT)
					.show();
			exitTime = System.currentTimeMillis();
		} else {
			this.finish();
		}
	}
}
