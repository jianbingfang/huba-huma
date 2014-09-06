package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import android.app.TabActivity;
import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.hubahuma.mobile.discovery.DiscoveryActivity_;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.info.InfoActivity_;
import com.hubahuma.mobile.news.NewsActivity_;
import com.hubahuma.mobile.profile.ProfileSelfActivity_;
import com.hubahuma.mobile.utils.ModelUtil;
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

	@AfterInject
	void preProc() {
		SDKInitializer.initialize(getApplicationContext());
	}

	@AfterViews
	void setValue() {

		UserEntity user = new UserEntity();
		user.setId("000001");
		user.setRemark("none");
		user.setUsername("当前用户");
		// user.setType(UserType.ADMIN);
		user.setType(UserType.ORGANIZTION);
		// user.setType(UserType.TEACHER);
		// user.setType(UserType.PARENTS);

		ModelUtil.setCurrentUser(user);

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

	}
}
