package com.hubahuma.mobile.info;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.news.message.BaseFragment;
import com.hubahuma.mobile.news.message.FmtPagerAdapter;
import com.hubahuma.mobile.news.message.GroupFragment_;
import com.hubahuma.mobile.news.message.NoticeFragment_;
import com.hubahuma.mobile.news.message.OnNewMessageListener;
import com.hubahuma.mobile.news.message.ParentsFragment_;
import com.hubahuma.mobile.news.message.TeacherFragment_;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_info)
public class InfoActivity extends FragmentActivity implements
		OnNewMessageListener {

	@ViewById
	ImageView redspot_news, redspot_around, redspot_focus, redspot_point;

	@ViewById
	TextView tab_txt_news, tab_txt_around, tab_txt_focus, tab_txt_point;

	@ViewById
	View tab_news, tab_around, tab_focus, tab_point;

	@ViewById
	ViewPager vPager;

	@ColorRes
	int message_tab_txt, message_tab_bg, message_tab_bg_selected;

	private NoticeFragment_ noticeFragment;
	private TeacherFragment_ teacherFragment;
	private ParentsFragment_ parentsFragment;
	private GroupFragment_ groupFragment;

	private List<BaseFragment> fragments;
	private int currIndex = 0;

	private List<TextView> listTabTxt;
	private List<View> listTab;

	@AfterViews
	void init() {

		redspot_around.setVisibility(View.VISIBLE);
		redspot_focus.setVisibility(View.VISIBLE);
		redspot_point.setVisibility(View.VISIBLE);

		listTabTxt = new ArrayList<TextView>();
		listTabTxt.add(tab_txt_news);
		listTabTxt.add(tab_txt_around);
		listTabTxt.add(tab_txt_focus);
		listTabTxt.add(tab_txt_point);

		listTab = new ArrayList<View>();
		listTab.add(tab_news);
		listTab.add(tab_around);
		listTab.add(tab_focus);
		listTab.add(tab_point);

		InitViewPager();

	}

	private void InitViewPager() {

		fragments = new ArrayList<BaseFragment>();

		noticeFragment = new NoticeFragment_();
		teacherFragment = new TeacherFragment_();
		parentsFragment = new ParentsFragment_();
		groupFragment = new GroupFragment_();

		fragments.add(noticeFragment);
		fragments.add(teacherFragment);
		fragments.add(parentsFragment);
		fragments.add(groupFragment);

		vPager.setAdapter(new FmtPagerAdapter(getSupportFragmentManager(),
				fragments));
		vPager.setOnPageChangeListener(new MyOnPageChangeListener());
		vPager.setCurrentItem(0);
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}


	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int targetIndex) {

			listTab.get(targetIndex)
					.setBackgroundColor(message_tab_bg_selected);
			listTabTxt.get(targetIndex).setTextColor(Color.WHITE);
			listTab.get(currIndex).setBackgroundColor(message_tab_bg);
			listTabTxt.get(currIndex).setTextColor(message_tab_txt);

			currIndex = targetIndex;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
	}

	@Click
	void tab_notice() {
		vPager.setCurrentItem(0);
	}

	@Click
	void tab_teacher() {
		vPager.setCurrentItem(1);
	}

	@Click
	void tab_parents() {
		vPager.setCurrentItem(2);
	}

	@Click
	void tab_group() {
		vPager.setCurrentItem(3);
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from MessageActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	@Override
	public void OnNewMessageReady(int type) {
		switch (type) {
		case OnNewMessageListener.NOTICE_MESSAGE:
			redspot_news.setVisibility(View.VISIBLE);
			break;
		case OnNewMessageListener.TEACHER_MESSAGE:
			redspot_around.setVisibility(View.VISIBLE);
			break;
		case OnNewMessageListener.PARENTS_MESSAGE:
			redspot_focus.setVisibility(View.VISIBLE);
			break;
		case OnNewMessageListener.GROUP_MESSAGE:
			redspot_point.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void OnNewMessageShowed(int type) {
		switch (type) {
		case OnNewMessageListener.NOTICE_MESSAGE:
			redspot_news.setVisibility(View.GONE);
			break;
		case OnNewMessageListener.TEACHER_MESSAGE:
			redspot_around.setVisibility(View.GONE);
			break;
		case OnNewMessageListener.PARENTS_MESSAGE:
			redspot_focus.setVisibility(View.GONE);
			break;
		case OnNewMessageListener.GROUP_MESSAGE:
			redspot_point.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

}
