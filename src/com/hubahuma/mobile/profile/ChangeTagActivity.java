package com.hubahuma.mobile.profile;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryViewAdapter;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@NoTitle
@EActivity(R.layout.activity_change_tag)
public class ChangeTagActivity extends FragmentActivity implements
		PromptDialogListener {

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	EditText search_input;

	@ViewById
	ImageButton btn_submit;

	@ViewById
	ListView added_tag_list_view, hot_tag_list_view;

	@Extra
	ArrayList<String> tagList;

	ArrayList<String> hotTagList;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private TagListViewAdapter addedTagAdapter;

	private TagListViewAdapter hotTagAdapter;

	private boolean publishSucc = false;

	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		addedTagAdapter = new TagListViewAdapter(getApplicationContext(),
				tagList);
		added_tag_list_view.setAdapter(addedTagAdapter);

		preLoadData();
		loadData();
	}

	@Background(delay = 1000)
	void loadData() {
		hotTagList = getTestData();
		postLoadData();
	}

	private ArrayList<String> getTestData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("音乐");
		list.add("数学");
		list.add("英语");
		list.add("语文");
		list.add("体育");
		list.add("舞蹈");

		list.removeAll(tagList);

		return list;
	}

	@UiThread
	void preLoadData() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postLoadData() {
		hotTagAdapter = new TagListViewAdapter(getApplicationContext(),
				hotTagList);
		hot_tag_list_view.setAdapter(hotTagAdapter);
		progress_bar.setVisibility(View.GONE);
	}

	@ItemClick(R.id.added_tag_list_view)
	public void onAddedTagClicked(int position) {
		String item = tagList.remove(position);
		hotTagList.add(item);
		addedTagAdapter.notifyDataSetChanged();
		hotTagAdapter.notifyDataSetChanged();
	}

	@ItemClick(R.id.hot_tag_list_view)
	public void onHotTagClicked(int position) {
		String item = hotTagList.remove(position);
		tagList.add(item);
		addedTagAdapter.notifyDataSetChanged();
		hotTagAdapter.notifyDataSetChanged();
	}

	@Click
	void btn_add_img() {

	}

	@Click
	void btn_submit() {
		showLoadingDialog();
		handleChangeTag();
	}

	@UiThread
	void showLoadingDialog() {
		loadingDialog.show(getSupportFragmentManager(), "dialog_loading");
	}

	@UiThread
	void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	@UiThread
	void showPromptDialog(String title, String content) {
		promptDialog.setTitle(title);
		promptDialog.setContent(content);
		promptDialog.show(getSupportFragmentManager(), "dialog_prompt");
	}

	@UiThread
	void dismissPromptDialog() {
		promptDialog.dismiss();
	}

	@Background(delay = 1000)
	void handleChangeTag() {
		publishSucc = changeTag();
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "修改成功！");
		} else {
			showPromptDialog("错误", "修改失败！");
		}
	}

	boolean changeTag() {
		// TODO 发送notice数据给后台
		return true;
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ChangeTagActivity");
		if (publishSucc) {
			intent.putStringArrayListExtra("newTagList", tagList);
			this.setResult(1, intent);
		} else {
			this.setResult(0, intent);
		}
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
		if (publishSucc) {
			btn_back();
		}
	}
}
