package com.hubahuma.mobile.news;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class NoitceListViewAdapter extends BaseAdapter {

	private List<NoticeEntity> dataList;

	private LayoutInflater mInflater;

	public NoitceListViewAdapter(Context context, List<NoticeEntity> data) {
		this.mInflater = LayoutInflater.from(context);
		this.dataList = data;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		NoticeEntity item = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.item_msglist_notice, null);
			viewHolder = new ViewHolder();
			viewHolder.portrait = (ImageView) convertView
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

		if (UtilTools.isEmpty(item.getAuthor().getPortrait())) {
			viewHolder.portrait.setImageResource(R.drawable.default_portrait);
		} else {
			// TODO 判断真实头像
			viewHolder.portrait.setImageResource(R.drawable.default_portrait);
		}
		if (UtilTools.isEmpty(item.getImage())) {
			viewHolder.content_img.setVisibility(View.GONE);
		} else {
			// TODO 判断是否真的有图片
			viewHolder.content_img
					.setImageResource(R.drawable.teaching_diary_img);
		}
		viewHolder.author_name.setText(item.getAuthor().getName());
		viewHolder.content_txt.setText(item.getContent());
		viewHolder.message_date.setText(UtilTools.parseDate(item.getDate()));

		return convertView;
	}

	private class ViewHolder {
		ImageView portrait;
		TextView author_name;
		ImageView content_img;
		TextView content_txt;
		TextView message_date;
	}

}
