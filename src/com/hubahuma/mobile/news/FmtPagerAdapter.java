package com.hubahuma.mobile.news;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public class FmtPagerAdapter extends FragmentPagerAdapter {
	
	private List<BaseFragment> fragmentsList;
	private FragmentManager fm;

	public FmtPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}

	public FmtPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
		super(fm);
		this.fragmentsList = fragments;
		this.fm = fm;
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentsList.get(arg0);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// 知道当前是第几页，但是每次滑动后可能会调用多次
		// 这个方法是重点
		super.setPrimaryItem(container, position, object);
		fragmentsList.get(position).show();
	}

	@Override
	public int getItemPosition(Object object) {
		// 加此方法可以使viewpager可以进行刷新
		return PagerAdapter.POSITION_NONE;
	}

	// 使用此方法刷新数据 每次都要NEW一个新的List，不然没有刷新效果
	public void setFragments(ArrayList<BaseFragment> fragments) {
		if (this.fragmentsList != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragmentsList) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragmentsList = fragments;
		notifyDataSetChanged();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// 注释自带的销毁方法防止页面被销毁
		// 这个方法是重点
		// super.destroyItem(container, position, object);
	}
}