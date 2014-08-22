package com.hubahuma.mobile.news.contacts;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;

public class SearchResultViewAdapter extends BaseAdapter {

	private List<UserEntity> dataList;

	private LayoutInflater mInflater;

	public SearchResultViewAdapter(Context context, List<UserEntity> data) {

		this.dataList = data;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		UserEntity user = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_result_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.portrait = (ImageView) convertView
					.findViewById(R.id.head_portrait);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.remark);
			viewHolder.follow = (ImageButton) convertView
					.findViewById(R.id.follow);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// TODO 判断真实头像
		viewHolder.portrait.setImageResource(R.drawable.default_portrait);
		final String username = user.getUsername();
		viewHolder.name.setText(username);
		viewHolder.remark.setText(user.getRemark());
		viewHolder.follow.setTag(user);

		viewHolder.follow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((ImageButton) v).setBackgroundResource(R.drawable.followed);
				Toast.makeText(mInflater.getContext(), "关注成功",
						Toast.LENGTH_SHORT).show();
			}
		});

		return convertView;
	}

	static class ViewHolder {
		ImageView portrait;
		TextView name;
		TextView remark;
		ImageButton follow;
	}

}
