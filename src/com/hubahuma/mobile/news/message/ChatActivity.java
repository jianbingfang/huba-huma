package com.hubahuma.mobile.news.message;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.ChatMsgEntity;
import com.hubahuma.mobile.entity.ChatMsgEntity.MsgState;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.managebook.ManageBookActivity_;
import com.hubahuma.mobile.news.message.ChatMsgViewAdapter.ChatMsgViewListener;
import com.hubahuma.mobile.profile.ProfileTeacherActivity_;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_chat)
public class ChatActivity extends Activity implements ChatMsgViewListener{

	@Extra
	String name;

	@Extra
	UserEntity user;
	
	private List<ChatMsgEntity> listItem = new LinkedList<ChatMsgEntity>();
	private ChatMsgViewAdapter adapter = null;

	@ViewById
	TextView topbar_title;

	@ViewById(R.id.chat_msg_list)
	PullToRefreshListView mPullRefreshListView;

	@ViewById
	EditText msg_input_box;

	@AfterViews
	void init() {

		topbar_title.setText(name);

		// 设定下拉监听函数
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// Do work to refresh the list here.
						loadDataInBackground();
					}
				});

		mPullRefreshListView.setMode(Mode.PULL_FROM_START);// 设置底部下拉刷新模式

		listItem = getTestData();// 获取list数据
		adapter = new ChatMsgViewAdapter(getApplicationContext(), listItem, this);

		// 设置适配器
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(adapter);
		
		scollToBottom();

	}

	// 后台处理部分
	@Background(delay = 1000)
	void loadDataInBackground() {
		// Simulates a background job.

		Random rand = new Random();
		int num = new Random().nextInt(2) + 2;
		for (int i = 0; i < num; i++) {
			ChatMsgEntity msg = new ChatMsgEntity();
			msg = new ChatMsgEntity();
			msg.setUser(user);
			msg.setContent("你好，我是编号 #" + rand.nextInt(1000));
			msg.setDate(new Date());
			msg.setComMsg(rand.nextBoolean());

			if (msg.isComMsg())
				msg.setState(MsgState.ARRIVED);
			else
				msg.setState(MsgState.SENDED);

			if (i != num - 1)
				msg.setShowDate(false);
			else
				msg.setShowDate(true);

			listItem.add(0, msg);
		}

		updateUiThread();
	}

	@Click
	void btn_send() {

		String content = msg_input_box.getText().toString();
		if (UtilTools.isEmpty(content))
			return;

		ChatMsgEntity msg = new ChatMsgEntity();
		msg = new ChatMsgEntity();
		msg.setUser(user);
		msg.setContent(content);
		msg.setDate(new Date());
		msg.setComMsg(false);
		msg.setState(MsgState.SENDING);
		listItem.add(msg);
		scollToBottom();
		msg_input_box.setText("");

		sendMsgInBackground(msg);
	}

	@Background(id = "msg_send_task", delay = 1000)
	void sendMsgInBackground(ChatMsgEntity msg) {
		msg.setState(MsgState.SENDED);
		updateUiThread();
	}

	@UiThread(delay = 0)
	void updateUiThread() {
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
	}

	private void scollToBottom(){
		mPullRefreshListView.getRefreshableView().setSelection(
				adapter.getCount() - 1);
	}
	
	@AfterTextChange(R.id.msg_input_box)
	void afterTextChangedOnHelloTextView(Editable text, TextView tv) {
		String content = text.toString();
		tv.setClickable(!UtilTools.isEmpty(content));
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ChatActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	private List<ChatMsgEntity> getTestData() {
		List<ChatMsgEntity> list = new LinkedList<ChatMsgEntity>();
		ChatMsgEntity msg = new ChatMsgEntity();
		Random rand = new Random();
		for (int i = 0; i < 5; i++) {
			msg = new ChatMsgEntity();

			msg.setUser(user);
			msg.setContent("你好，我是编号 #" + new Random().nextInt(1000));
			msg.setDate(new Date());
			msg.setComMsg(rand.nextBoolean());
			if (msg.isComMsg())
				msg.setState(MsgState.ARRIVED);
			else
				msg.setState(MsgState.SENDED);
			msg.setShowDate(rand.nextBoolean());

			list.add(msg);
		}
		return list;
	}

	@Override
	public void onPortraitClick(UserEntity user) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("user", user);
		intent.setClass(this, ProfileTeacherActivity_.class);
		startActivityForResult(intent, ActivityCode.PROFILE_TEACHER_ACTIVITY);
	}
}
