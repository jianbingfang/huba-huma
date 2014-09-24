package com.hubahuma.mobile.writing;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class ReceiptListViewAdapter extends BaseAdapter {

	public interface ReceiptListViewListener {
		public void onPortraitClick(UserEntity user);
	}

	private List<UserEntity> userList;

	// private List<UserEntity> readUserList;

	private int unreadNum;

	private LayoutInflater mInflater;

	private ReceiptListViewListener listener;

	public ReceiptListViewAdapter(Context context, List<UserEntity> userList,
			int unreadNum, ReceiptListViewListener listener) {
		this.mInflater = LayoutInflater.from(context);
		this.userList = userList;
		this.unreadNum = unreadNum;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		UserEntity item = userList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_receipt, null);
			viewHolder = new ViewHolder();
			viewHolder.portrait = (ImageView) convertView
					.findViewById(R.id.portrait);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.remark);
			viewHolder.read_hint = (TextView) convertView
					.findViewById(R.id.read_hint);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (UtilTools.isEmpty(item.getPortrait())) {
			viewHolder.portrait.setImageResource(R.drawable.default_portrait);
		} else {
			try {
				Bitmap img = UtilTools.string2Bitmap(item.getPortrait());
				viewHolder.portrait.setImageBitmap(img);
			} catch (Exception e) {
				viewHolder.portrait
						.setImageResource(R.drawable.default_portrait);
				Log.e("Base64", e.getMessage());
			}

		}
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
		if (position < unreadNum) {
			viewHolder.read_hint.setText("已读");
			viewHolder.read_hint
					.setBackgroundResource(R.drawable.receipt_green_bg);
		} else {
			viewHolder.read_hint.setText("未读");
			viewHolder.read_hint
					.setBackgroundResource(R.drawable.receipt_red_bg);
		}

		return convertView;
	}

	static class ViewHolder {
		public ImageView portrait;
		public TextView name;
		public TextView remark;
		public TextView read_hint;
	}

}
