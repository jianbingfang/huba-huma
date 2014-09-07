package com.hubahuma.mobile.profile;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.DiaryEntity;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class TagListViewAdapter extends BaseAdapter {

	private List<String> dataList;

	private LayoutInflater mInflater;

	public TagListViewAdapter(Context context, List<String> data) {
		this.mInflater = LayoutInflater.from(context);
		this.dataList = data;
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

		String item = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_tag, null);
			viewHolder = new ViewHolder();
			viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tag.setText(item);

		return convertView;
	}

	static class ViewHolder {
		public TextView tag;
	}

}
