package com.hubahuma.mobile.news;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.news.contacts.ContactsActivity_;
import com.hubahuma.mobile.news.managebook.ManageBookActivity_;
import com.hubahuma.mobile.news.message.MessageActivity_;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryActivity_;
import com.hubahuma.mobile.utils.GlobalVar;

@EActivity(R.layout.activity_news)
public class NewsActivity extends Activity {

	@ViewById(R.id.message_redspot)
	ImageView msgRedspot;

	@ViewById
	LinearLayout layout_teaching_diary, layout_manage_book;

	@App
	MyApplication myApp;
	
	@AfterViews
	void init() {
		msgRedspot.setVisibility(View.VISIBLE);

		System.out.println("News of "+ myApp.getCurrentUser().getType());
		if (myApp.getCurrentUser() != null) {
			switch (myApp.getCurrentUser().getType()) {

			case UserType.PARENTS:
				layout_manage_book.setVisibility(View.VISIBLE);
				break;
			case UserType.ORGANIZTION:
			case UserType.TEACHER:
				layout_teaching_diary.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}

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
		Intent intent = new Intent();
		intent.setClass(this, TeachingDiaryActivity_.class);
		intent.putExtra("isSelf", false);
		startActivityForResult(intent, ActivityCode.TEACHING_DIARY_ACTIVITY);
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
		// Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
		// .show();
	}

	@OnActivityResult(ActivityCode.MANAGE_BOOK_ACTIVITY)
	void onManageBookActivityResult(int resultCode, Intent data) {
		String result = data.getStringExtra("result");
		// Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
		// .show();
	}

	@OnActivityResult(ActivityCode.TEACHING_DIARY_ACTIVITY)
	void onTeachingDiaryActivityResult(int resultCode, Intent data) {
		String result = data.getStringExtra("result");
		// Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
		// .show();
	}

	@OnActivityResult(ActivityCode.CONTACTS_ACTIVITY)
	void onContactsActivityResult(int resultCode, Intent data) {
		String result = data.getStringExtra("result");
		// Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
		// .show();
	}

}
