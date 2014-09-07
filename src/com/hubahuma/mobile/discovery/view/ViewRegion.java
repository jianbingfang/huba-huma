package com.hubahuma.mobile.discovery.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hubahuma.mobile.R;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ViewRegion extends LinearLayout implements ViewBaseAction {

	private static final String TOTAL = "全部";
	
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups = new ArrayList<String>();
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
	private TextAdapter plateListViewAdapter;
	private TextAdapter regionListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString = TOTAL;
	

	public ViewRegion(Context context) {
		super(context);
		init(context);
	}

	public ViewRegion(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				regionListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace(TOTAL, "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		// setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.choosearea_bg_left));

		for (int i = 0; i < 10; i++) {
			groups.add(i + "区");
			LinkedList<String> tItem = new LinkedList<String>();
			for (int j = 0; j < 15; j++) {
				tItem.add(i + "区" + j + "街道");
			}
			children.put(i, tItem);
		}

		regionListViewAdapter = new TextAdapter(context, groups,
				R.color.area_item_selected_color, R.color.popup_main_background);
		regionListViewAdapter.setTextSize(16);
		regionListViewAdapter.setTextGravity(Gravity.CENTER);
		regionListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(regionListViewAdapter);
		regionListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						if (position < children.size()) {
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(tEaraPosition));
		plateListViewAdapter = new TextAdapter(context, childrenItem,
				R.color.area_item_selected_color,
				R.color.area_item_selected_color);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(View view, final int position) {
						showString = childrenItem.get(position);
						if (mOnSelectListener != null) {
							mOnSelectListener.getValue(showString);
						}
					}
				});
		if (tBlockPosition < childrenItem.size())
			showString = childrenItem.get(tBlockPosition);
		if (showString.contains(TOTAL)) {
			showString = showString.replace(TOTAL, "");
		}
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
