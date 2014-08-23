package com.hubahuma.mobile.news;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.news.contacts.ContactsActivity_;
import com.hubahuma.mobile.news.managebook.ManageBookActivity_;
import com.hubahuma.mobile.news.message.MessageActivity_;

@EActivity(R.layout.activity_news)
public class NewsActivity extends Activity {

	public interface ActivityCode {
		public static final int MESSAGE_ACTIVITY = 0;
		public static final int MANAGE_BOOK_ACTIVITY = 1;
		public static final int TEACHING_DIARY_ACTIVITY = 2;
		public static final int CONTACTS_ACTIVITY = 3;
		public static final int GROUP_MANAGE_ACTIVITY = 4;
		public static final int ADD_CONTACT_ACTIVITY = 5;
		public static final int SEARCH_RESULT_ACTIVITY = 6;
		public static final int ADD_CHAT_GROUP_ACTIVITY = 7;
		public static final int INVITE_USER_ACTIVITY = 8;
		public static final int CHAT_ACTIVITY = 9;
	}

	@ViewById(R.id.message_redspot)
	ImageView msgRedspot;

	@AfterViews
	void init() {
		msgRedspot.setVisibility(View.VISIBLE);
	}

	@Click
	void btn_message() {
		Intent intent = new Intent();
		intent.setClass(this, MessageActivity_.class);
		startActivityForResult(intent, ActivityCode.MESSAGE_ACTIVITY);
	}

	@Click
	void btn_manage_book() {
		Intent intent = new Intent();
		intent.setClass(this, ManageBookActivity_.class);
		startActivityForResult(intent, ActivityCode.MANAGE_BOOK_ACTIVITY);
	}

	@Click
	void btn_teaching_diary() {
		// TODO
	}

	@Click
	void btn_contacts() {
		Intent intent = new Intent();
		intent.setClass(this, ContactsActivity_.class);
		startActivityForResult(intent, ActivityCode.CONTACTS_ACTIVITY);
	
	}

	@OnActivityResult(ActivityCode.MESSAGE_ACTIVITY)
	void onMessageActivityResult(int resultCode, Intent data) {
		String result = data.getStringExtra("result");
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
				.show();
	}

	@OnActivityResult(ActivityCode.MANAGE_BOOK_ACTIVITY)
	void onManageBookActivityResult(int resultCode, Intent data) {

	}

	@OnActivityResult(ActivityCode.TEACHING_DIARY_ACTIVITY)
	void onTeachingDiaryActivityResult(int resultCode, Intent data) {

	}

	@OnActivityResult(ActivityCode.CONTACTS_ACTIVITY)
	void onContactsActivityResult(int resultCode, Intent data) {

	}

}
