package com.hubahuma.mobile.writing;

import java.util.Date;
import java.util.List;
import java.util.Random;

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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.CommentEntity;
import com.hubahuma.mobile.entity.DiaryEntity;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class MyNoitceListViewAdapter extends BaseAdapter {

	private List<NoticeEntity> dataList;

	private LayoutInflater mInflater;

	public MyNoitceListViewAdapter(Context context, List<NoticeEntity> data) {
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

		NoticeEntity item = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_my_notice, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.remark);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.image);
			viewHolder.day = (TextView) convertView.findViewById(R.id.day);
			viewHolder.month = (TextView) convertView.findViewById(R.id.month);
			viewHolder.receipt_info = (TextView) convertView
					.findViewById(R.id.receipt_info);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(item.getUser().getUsername());
		viewHolder.remark.setText(item.getUser().getRemark());
		viewHolder.content.setText(item.getContent());
		if(UtilTools.isEmpty(item.getImage())){
			viewHolder.image.setVisibility(View.GONE);
		}else{
			// TODO 加入真实图片
			viewHolder.image.setVisibility(View.VISIBLE);
			viewHolder.image.setImageResource(R.drawable.teaching_diary_img);
		}
		viewHolder.day.setText(UtilTools.getDate(item.getDate()));
		viewHolder.month.setText(UtilTools.getMonth(item.getDate()));
		Random rand = new Random();
		viewHolder.receipt_info.setText("收到" + rand.nextInt(30) + "条家长回执");

		return convertView;
	}

	static class ViewHolder {
		public TextView name;
		public TextView remark;
		public TextView content;
		public ImageView image;
		public TextView day;
		public TextView month;
		public TextView receipt_info;
	}

}
