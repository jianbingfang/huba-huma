package com.hubahuma.mobile.news.teachingdiary;

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
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.DiaryEntity;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.utils.UtilTools;

public class TeachingDiaryViewAdapter extends BaseAdapter {

	public interface TeachingDiaryViewListener {
		public void onAuthorClick(UserEntity user);
	}

	private List<DiaryEntity> dataList;

	private LayoutInflater mInflater;

	private TeachingDiaryViewListener listener;

	public TeachingDiaryViewAdapter(Context context, List<DiaryEntity> data,
			TeachingDiaryViewListener listener) {
		this.mInflater = LayoutInflater.from(context);
		this.dataList = data;
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

		DiaryEntity item = dataList.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_teaching_diary, null);
			viewHolder = new ViewHolder();
			viewHolder.portrait = (ImageView) convertView
					.findViewById(R.id.portrait);
			viewHolder.authorName = (TextView) convertView
					.findViewById(R.id.author_name);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.remark);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			viewHolder.isFollowed = (ImageView) convertView
					.findViewById(R.id.btn_follow);
			viewHolder.diaryImg = (ImageView) convertView
					.findViewById(R.id.diary_img);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.collectBtn = (ImageButton) convertView
					.findViewById(R.id.icon_collect);
			viewHolder.popularity = (TextView) convertView
					.findViewById(R.id.popularity);
			viewHolder.commentBtn = (ImageButton) convertView
					.findViewById(R.id.icon_comment);
			viewHolder.commentNum = (TextView) convertView
					.findViewById(R.id.comment_num);
			viewHolder.shareBtn = (ImageButton) convertView
					.findViewById(R.id.icon_share);
			viewHolder.likeCheckBox = (CheckBox) convertView
					.findViewById(R.id.icon_like);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.portrait.setBackgroundResource(R.drawable.default_portrait);
		viewHolder.portrait.setTag(item.getAuthor());
		viewHolder.portrait.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onAuthorClick((UserEntity) v.getTag());
			}
		});
		viewHolder.authorName.setText(item.getAuthor().getUsername());
		viewHolder.authorName.setTag(item.getAuthor());
		viewHolder.authorName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onAuthorClick((UserEntity) v.getTag());
			}
		});
		viewHolder.remark.setText(item.getAuthor().getRemark());
		viewHolder.date.setText(UtilTools.ParseDate(item.getDate()));
		// TODO 判断真实图片
		viewHolder.diaryImg
				.setBackgroundResource(R.drawable.teaching_diary_img);
		viewHolder.content.setText(item.getContent());
		viewHolder.popularity.setText(String.valueOf(item.getCollectNum()));
		viewHolder.commentNum.setText(String.valueOf(item.getCommentNum()));
		viewHolder.likeCheckBox.setChecked(item.isLiked());

		// TODO 待定事项
		// viewHolder.isFollowed;
		// viewHolder.shareBtn;
		// viewHolder.commentBtn;
		// viewHolder.collectBtn;

		return convertView;
	}

	static class ViewHolder {
		public ImageView portrait;
		public TextView authorName;
		public TextView remark;
		public TextView date;
		public ImageView isFollowed;
		public ImageView diaryImg;
		public TextView content;
		public ImageButton collectBtn;
		public TextView popularity;
		public ImageButton commentBtn;
		public TextView commentNum;
		public ImageButton shareBtn;
		public CheckBox likeCheckBox;
	}

}
