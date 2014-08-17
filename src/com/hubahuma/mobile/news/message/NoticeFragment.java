package com.hubahuma.mobile.news.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hubahuma.mobile.R;

@EFragment(R.layout.fragment_notice)
public class NoticeFragment extends BaseFragment {

	private boolean isInit; // 是否可以开始加载数据

	private List<HashMap<String, Object>> listItem = new LinkedList<HashMap<String, Object>>();

	@ViewById(R.id.notice_msg_list)
	PullToRefreshListView mPullRefreshListView;

	@ViewById
	ListView list;
	
	private MyAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return null;
	}
	
	@AfterViews
	void init() {
		isInit = true;
		// 设定下拉监听函数
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								NoticeFragment.this.getActivity()
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// Do work to refresh the list here.
						new GetDataTask().execute();
					}
				});

		mPullRefreshListView.setMode(Mode.PULL_FROM_START);// 设置底部下拉刷新模式

		listItem = getTestData();// 获取LIST数据
		adapter = new MyAdapter(this.getActivity().getApplicationContext());

		// 设置适配器
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(adapter);
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, List<HashMap<String, Object>>> {

		// 后台处理部分
		@Override
		protected List<HashMap<String, Object>> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
			int num = new Random().nextInt(3)+1;
			try {
				for (int i = 0; i < num; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map = new HashMap<String, Object>();
					map.put("portrait", "");
					map.put("author_name", new Random().nextInt(50) + "号管理员");
					map.put("content_img", "");
					map.put("content_txt",
							"中新网8月14日电 据共同社报道，日本 15 日将迎来第 69 个战败纪念日。届时，日本政府主办的全国“战殁者追悼仪式”将在东京都举行。");
					map.put("message_date", "今天 14:26");
					mapList.add(map);
				}
			} catch (Exception e) {
				NoticeFragment.this.getActivity().setTitle("map出错了");
				return null;
			}

			return mapList;
		}

		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(List<HashMap<String, Object>> resultList) {
			// 在头部增加新添内容
			try {
				if (resultList != null && !resultList.isEmpty()) {
					for (HashMap<String, Object> result : resultList) {
						listItem.add(0, result);
					}
					// 通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
					adapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
					msgListener
							.OnNewMessageShowed(OnNewMessageListener.NOTICE_MESSAGE);
				}
			} catch (Exception e) {
				NoticeFragment.this.getActivity().setTitle(e.getMessage());
			}

			super.onPostExecute(resultList);
		}
	}
	
	private class ViewHolder {
		ImageButton portrait;
		TextView author_name;
		ImageView content_img;
		TextView content_txt;
		TextView message_date;
	}

	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return listItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.msglist_item_notice,
						null);
				viewHolder = new ViewHolder();
				viewHolder.portrait = (ImageButton) convertView
						.findViewById(R.id.portrait);
				viewHolder.content_img = (ImageView) convertView
						.findViewById(R.id.content_img);
				viewHolder.author_name = (TextView) convertView
						.findViewById(R.id.author_name);
				viewHolder.content_txt = (TextView) convertView
						.findViewById(R.id.content_txt);
				viewHolder.message_date = (TextView) convertView
						.findViewById(R.id.message_date);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// TODO 判断真实头像
			viewHolder.portrait.setImageResource(R.drawable.default_portrait);
			// TODO 判断是否真的有图片
			viewHolder.content_img.setVisibility(View.GONE);

			viewHolder.author_name.setText((String) listItem.get(position).get(
					"author_name"));
			viewHolder.content_txt.setText((String) listItem.get(position).get(
					"content_txt"));
			viewHolder.message_date.setText((String) listItem.get(position).get(
					"message_date"));

			return convertView;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		/* 初始化控件 */
	}

	@Override
	public void onResume() {
		super.onResume();
		// 判断当前fragment是否显示
		if (getUserVisibleHint()) {
			showData();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// 每次切换fragment时调用的方法
		if (isVisibleToUser) {
			showData();
			// Toast.makeText(getActivity().getApplicationContext(), "notice",
			// Toast.LENGTH_SHORT).show();
		}
	}

	private void showData() {
		if (isInit) {
			isInit = false;// 加载数据完成
			// 加载各种数据
		}
	}

	@Override
	public void show() {

	}

	private List<HashMap<String, Object>> getTestData() {
		List<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		InputStream inputStream;
		try {
			inputStream = this.getActivity().getAssets()
					.open("notice_msg_items.txt");
			String json = readTextFile(inputStream);
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				map = new HashMap<String, Object>();
				map.put("portrait", array.getJSONObject(i)
						.getString("portrait"));
				map.put("author_name",
						array.getJSONObject(i).getString("author_name"));
				map.put("content_img",
						array.getJSONObject(i).getString("content_img"));
				map.put("content_txt",
						array.getJSONObject(i).getString("content_txt"));
				map.put("message_date",
						array.getJSONObject(i).getString("message_date"));
				list.add(0, map);
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public String readTextFile(InputStream inputStream) {
		String readedStr = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				readedStr += tmp;
			}
			br.close();
			inputStream.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return readedStr;
	}
}