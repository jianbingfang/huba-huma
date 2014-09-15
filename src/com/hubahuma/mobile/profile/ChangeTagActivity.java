package com.hubahuma.mobile.profile;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.discovery.VerifyChildDialog_;
import com.hubahuma.mobile.profile.InputNewTagDialog.InputNewTagDialogConfirmListener;
import com.hubahuma.mobile.view.FlowLayout;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_change_tag)
public class ChangeTagActivity extends FragmentActivity implements
		PromptDialogListener, InputNewTagDialogConfirmListener {

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	EditText search_input;

	@ViewById
	ImageButton btn_submit;

	@ViewById
	FlowLayout added_tag_layout, hot_tag_layout;

	@Extra
	ArrayList<String> tagList;

	ArrayList<String> hotTagList;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private NewTagButtonView newTagBtn;

	// private TagListViewAdapter addedTagAdapter;
	//
	// private TagListViewAdapter hotTagAdapter;

	private boolean publishSucc = false;

	private LayoutInflater mInflater;

	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		newTagBtn = NewTagButtonView_.build(getApplicationContext());
		newTagBtn.tag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				InputNewTagDialog_ dialog = new InputNewTagDialog_();
				dialog.show(fm, "dialog_new_tag");
			}
		});

		updateAddedListView();

		preLoadData();
		loadData();
	}

	@UiThread
	void updateAddedListView() {
		added_tag_layout.removeAllViews();
		if (tagList != null) {
			for (String tag : tagList) {
				TagItemView tagView = TagItemView_
						.build(getApplicationContext());
				tagView.bind(tag);
				tagView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TagItemView view = (TagItemView) v;
						tagList.remove(view.tag.getText().toString());
						hotTagList.add(view.tag.getText().toString());
						updateAddedListView();
						updateHotListView();
					}
				});
				added_tag_layout.addView(tagView);
			}
		}

		added_tag_layout.addView(newTagBtn);

	}

	@UiThread
	void updateHotListView() {
		hot_tag_layout.removeAllViews();
		if (hotTagList != null) {
			for (String tag : hotTagList) {
				TagItemView tagView = TagItemView_
						.build(getApplicationContext());
				tagView.bind(tag);
				tagView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TagItemView view = (TagItemView) v;
						hotTagList.remove(view.tag.getText().toString());
						tagList.add(view.tag.getText().toString());
						updateAddedListView();
						updateHotListView();
					}
				});
				hot_tag_layout.addView(tagView);
			}
		}
	}

	@Background(delay = 1000)
	void loadData() {
		hotTagList = getTestData();
		postLoadData();
	}

	private ArrayList<String> getTestData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("音乐");
		list.add("数学12");
		list.add("英语");
		list.add("语文33312");
		list.add("体育12");
		list.add("舞蹈312");
		list.add("钢琴1");
		list.add("吉他314121");

		list.removeAll(tagList);

		return list;
	}

	@UiThread
	void preLoadData() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postLoadData() {
		updateHotListView();
		progress_bar.setVisibility(View.GONE);
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

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
	}

	@Override
	public void onDialogConfirm(String inputText) {

		if (tagList.contains(inputText)) {
			showToast("该标签已存在", Toast.LENGTH_SHORT);
			return;
		} else {
			tagList.add(inputText);
			updateAddedListView();
		}

		if (hotTagList.remove(inputText)) {
			updateHotListView();
		}

	}
}
