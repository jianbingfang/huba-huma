package com.hubahuma.mobile.discovery;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.UserEntity;

public class TrainOrgListViewAdapter extends BaseAdapter {

	public interface TrainOrgListViewListener {
		public void onPortraitClick(UserEntity user);
	}

	private List<UserEntity> dataList;

	private LayoutInflater mInflater;

	private TrainOrgListViewListener listener;

	public TrainOrgListViewAdapter(Context context, List<UserEntity> orgData,
			TrainOrgListViewListener listener) {

		this.dataList = orgData;
		this.mInflater = LayoutInflater.from(context);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		UserEntity entity = dataList.get(position);

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_train_org_list, null);
			holder = new ViewHolder();
			holder.portrait = (ImageView) convertView
					.findViewById(R.id.portrait);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.remark = (TextView) convertView.findViewById(R.id.remark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// TODO 判断真实头像
		holder.portrait.setImageResource(R.drawable.default_portrait);
		holder.portrait.setTag(entity);
		holder.portrait.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onPortraitClick((UserEntity)v.getTag());
			}
		});
		holder.name.setText(entity.getName());
		Random rand = new Random();
		// TODO 载入真实数据
		String remark = "教师：" + rand.nextInt(50) + "名"
				+ "\t\t人气：" + rand.nextInt(3000)
				+ "\t\t综合评分：" + rand.nextInt(100);
		holder.remark.setText(remark);

		return convertView;
	}

	// 子单元
	static class ViewHolder {
		ImageView portrait;
		TextView name;
		TextView remark;
	}

}
