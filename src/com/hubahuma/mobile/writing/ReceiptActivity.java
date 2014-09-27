package com.hubahuma.mobile.writing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.entity.Parent;
import com.hubahuma.mobile.entity.User;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.FetchDetailParentParam;
import com.hubahuma.mobile.entity.service.FetchDetailParentResp;
import com.hubahuma.mobile.entity.service.WriteAnnouncementParam;
import com.hubahuma.mobile.entity.service.WriteAnnouncementResp;
import com.hubahuma.mobile.profile.ProfileOrganizationActivity_;
import com.hubahuma.mobile.profile.ProfileParentsActivity_;
import com.hubahuma.mobile.profile.ProfileTeacherActivity_;
import com.hubahuma.mobile.service.MyErrorHandler;
import com.hubahuma.mobile.service.UserService;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;
import com.hubahuma.mobile.writing.ReceiptListViewAdapter.ReceiptListViewListener;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_receipt)
public class ReceiptActivity extends FragmentActivity implements
		PromptDialogListener, ReceiptListViewListener {

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	TextView read_info, unread_info;

	@ViewById
	ListView read_list;

	@ViewById
	Button resend_app, resend_sms;

	@Extra
	NoticeEntity notice;

	// private List<UserEntity> readUserList;

	private List<UserEntity> userList;

	private int unreadNum = 0;

	private ReceiptListViewAdapter readAdapter;

	// private ReceiptListViewAdapter unreadAdapter;

	@RestService
	UserService userService;

	@App
	MyApplication myApp;

	@Bean
	MyErrorHandler myErrorHandler;

	private NoticeEntity newNotice = null;

	private boolean publishSucc = false;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	@AfterInject
	void afterInject() {
		userService.setRestErrorHandler(myErrorHandler);
		RestTemplate tpl = userService.getRestTemplate();
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		s.setConnectTimeout(GlobalVar.CONNECT_TIMEOUT);
		s.setReadTimeout(GlobalVar.READ_TIMEOUT);
		tpl.setRequestFactory(s);
	}

	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);

		preLoadData();
		loadData();
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

	@Background(delay = 0)
	void loadData() {
		// readUserList = getTestData();
		// userList = getTestData();

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			postLoadDataFail();
			return;
		}

		try {
			FetchDetailParentParam p = new FetchDetailParentParam();
			p.setParentId(notice.getRecipients());
			p.setToken(myApp.getToken());
			FetchDetailParentResp parentResp = userService.fetchDetailParent(p);
			if (parentResp == null || parentResp.getParentObjects() == null
					|| parentResp.getParentObjects().isEmpty()
					|| parentResp.getUserObjects() == null
					|| parentResp.getUserObjects().isEmpty()) {
				showToast("服务器数据返回异常", Toast.LENGTH_LONG);
				postLoadDataFail();
				return;
			} else {
				List<Parent> pl = parentResp.getParentObjects();
				List<User> ul = parentResp.getUserObjects();
				for (int i = 0; i < pl.size(); i++) {
					UserEntity user = new UserEntity();
					user.bind(pl.get(i), ul.get(i));
					if (notice.getRecipientsRead() != null
							&& notice.getRecipientsRead().contains(
									user.getUserId())) {
						userList.add(user);
						// readUserList.add(user);
					} else {
						userList.add(unreadNum, user);
						unreadNum++;
					}
				}
			}
		} catch (RestClientException e) {
			showToast("服务器异常，回执数据载入失败", Toast.LENGTH_LONG);
			postLoadDataFail();
			return;
		}

		postLoadDataSucc();
	}

	@UiThread
	void preLoadData() {

		unreadNum = 0;

		unread_info.setText("未读：");
		read_info.setText("已读：");
		resend_app.setVisibility(View.GONE);
		resend_sms.setVisibility(View.GONE);
		progress_bar.setVisibility(View.VISIBLE);

		// readUserList = new ArrayList<UserEntity>();
		userList = new ArrayList<UserEntity>();

		readAdapter = new ReceiptListViewAdapter(getApplicationContext(),
				userList, unreadNum, this);
		read_list.setAdapter(readAdapter);
	}

	@UiThread
	void postLoadDataFail() {
		progress_bar.setVisibility(View.GONE);
	}

	@UiThread
	void postLoadDataSucc() {

		unread_info.setText("未读：" + unreadNum);
		read_info.setText("已读：" + (userList.size() - unreadNum));

		if (unreadNum > 0) {
			resend_app.setVisibility(View.VISIBLE);
			resend_sms.setVisibility(View.VISIBLE);
		}

		readAdapter.notifyDataSetChanged();
		progress_bar.setVisibility(View.GONE);
	}

	@Click(R.id.resend_app)
	void resendNoticeByApp() {
		showLoadingDialog();
		handleResendNoticeByApp();
	}

	@Background
	void handleResendNoticeByApp() {
		notice.setDate(new Date());
		publishSucc = publishNotice(notice);
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "该通知已通过应用重新发布");
		} else {
			showPromptDialog("错误", "发布失败");
		}
	}

	boolean publishNotice(NoticeEntity notice) {
		try {
			WriteAnnouncementParam param = new WriteAnnouncementParam();
			param.setTitle(notice.getTitle());
			param.setText(notice.getText());
			param.setPhotos(notice.getPhotos());
			param.setRecepients(notice.getRecipients());
			param.setToken(myApp.getToken());
			WriteAnnouncementResp resp = userService.writeAnnouncement(param);
			if (resp == null) {
				showToast("服务器处理错误", Toast.LENGTH_LONG);
				return false;
			}
			newNotice = resp.getCreatedObject();
		} catch (RestClientException e) {
			showToast("服务器连接异常", Toast.LENGTH_LONG);
			return false;
		}

		return true;
	}

	@Click(R.id.resend_sms)
	void resendNoticeBySms() {
		handleResendSms();
	}

	@Background
	void handleResendSms() {
		showLoadingDialog();

		if (!UtilTools.isNetConnected(getApplicationContext())) {
			showToast("无法访问网络", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		// TODO 调用UserService的短信接口

		String result = null;
		try {
			// result = smsService.sendSMS(uid, key, phone, msg);
			System.out.println("SMS result:" + result);
		} catch (RestClientException e) {
			showToast("服务器连接异常", Toast.LENGTH_LONG);
			dismissLoadingDialog();
			return;
		}

		if (result == null || Integer.parseInt(result) < 0) {
			dismissLoadingDialog();
			showPromptDialog("失败", "短信发送失败，请稍后重试。");
			return;
		} else {
			dismissLoadingDialog();
			showPromptDialog("成功", "通知内容已通过短信成功发送。");
			resend_sms.setText("已重发");
			resend_sms.setEnabled(false);
			resend_sms.setBackgroundResource(R.drawable.btn_grey_bg);
			// showToast("验证短信已发送", Toast.LENGTH_SHORT);
		}

	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
	}

	@Override
	public void onPortraitClick(UserEntity user) {
		Intent intent = new Intent();
		intent.putExtra("user", user);

		switch (user.getType()) {
		case UserType.ORGANIZTION:
			intent.setClass(this, ProfileOrganizationActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_ORGANIZATION_ACTIVITY);
			break;

		case UserType.TEACHER:
			intent.setClass(this, ProfileTeacherActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_TEACHER_ACTIVITY);
			break;

		case UserType.PARENTS:
			intent.setClass(this, ProfileParentsActivity_.class);
			startActivityForResult(intent,
					ActivityCode.PROFILE_PARENTS_ACTIVITY);
			break;

		}
	}

	@UiThread
	void showToast(String info, int time) {
		Toast.makeText(getApplicationContext(), info, time).show();
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		if (publishSucc) {
			intent.putExtra("newNotice", newNotice);
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

	private List<UserEntity> getTestData() {

		Random rand = new Random();
		List<UserEntity> testData = new ArrayList<UserEntity>();
		for (int i = 1; i <= rand.nextInt(20); i++) {
			UserEntity item = new UserEntity();
			item.setUserId("0000" + i);
			item.setType(UserType.PARENTS);
			item.setName("高健" + i);
			item.setRemark("高晓东" + i + "爸爸");
			testData.add(item);
		}
		return testData;
	}
}
