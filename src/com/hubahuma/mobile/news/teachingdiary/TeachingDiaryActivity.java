package com.hubahuma.mobile.news.teachingdiary;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_teaching_diary)
public class TeachingDiaryActivity extends Activity {

	@ViewById
	ListView diary_list;

	@ViewById
	ProgressBar progress_bar;
	
	@AfterViews
	void init() {
		showProgressBar();
		loadData();
	}
	
	@Background(delay = 2000)
	void loadData(){
		
		
		hideProgressBar();
	}
	
	@UiThread
	void showProgressBar(){
		progress_bar.setVisibility(View.VISIBLE);
	}
	
	@UiThread
	void hideProgressBar(){
		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from TeachingDiaryActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
