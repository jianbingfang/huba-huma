package com.hubahuma.mobile.news.contacts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.MainActivity;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.managebook.ManageBookActivity_;
import com.hubahuma.mobile.utils.ModelUtil;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_add_contact)
public class AddContactActivity extends FragmentActivity {

	private volatile boolean searchResultShowed = false;

	@ViewById(R.id.search_input)
	EditText searchBox;

	@ViewById
	RelativeLayout search_group_btn, new_group_btn;

	@AfterViews
	void init() {

		if (ModelUtil.getCurrentUser() != null
				&& ModelUtil.getCurrentUser().getType() == UserType.PARENTS) {
			search_group_btn.setVisibility(View.GONE);
			new_group_btn.setVisibility(View.GONE);
		}

		searchBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == EditorInfo.IME_ACTION_SEARCH
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

					String text = ((EditText) v).getText().toString().trim();

					if (UtilTools.isEmpty(text) || searchResultShowed == true)
						return false;
					searchResultShowed = true;

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
		searchBox.setText("");
		searchResultShowed = false;
	}

	@Click
	void search_group_btn() {
		System.out.println("search_group_btn");
	}

	@Click
	void new_group_btn() {
		Intent intent = new Intent();
		intent.setClass(this, AddChatGroupActivity_.class);
		startActivityForResult(intent, ActivityCode.ADD_CHAT_GROUP_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.ADD_CHAT_GROUP_ACTIVITY)
	void onAddChatGroupActivityResult(int resultCode, Intent data) {
		Log.d("Return", "return from AddChatGroupActivity");
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
