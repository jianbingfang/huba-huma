package com.hubahuma.mobile.news;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.format.DateUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.ChatMsgEntity;
import com.hubahuma.mobile.entity.ChatMsgEntity.MsgState;
import com.hubahuma.mobile.utils.UtilTools;
import com.sun.codemodel.util.MS1252Encoder;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_chat)
public class ChatActivity extends Activity {

	@Extra
	String name;

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
						new GetDataTask().execute();
					}
				});

		mPullRefreshListView.setMode(Mode.PULL_FROM_START);// 设置底部下拉刷新模式

		listItem = getTestData();// 获取list数据
		adapter = new ChatMsgViewAdapter(getApplicationContext(), listItem);

		// 设置适配器
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(adapter);

	}

	private class GetDataTask extends
			AsyncTask<Void, Void, List<ChatMsgEntity>> {

		// 后台处理部分
		@Override
		protected List<ChatMsgEntity> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			List<ChatMsgEntity> msgList = new ArrayList<ChatMsgEntity>();
			Random rand = new Random();
			int num = new Random().nextInt(2) + 3;
			for (int i = 0; i < num; i++) {
				ChatMsgEntity msg = new ChatMsgEntity();
				msg = new ChatMsgEntity();
				msg.setName(name);
				msg.setContent("你好，我是编号 #" + rand.nextInt(1000));
				msg.setDate(new Date());
				msg.setComMsg(rand.nextBoolean());
				if (msg.isComMsg())
					msg.setState(MsgState.ARRIVED);
				else
					msg.setState(MsgState.SENDED);
				msgList.add(msg);
			}

			return msgList;
		}

		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(List<ChatMsgEntity> resultList) {
			// 在头部增加新添内容
			try {
				if (resultList != null && !resultList.isEmpty()) {
					for (ChatMsgEntity result : resultList) {
						listItem.add(0, result);
					}
					// 通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
					adapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
				}
			} catch (Exception e) {
				setTitle(e.getMessage());
			}

			super.onPostExecute(resultList);
		}
	}

	@Click
	void btn_send() {
		
		String content = msg_input_box.getText().toString();
		System.out.println("SEND:" + msg_input_box.getText().toString());
		if(UtilTools.isEmpty(content))
			return;
		
		ChatMsgEntity msg = new ChatMsgEntity();
		msg = new ChatMsgEntity();
		msg.setName(name);
		msg.setContent(content);
		msg.setDate(new Date());
		msg.setComMsg(false);
		msg.setState(MsgState.SENDING);
		listItem.add(msg);
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
		msg_input_box.setText("");
		
		sendMsgBackground(msg);
	}

	@Background(id = "msg_send_task", delay = 2000)
	void sendMsgBackground(ChatMsgEntity msg) {
		msg.setState(MsgState.SENDED);
		updateUiThread();
	}
	
	@UiThread
	void updateUiThread() {
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
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
			msg.setName(name);
			msg.setContent("你好，我是编号 #" + new Random().nextInt(1000));
			msg.setDate(new Date());
			msg.setComMsg(rand.nextBoolean());
			if (msg.isComMsg())
				msg.setState(MsgState.ARRIVED);
			else
				msg.setState(MsgState.SENDED);
			list.add(0, msg);
		}
		return list;
	}
}
