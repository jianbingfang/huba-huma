package com.hubahuma.mobile.news.message;

import android.app.Activity;
import android.support.v4.app.ListFragment;

public abstract class BaseFragment extends ListFragment {

	protected OnNewMessageListener msgListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			msgListener = (OnNewMessageListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnNewMessageListener");
		}
	}

	/**
	 * 显示页面数据
	 */
	public abstract void show();
}