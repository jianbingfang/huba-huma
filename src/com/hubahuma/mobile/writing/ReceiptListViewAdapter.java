package com.hubahuma.mobile.writing;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.UserEntity;

public class ReceiptListViewAdapter extends BaseAdapter {

	public interface ReceiptListViewListener {
		public void onPortraitClick(UserEntity user);
	}

	private List<UserEntity> dataList;

	private LayoutInflater mInflater;

	private ReceiptListViewListener listener;

	private boolean isRead;

	public ReceiptListViewAdapter(Context context, List<UserEntity> data,
			boolean hasRead, ReceiptListViewListener listener) {
		this.mInflater = LayoutInflater.from(context);
		this.dataList = data;
		this.isRead = hasRead;
		this.listener = listener;
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

		UserEntity item = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isRead) {
				convertView = mInflater.inflate(R.layout.item_receipt_read,
						null);
			} else {
				convertView = mInflater.inflate(R.layout.item_receipt_unread,
						null);
			}
			viewHolder = new ViewHolder();
			viewHolder.portrait = (ImageView) convertView
					.findViewById(R.id.portrait);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.remark);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// TODO 加入真实头像
		viewHolder.portrait.setImageResource(R.drawable.default_portrait);
		viewHolder.portrait.setTag(item);
		viewHolder.portrait.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserEntity user = (UserEntity) v.getTag();
				listener.onPortraitClick(user);
			}
		});
		viewHolder.name.setText(item.getName());
		viewHolder.remark.setText(item.getRemark());

		return convertView;
	}

	static class ViewHolder {
		public ImageView portrait;
		public TextView name;
		public TextView remark;
	}

}
