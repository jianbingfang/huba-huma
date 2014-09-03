package com.hubahuma.mobile.news.managebook;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.GroupEntity;

public class GroupManageViewAdapter extends BaseAdapter {

	private List<GroupEntity> dataList;

	private LayoutInflater mInflater;

	public GroupManageViewAdapter(Context context, List<GroupEntity> groupData) {

		this.dataList = groupData;
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

		GroupEntity entity = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_group_list, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(entity.getGroupName());

		return convertView;
	}

	static class ViewHolder {
		public TextView name;
	}

}
