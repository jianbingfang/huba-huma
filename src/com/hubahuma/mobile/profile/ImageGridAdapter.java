package com.hubahuma.mobile.profile;

import java.util.ArrayList;
import java.util.List;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.profile.PublishedArticleDetailViewAdapter.ViewHolder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageGridAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<Drawable> imgList;

	public ImageGridAdapter(Context context, List<Drawable> dataList) {
		this.mInflater = LayoutInflater.from(context);
		this.imgList = dataList;
	}

	@Override
	public int getCount() {
		return imgList.size();
	}

	@Override
	public Object getItem(int position) {
		return imgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_img_grid, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.image.setImageDrawable(imgList.get(position));
		
		return convertView;
	}

	class ViewHolder {
		public ImageView image;
	}
}
