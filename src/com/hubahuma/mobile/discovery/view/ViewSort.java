package com.hubahuma.mobile.discovery.view;

import com.hubahuma.mobile.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ViewSort extends RelativeLayout implements ViewBaseAction {

	private ListView mListView;
	private final String[] items = new String[] { "默认排序", "距离最近", "按人气排序",
			"按评分排序" };// 显示字段
	private final String[] itemsVaule = new String[] { "1", "2", "3", "4" };// 隐藏id
	private OnSelectListener mOnSelectListener;
	private TextAdapter adapter;
	private String mDistance;
	private String showText = "排序";
	private Context mContext;

	public String getShowText() {
		return showText;
	}

	public ViewSort(Context context) {
		super(context);
		init(context);
	}

	public ViewSort(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewSort(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_sort, this, true);
		// setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_mid));
		mListView = (ListView) findViewById(R.id.listView);
		adapter = new TextAdapter(context, items,
				R.color.area_item_selected_color,
				R.color.area_item_selected_color);
		adapter.setTextSize(16);
		if (mDistance != null) {
			for (int i = 0; i < itemsVaule.length; i++) {
				if (itemsVaule[i].equals(mDistance)) {
					adapter.setSelectedPositionNoNotify(i);
					// showText = items[i];
					break;
				}
			}
		}
		mListView.setAdapter(adapter);
		adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {

				if (mOnSelectListener != null) {
					showText = items[position];
					mOnSelectListener.getValue(itemsVaule[position],
							items[position]);
				}
			}
		});
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String distance, String showText);
	}

	@Override
	public void hide() {

	}

	@Override
	public void show() {

	}

}
