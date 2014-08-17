package com.hubahuma.mobile.news;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.ChatMsgEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class ChatMsgViewAdapter extends BaseAdapter {

	private List<ChatMsgEntity> dataList;
	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> data) {
		this.dataList = data;
		mInflater = LayoutInflater.from(context);
	}

	// 获取ListView的项个数
	public int getCount() {
		return dataList.size();
	}

	// 获取项
	public Object getItem(int position) {
		return dataList.get(position);
	}

	// 获取项的ID
	public long getItemId(int position) {
		return position;
	}

	// 获取View
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		ChatMsgEntity entity = dataList.get(position);

		ViewHolder viewHolder = null;
		// if (convertView == null ) {
		if (entity.isComMsg() == true) {
			// 如果是对方发来的消息，则显示的是左气泡
			convertView = mInflater.inflate(R.layout.chat_msg_item_left, null);
		} else {
			// 如果是自己发出的消息，则显示的是右气泡
			convertView = mInflater.inflate(R.layout.chat_msg_item_right, null);
		}

		viewHolder = new ViewHolder();
		viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.date);
		viewHolder.portrait = (ImageButton) convertView
				.findViewById(R.id.portrait);
		viewHolder.tvContent = (TextView) convertView
				.findViewById(R.id.content);
		viewHolder.progressBar = (ProgressBar) convertView
				.findViewById(R.id.progress_bar);

		// convertView.setTag(viewHolder);
		// } else {
		// viewHolder = (ViewHolder) convertView.getTag();
		// }

		viewHolder.showDate = entity.isShowDate();
		viewHolder.tvSendTime.setText(UtilTools.ParseDate(entity.getDate()));
		if (viewHolder.showDate == true)
			viewHolder.tvSendTime.setVisibility(View.VISIBLE);
		else
			viewHolder.tvSendTime.setVisibility(View.GONE);
		// TODO 判断真实头像
		viewHolder.portrait.setBackgroundResource(R.drawable.default_portrait);
		viewHolder.tvContent.setText(entity.getContent());
		viewHolder.isComMsg = entity.isComMsg();
		if (entity.getState() == ChatMsgEntity.MsgState.SENDING)
			viewHolder.progressBar.setVisibility(View.VISIBLE);
		else
			viewHolder.progressBar.setVisibility(View.GONE);

		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public ImageButton portrait;
		public TextView tvContent;
		public ProgressBar progressBar;
		public boolean isComMsg = true;
		public boolean showDate = false;
	}

}