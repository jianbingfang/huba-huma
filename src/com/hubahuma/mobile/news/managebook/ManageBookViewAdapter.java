package com.hubahuma.mobile.news.managebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;

public class ManageBookViewAdapter extends BaseExpandableListAdapter {

	private List<GroupEntity> groupData;// 组显示

	private LayoutInflater mInflater;

	private PhoneOperationListener phoneOperListener;

	public ManageBookViewAdapter(Context context, List<GroupEntity> groupData,
			PhoneOperationListener phoneOperListener) {

		this.groupData = groupData;
		this.mInflater = LayoutInflater.from(context);
		this.phoneOperListener = phoneOperListener;
	}

	// 必须实现 得到子数据
	@Override
	public Object getChild(int groupPosition, int j) {
		return groupData.get(groupPosition).getMemberList().get(j);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groupData.get(groupPosition).getPopulation();
	}

	@Override
	public Object getGroup(int i) {
		return groupData.get(i);
	}

	@Override
	public int getGroupCount() {
		return groupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {// 行是否具有唯一id
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {// 行是否可选
		return false;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup viewgroup) {
		ExpandableGroupHolder holder = null; // 清空临时变量holder
		if (true) { // 判断view（即view是否已构建好）是否为空

			convertView = mInflater.inflate(R.layout.group_tree_title, null);
			holder = new ExpandableGroupHolder();
			holder.title = (TextView) convertView.findViewById(R.id.group_name);
			holder.indicator = (ImageView) convertView
					.findViewById(R.id.indicator);
			convertView.setTag(holder);
		} else { // 若view不为空，直接从view的tag属性中获得各子视图的引用
			holder = (ExpandableGroupHolder) convertView.getTag();
		}
		String title = (String) groupData.get(groupPosition).getGroupName();
		holder.title.setText(title);

		if (isExpanded) {
			holder.indicator
					.setBackgroundResource(R.drawable.down_seletor_triangle);
		} else {
			holder.indicator
					.setBackgroundResource(R.drawable.right_seletor_triangle);
		}
		// notifyDataSetChanged();
		return convertView;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup viewgroup) {
		ExpandableListHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contacts_item_parents,
					null);
			holder = new ExpandableListHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.portrait = (ImageView) convertView
					.findViewById(R.id.head_portrait);
			holder.remark = (TextView) convertView.findViewById(R.id.remark);
			holder.sendMsg = (ImageButton) convertView
					.findViewById(R.id.btn_send_message);
			holder.giveCall = (ImageButton) convertView
					.findViewById(R.id.btn_call);
			convertView.setTag(holder);
		} else {// 若行已初始化，直接从tag属性获得子视图的引用
			holder = (ExpandableListHolder) convertView.getTag();
		}
		UserEntity user = groupData.get(groupPosition).getMemberList()
				.get(childPosition);
		// TODO 判断真实头像
		holder.portrait.setImageResource(R.drawable.default_portrait);
		final String username = user.getUsername();
		holder.name.setText(username);
		holder.remark.setText(user.getRemark());
		// TODO 记录该用户ID
		holder.sendMsg.setTag(holder.name);
		holder.giveCall.setTag(holder.name);

		holder.sendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 写入真实号码
				phoneOperListener.sendSMS("10086", "hello");
				Log.d("debug", "send message to " + username);
			}
		});

		holder.giveCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 写入真实号码
				phoneOperListener.phoneCall("10086");
				Log.d("debug", "make call to " + username);
			}
		});

		return convertView;
	}

	// 父单元
	static class ExpandableGroupHolder {
		ImageView indicator;
		TextView title;
	}

	// 子单元
	static class ExpandableListHolder {
		ImageView portrait;
		TextView name;
		TextView remark;
		ImageButton sendMsg;
		ImageButton giveCall;
	}

	public interface PhoneOperationListener {
		public void sendSMS(String phoneNum, String smsContent);

		public void phoneCall(String phoneNum);
	}

}
