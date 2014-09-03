package com.hubahuma.mobile.discovery;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.news.contacts.AddContactActivity;
import com.hubahuma.mobile.news.contacts.SearchResultActivity_;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_discovery)
public class DiscoveryActivity extends Activity {

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

					if (UtilTools.isEmpty(text) || searchResultShowed == true)
						return false;
					searchResultShowed = true;

					return true;
				}
				return false;
			}
		});
	}

	@Click
	void btn_train() {
		Intent intent = new Intent(this, TrainActivity_.class);
		startActivity(intent);
	}

	@Click
	void btn_english() {

	}

	@Click
	void btn_interest() {

	}

	@Click
	void btn_org() {

	}

	@Click
	void btn_parents_group() {

	}

	@Click
	void btn_happy_weekend() {

	}

	@Click
	void btn_rank_teacher() {

	}

	@Click
	void btn_rank_org() {

	}

	@Click
	void btn_rank_train() {

	}

	@Click
	void btn_rank_group() {

	}

}
