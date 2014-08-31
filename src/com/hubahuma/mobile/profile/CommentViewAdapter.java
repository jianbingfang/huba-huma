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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.CommentEntity;
import com.hubahuma.mobile.entity.DiaryEntity;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class CommentViewAdapter extends BaseAdapter {

	private List<CommentEntity> dataList;

	private LayoutInflater mInflater;

	public CommentViewAdapter(Context context, List<CommentEntity> data) {
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

		CommentEntity item = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.comment_item, null);
			viewHolder = new ViewHolder();
			viewHolder.portrait = (ImageView) convertView.findViewById(R.id.portrait);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.remark = (TextView) convertView.findViewById(R.id.remark);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			viewHolder.scoreBar = (LinearLayout) convertView.findViewById(R.id.score_bar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// TODO 判断真实头像
		viewHolder.portrait.setImageResource(R.drawable.default_portrait);
		viewHolder.name.setText(item.getUser().getUsername());
		viewHolder.remark.setText(item.getUser().getRemark());
		viewHolder.date.setText(UtilTools.ParseDate(item.getDate()));
		viewHolder.content.setText(item.getContent());
		for(int i = 0; i < item.getScore(); i++){
			ImageView star = (ImageView)viewHolder.scoreBar.getChildAt(i);
			star.setImageResource(R.drawable.star_red);
		}
		
		return convertView;
	}

	static class ViewHolder {
		public ImageView portrait;
		public TextView name;
		public TextView remark;
		public TextView content;
		public TextView date;
		public LinearLayout scoreBar;
	}

}
