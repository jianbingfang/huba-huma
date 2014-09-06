package com.hubahuma.mobile.profile;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.DiaryEntity;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_school_img_manage)
public class SchoolImgManageActivity extends Activity {

	@ViewById
	GridView img_grid;

	@ViewById
	RelativeLayout img_layout;

	@ViewById
	ProgressBar progress_bar;

	private List<Drawable> imgList = new ArrayList<Drawable>();

	private ImageGridAdapter adapter;

	@AfterViews
	void init() {
		showProgressBar();
		loadData();
	}

	@ItemClick(R.id.img_grid)
	public void onImgGridItemClicked(int position) {
		Toast.makeText(getApplicationContext(), "click:" + position,
				Toast.LENGTH_SHORT).show();
	}
	
	@ItemLongClick(R.id.img_grid)
	public void onImgGridItemLongClicked(int position) {
		Toast.makeText(getApplicationContext(), "long:" + position,
				Toast.LENGTH_SHORT).show();
	}

	@Background(delay = 1000)
	void loadData() {
		imgList = getTestData();
		hideProgressBar();
		adapter = new ImageGridAdapter(getApplicationContext(), imgList);
		img_grid.setAdapter(adapter);
	}

	private List<Drawable> getTestData() {
		List<Drawable> list = new ArrayList<Drawable>();
		for (int i = 1; i <= 17; i++) {
			Drawable img = getResources().getDrawable(
					R.drawable.teaching_diary_img);
			list.add(img);
		}
		return list;
	}

	@UiThread
	void showProgressBar() {
		img_layout.setVisibility(View.GONE);
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void hideProgressBar() {
		img_layout.setVisibility(View.VISIBLE);
		progress_bar.setVisibility(View.GONE);
	}
	
	@Click
	void btn_upload(){
		//TODO
	}

	@Click
	void btn_submit(){
		//TODO
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
