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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;

public class SimpleManageBookViewAdapter extends BaseExpandableListAdapter {

	private List<GroupEntity> groupData;// 组显示

	private LayoutInflater mInflater;

	private SimpleManageBookListener listener;

	private final int VIEW_NUMBER = 2;

	private final int VERIFY_VIEW = 0;
	private final int CONTACT_VIEW = 1;

	public SimpleManageBookViewAdapter(Context context,
			List<GroupEntity> groupData,
			SimpleManageBookListener phoneOperListener) {

		this.groupData = groupData;
		this.mInflater = LayoutInflater.from(context);
		this.listener = phoneOperListener;
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
		return true;
	}

	@Override
	public int getChildType(int groupPosition, int childPosition) {
		if (groupPosition == 0) {
			return VERIFY_VIEW;
		} else {
			return CONTACT_VIEW;
		}
	}

	@Override
	public int getChildTypeCount() {
		return VIEW_NUMBER;
	}

	@Override
	public int getGroupTypeCount() {
		return 1;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup viewgroup) {
		GroupHolder holder = null; // 清空临时变量holder
		if (true) { // 判断view（即view是否已构建好）是否为空

			convertView = mInflater.inflate(R.layout.item_group_tree_title,
					null);
			holder = new GroupHolder();
			holder.title = (TextView) convertView.findViewById(R.id.group_name);
			holder.indicator = (ImageView) convertView
					.findViewById(R.id.indicator);
			convertView.setTag(holder);
		} else { // 若view不为空，直接从view的tag属性中获得各子视图的引用
			holder = (GroupHolder) convertView.getTag();
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

		VerifyViewHolder holder1 = null;
		ContactViewHolder holder2 = null;

		int type = getChildType(groupPosition, childPosition);

		if (convertView == null) {

			switch (type) {
			case VERIFY_VIEW:
				convertView = mInflater.inflate(R.layout.item_contacts_verify,
						null);
				holder1 = new VerifyViewHolder();
				holder1.name = (TextView) convertView.findViewById(R.id.name);
				holder1.portrait = (ImageView) convertView
						.findViewById(R.id.head_portrait);
				holder1.remark = (TextView) convertView
						.findViewById(R.id.remark);
				holder1.ignore = (Button) convertView
						.findViewById(R.id.btn_ignore);
				holder1.accept = (Button) convertView
						.findViewById(R.id.btn_accept);
				convertView.setTag(holder1);
				break;

			case CONTACT_VIEW:
				convertView = mInflater.inflate(R.layout.item_contacts_parents,
						null);
				holder2 = new ContactViewHolder();
				holder2.name = (TextView) convertView.findViewById(R.id.name);
				holder2.portrait = (ImageView) convertView
						.findViewById(R.id.head_portrait);
				holder2.remark = (TextView) convertView
						.findViewById(R.id.remark);
				holder2.sendMsg = (ImageButton) convertView
						.findViewById(R.id.btn_send_message);
				holder2.giveCall = (ImageButton) convertView
						.findViewById(R.id.btn_call);
				convertView.setTag(holder2);
				break;
			}

		} else {
			switch (type) {
			case VERIFY_VIEW:
				holder1 = (VerifyViewHolder) convertView.getTag();
				break;
			case CONTACT_VIEW:
				holder2 = (ContactViewHolder) convertView.getTag();
				break;
			}
		}

		if (type == VERIFY_VIEW) {
			final UserEntity user = groupData.get(groupPosition)
					.getMemberList().get(childPosition);
			// TODO 判断真实头像
			holder1.portrait.setImageResource(R.drawable.default_portrait);
			holder1.portrait.setTag(user);
			holder1.portrait.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UserEntity user = (UserEntity) v.getTag();
					listener.onPortraitClick(user);
				}
			});
			final String username = user.getName();
			holder1.name.setText(username);
			holder1.remark.setText(user.getRemark());
			// TODO 记录该用户ID
			holder1.ignore.setTag(user);
			holder1.accept.setTag(user);

			final Button btn_accept = holder1.accept;
			holder1.ignore.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.ignoreReceipt((Button) v, btn_accept);
					Log.d("debug", "ignore receipt from " + username);
				}
			});

			final Button btn_ignore = holder1.ignore;
			holder1.accept.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.acceptReceipt(btn_ignore, (Button) v);
					Log.d("debug", "accept receipt from " + username);
				}
			});
		} else {
			final UserEntity user = groupData.get(groupPosition)
					.getMemberList().get(childPosition);
			// TODO 判断真实头像
			holder2.portrait.setImageResource(R.drawable.default_portrait);
			holder2.portrait.setTag(user);
			holder2.portrait.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UserEntity user = (UserEntity) v.getTag();
					listener.onPortraitClick(user);
				}
			});
			final String username = user.getName();
			holder2.name.setText(username);
			holder2.remark.setText(user.getRemark());
			// TODO 记录该用户ID
			holder2.sendMsg.setTag(holder2.name);
			holder2.giveCall.setTag(holder2.name);

			holder2.sendMsg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO 写入真实号码
					listener.sendSMS(user.getPhone(), "");
					Log.d("debug", "send message to " + username);
				}
			});

			holder2.giveCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO 写入真实号码
					listener.phoneCall(user.getPhone());
					Log.d("debug", "make call to " + username);
				}
			});
		}

		return convertView;

	}

	// 父单元
	static class GroupHolder {
		ImageView indicator;
		TextView title;
	}

	// 子单元
	static class VerifyViewHolder {
		ImageView portrait;
		TextView name;
		TextView remark;
		Button ignore;
		Button accept;
	}

	// 子单元
	static class ContactViewHolder {
		ImageView portrait;
		TextView name;
		TextView remark;
		ImageButton sendMsg;
		ImageButton giveCall;
	}

	public interface SimpleManageBookListener {
		
		public void onPortraitClick(UserEntity user);
		
		public void sendSMS(String phoneNum, String smsContent);

		public void phoneCall(String phoneNum);

		public void ignoreReceipt(Button ignore, Button accept);

		public void acceptReceipt(Button ignore, Button accept);
	}

}
