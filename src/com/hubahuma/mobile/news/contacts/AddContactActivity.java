package com.hubahuma.mobile.news.contacts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.news.NewsActivity.ActivityCode;
import com.hubahuma.mobile.news.managebook.ManageBookActivity_;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_add_contact)
public class AddContactActivity extends FragmentActivity {

	private volatile boolean searchResultShowed = false;
	
	@ViewById(R.id.search_input)
	EditText searchBox;

	@AfterViews
	void init() {
		searchBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == EditorInfo.IME_ACTION_SEARCH
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

					String text = ((EditText) v).getText().toString().trim();

					if("".equals(text) || searchResultShowed == true)
						return false;
					searchResultShowed = true;
					
					System.out.println("SEARCH:"+text);
					
//					Toast.makeText(getApplicationContext(), "SEARCH" + text,
//							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent();
					intent.setClass(AddContactActivity.this,
							SearchResultActivity_.class);
					startActivityForResult(intent,
							ActivityCode.SEARCH_RESULT_ACTIVITY);

					return true;
				}
				return false;
			}
		});
	}

	@OnActivityResult(ActivityCode.SEARCH_RESULT_ACTIVITY)
	void onSearchResultActivityResult(int resultCode, Intent data) {
		searchResultShowed = false;
	}

	@Click
	void search_group_btn() {
		System.out.println("search_group_btn");
	}

	@Click
	void new_group_btn() {
		System.out.println("new_group_btn");
	}

	@Click
	void btn_back() {

		// String str = searchBox.getText().toString().trim();
		// if (!UtilTools.isEmpty(str)) {
		// searchBox.getText().clear();
		// return;
		// }

		Intent intent = getIntent();
		intent.putExtra("result", "returned from ContactsActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
