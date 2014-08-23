package com.hubahuma.mobile.news.contacts;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.news.contacts.InviteUserActivity.CheckableUserEntity;

public class InviteViewAdapter extends BaseExpandableListAdapter {

	private List<String> groupData;
	private List<List<CheckableUserEntity>> childData;// 组显示

	private LayoutInflater mInflater;

	public InviteViewAdapter(Context context, List<String> groupData,
			List<List<CheckableUserEntity>> childData) {
		this.groupData = groupData;
		this.childData = childData;
		this.mInflater = LayoutInflater.from(context);
	}

	// 必须实现 得到子数据
	@Override
	public Object getChild(int groupPosition, int j) {
		return childData.get(groupPosition).get(j);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childData.get(groupPosition).size();
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

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup viewgroup) {
		ExpandableGroupHolder holder = null; // 清空临时变量holder
		if (convertView == null) { // 判断view（即view是否已构建好）是否为空

			convertView = mInflater.inflate(R.layout.group_tree_title, null);
			holder = new ExpandableGroupHolder();
			holder.title = (TextView) convertView.findViewById(R.id.group_name);
			holder.indicator = (ImageView) convertView
					.findViewById(R.id.indicator);
			convertView.setTag(holder);
		} else { // 若view不为空，直接从view的tag属性中获得各子视图的引用
			holder = (ExpandableGroupHolder) convertView.getTag();
		}
		String title = groupData.get(groupPosition);
		holder.title.setText(title + "(" + this.getChildrenCount(groupPosition) + ")");

		if (isExpanded) {
			holder.indicator
					.setBackgroundResource(R.drawable.down_seletor_triangle);
		} else {
			holder.indicator
					.setBackgroundResource(R.drawable.right_seletor_triangle);
		}
		notifyDataSetChanged();
		return convertView;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup viewgroup) {
		ExpandableListHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.invite_user_item,
					null);
			holder = new ExpandableListHolder();
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.invite_check_box);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.portrait = (ImageView) convertView
					.findViewById(R.id.head_portrait);
			holder.remark = (TextView) convertView.findViewById(R.id.remark);
			convertView.setTag(holder);
		} else {// 若行已初始化，直接从tag属性获得子视图的引用
			holder = (ExpandableListHolder) convertView.getTag();
		}
		CheckableUserEntity user = childData.get(groupPosition).get(childPosition);
		// TODO 判断真实头像
		holder.portrait.setImageResource(R.drawable.default_portrait);
		holder.checkBox.setChecked(user.isChecked());
		holder.checkBox.setTag(user);
		holder.name.setText(user.getUsername());
		if ("".equals(user.getRemark())) {
			holder.remark.setVisibility(View.GONE);
		} else {
			holder.remark.setVisibility(View.VISIBLE);
			holder.remark.setText(user.getRemark());
		}
		
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				CheckableUserEntity u = (CheckableUserEntity)buttonView.getTag();
				u.setChecked(isChecked);
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
		CheckBox checkBox;
		ImageView portrait;
		TextView name;
		TextView remark;
	}
	
}
