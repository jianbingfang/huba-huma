package com.hubahuma.mobile.news;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

@NoTitle
@EActivity(R.layout.activity_message)
public class MessageActivity extends FragmentActivity implements
		OnNewMessageListener {

	private static final int TAB_NOTICE = 0;
	private static final int TAB_TEACHER = 1;
	private static final int TAB_PARENTS = 2;
	private static final int TAB_GROUP = 3;

	@ViewById
	ImageView redspot_notice, redspot_teacher, redspot_parents, redspot_group;

	@ViewById
	TextView tab_txt_notice, tab_txt_teacher, tab_txt_parents, tab_txt_group;

	@ViewById
	View tab_notice, tab_teacher, tab_parents, tab_group;

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

	// private List<View> listViews;
	private List<TextView> listTabTxt;
	private List<View> listTab;

	@AfterViews
	void init() {

		redspot_teacher.setVisibility(View.VISIBLE);
		redspot_parents.setVisibility(View.VISIBLE);
		redspot_group.setVisibility(View.VISIBLE);

		listTabTxt = new ArrayList<TextView>();
		listTabTxt.add(tab_txt_notice);
		listTabTxt.add(tab_txt_teacher);
		listTabTxt.add(tab_txt_parents);
		listTabTxt.add(tab_txt_group);

		listTab = new ArrayList<View>();
		listTab.add(tab_notice);
		listTab.add(tab_teacher);
		listTab.add(tab_parents);
		listTab.add(tab_group);

		InitViewPager();

		// vPager = (ViewPager) findViewById(R.id.vPager);
		// LayoutInflater mInflater = getLayoutInflater();
		// listViews = new ArrayList<View>();
		// listViews.add(mInflater.inflate(R.layout.lay1, null));
		// listViews.add(mInflater.inflate(R.layout.lay2, null));
		// listViews.add(mInflater.inflate(R.layout.lay3, null));
		// listViews.add(mInflater.inflate(R.layout.fragment_group, null));
		// vPager.setAdapter(new MyPagerAdapter(listViews));
		// vPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// vPager.setCurrentItem(0);
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

	// /**
	// * 头标点击监听
	// */
	// public class MyOnClickListener implements View.OnClickListener {
	// private int index = 0;
	//
	// public MyOnClickListener(int i) {
	// index = i;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// vPager.setCurrentItem(index);
	// }
	// };

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int targetIndex) {
			// switch (targetIndex) {
			// case TAB_NOTICE:
			// redspot_notice.setVisibility(View.GONE);
			// break;
			// case TAB_TEACHER:
			// redspot_teacher.setVisibility(View.GONE);
			// break;
			// case TAB_PARENTS:
			// redspot_parents.setVisibility(View.GONE);
			// break;
			// case TAB_GROUP:
			// redspot_group.setVisibility(View.GONE);
			// break;
			// default:
			// break;
			// }

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
			redspot_notice.setVisibility(View.VISIBLE);
			break;
		case OnNewMessageListener.TEACHER_MESSAGE:
			redspot_teacher.setVisibility(View.VISIBLE);
			break;
		case OnNewMessageListener.PARENTS_MESSAGE:
			redspot_parents.setVisibility(View.VISIBLE);
			break;
		case OnNewMessageListener.GROUP_MESSAGE:
			redspot_group.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void OnNewMessageShowed(int type) {
		switch (type) {
		case OnNewMessageListener.NOTICE_MESSAGE:
			redspot_notice.setVisibility(View.GONE);
			break;
		case OnNewMessageListener.TEACHER_MESSAGE:
			redspot_teacher.setVisibility(View.GONE);
			break;
		case OnNewMessageListener.PARENTS_MESSAGE:
			redspot_parents.setVisibility(View.GONE);
			break;
		case OnNewMessageListener.GROUP_MESSAGE:
			redspot_group.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

}
