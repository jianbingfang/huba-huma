package com.hubahuma.mobile.news;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.id;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.news.message.MessageActivity_;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

@EActivity(R.layout.activity_news)
public class NewsActivity extends Activity {

	public interface ActivityCode {
		public static final int MESSAGE_ACTIVITY = 0;
		public static final int MANAGE_BOOK_ACTIVITY = 1;
		public static final int TEACHING_DIARY_ACTIVITY = 2;
		public static final int CONTACTS_ACTIVITY = 3;
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
		intent.setClass(this, MessageActivity_.class);
		startActivityForResult(intent, ActivityCode.MANAGE_BOOK_ACTIVITY);
	}

	@Click
	void btn_teaching_diary() {
		// TODO
	}

	@Click
	void btn_contacts() {
		// TODO
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case ActivityCode.MESSAGE_ACTIVITY:
			String result = data.getStringExtra("result");
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
					.show();
			break;

		case ActivityCode.MANAGE_BOOK_ACTIVITY:

			break;

		case ActivityCode.TEACHING_DIARY_ACTIVITY:

			break;

		case ActivityCode.CONTACTS_ACTIVITY:

			break;

		default:

		}
	}

}
