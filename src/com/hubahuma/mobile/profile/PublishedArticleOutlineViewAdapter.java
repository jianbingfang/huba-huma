package com.hubahuma.mobile.profile;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.ArticleEntity;
import com.hubahuma.mobile.entity.DiaryEntity;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class PublishedArticleOutlineViewAdapter extends BaseAdapter {

	private List<ArticleEntity> dataList;

	private LayoutInflater mInflater;

	public PublishedArticleOutlineViewAdapter(Context context, List<ArticleEntity> data) {
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

		ArticleEntity item = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.article_outline_item, null);
			viewHolder = new ViewHolder();
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			viewHolder.articleImg = (ImageView) convertView.findViewById(R.id.article_img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.date.setText(UtilTools.ParseDate(item.getDate()));
		// TODO 判断真实图片
		viewHolder.articleImg.setBackgroundResource(R.drawable.teaching_diary_img);
		viewHolder.title.setText(item.getTitle());
		
		return convertView;
	}

	static class ViewHolder {
		public TextView date;
		public ImageView articleImg;
		public TextView title;
	}

}
