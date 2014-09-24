package com.hubahuma.mobile.writing;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class NoitceListViewAdapter extends BaseAdapter {

	public interface NoitceListViewListener {
		public void onPortraitClick(UserEntity user);

		public boolean onNoticeClick(String id);
	}

	private List<NoticeEntity> dataList;

	private LayoutInflater mInflater;

	private NoitceListViewListener listener;

	public NoitceListViewAdapter(Context context, List<NoticeEntity> data,
			NoitceListViewListener listener) {
		this.mInflater = LayoutInflater.from(context);
		this.dataList = data;
		this.listener = listener;
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

		if (UtilTools.isEmpty(item.getAuthorPhoto())) {
			viewHolder.portrait.setImageResource(R.drawable.default_portrait);
		} else {

			try {
				Bitmap img = UtilTools.string2Bitmap(item.getAuthorPhoto());
				viewHolder.portrait.setImageBitmap(img);
			} catch (Exception e) {
				viewHolder.portrait
						.setImageResource(R.drawable.default_portrait);
				Log.e("Base64", e.getMessage());
			}

		}

		viewHolder.portrait.setTag(item.getAuthor());
		viewHolder.portrait.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserEntity user = (UserEntity) v.getTag();
				listener.onPortraitClick(user);
			}
		});

		if (item.getPhotos() == null || item.getPhotos().isEmpty()
				|| UtilTools.isEmpty(item.getPhotos().get(0))) {
			viewHolder.content_img.setVisibility(View.GONE);
		} else {
			Bitmap img;
			try {
				img = UtilTools.string2Bitmap(item.getPhotos().get(0));
				viewHolder.content_img.setImageBitmap(img);
				viewHolder.content_img.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				viewHolder.content_img.setVisibility(View.GONE);
				Log.e("Base64", e.getMessage());
			}
		}
		viewHolder.author_name.setText(item.getAuthor());
		viewHolder.content_txt.setText(item.getText());
		viewHolder.content_txt.setTag(item.getNoticeId());
		viewHolder.content_txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = (String) v.getTag();
				v.setClickable(false);
				if (listener.onNoticeClick(id)) {
					v.setClickable(false);
				}
			}
		});
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
