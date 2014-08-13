package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.hubahuma.mobile.news.MessageActivity;
import com.hubahuma.mobile.news.MessageActivity_;

@EActivity(R.layout.activity_news)
public class NewsActivity extends Activity {

	@ViewById(R.id.btn_message)
	ImageButton messageBtn;

	@ViewById(R.id.btn_teaching_diary)
	ImageButton diaryBtn;

	@ViewById(R.id.btn_contacts)
	ImageButton contactsBtn;

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
		startActivityForResult(intent, 0);
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
		case 0:
			// result from MessageActivity
			String result = data.getStringExtra("result");
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
					.show();
			break;

		default:

		}
	}

}
