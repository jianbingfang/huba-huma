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

@NoTitle
@EActivity(R.layout.activity_main)
public class MainActivity extends TabActivity {

	@ViewById(R.id.main_tab_news_counter)
	private TextView mainTabNewsCounter;

	private TabHost tabHost;

	@AfterViews
	void setValue(){
		mainTabNewsCounter.setVisibility(View.VISIBLE);
		mainTabNewsCounter.setText("10");
	}
	
	@AfterInject
	void init(){
		
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, NewsActivity.class);
		spec = tabHost.newTabSpec("news").setIndicator("news")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, WritingActivity.class);
		spec = tabHost.newTabSpec("writing").setIndicator("writing")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, DiscoveryActivity.class);
		spec = tabHost.newTabSpec("discovery").setIndicator("discovery")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, InfoActivity.class);
		spec = tabHost.newTabSpec("info").setIndicator("info")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ProfileActivity.class);
		spec = tabHost.newTabSpec("profile").setIndicator("profile")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		RadioGroup radioGroup = (RadioGroup) this
				.findViewById(R.id.main_tab_group);
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
	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		mainTabNewsCounter = (TextView) findViewById(R.id.main_tab_news_counter);
//		mainTabNewsCounter.setVisibility(View.VISIBLE);
//		mainTabNewsCounter.setText("10");
//		tabHost = this.getTabHost();
//		TabHost.TabSpec spec;
//		Intent intent;
//		intent = new Intent().setClass(this, NewsActivity.class);
//		spec = tabHost.newTabSpec("news").setIndicator("news")
//				.setContent(intent);
//		tabHost.addTab(spec);
//
//		intent = new Intent().setClass(this, WritingActivity.class);
//		spec = tabHost.newTabSpec("writing").setIndicator("writing")
//				.setContent(intent);
//		tabHost.addTab(spec);
//
//		intent = new Intent().setClass(this, DiscoveryActivity.class);
//		spec = tabHost.newTabSpec("discovery").setIndicator("discovery")
//				.setContent(intent);
//		tabHost.addTab(spec);
//
//		intent = new Intent().setClass(this, InfoActivity.class);
//		spec = tabHost.newTabSpec("info").setIndicator("info")
//				.setContent(intent);
//		tabHost.addTab(spec);
//
//		intent = new Intent().setClass(this, ProfileActivity.class);
//		spec = tabHost.newTabSpec("profile").setIndicator("profile")
//				.setContent(intent);
//		tabHost.addTab(spec);
//
//		tabHost.setCurrentTab(0);
//		RadioGroup radioGroup = (RadioGroup) this
//				.findViewById(R.id.main_tab_group);
//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.main_tab_news:
//					tabHost.setCurrentTabByTag("news");
//					break;
//				case R.id.main_tab_writing:
//					tabHost.setCurrentTabByTag("writing");
//					break;
//				case R.id.main_tab_discovery:
//					tabHost.setCurrentTabByTag("discovery");
//					break;
//				case R.id.main_tab_info:
//					tabHost.setCurrentTabByTag("info");
//					break;
//				case R.id.main_tab_profile:
//					tabHost.setCurrentTabByTag("profile");
//					break;
//				default:
//					tabHost.setCurrentTabByTag("news");
//					break;
//				}
//			}
//		});
//	}
}
