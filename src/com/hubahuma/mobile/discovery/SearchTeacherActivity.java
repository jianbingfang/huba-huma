package com.hubahuma.mobile.discovery;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.news.contacts.AddContactActivity;
import com.hubahuma.mobile.news.contacts.SearchResultActivity_;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_search_teacher)
public class SearchTeacherActivity extends Activity {

	@ViewById
	EditText search_input;

	@ViewById
	ImageButton btn_submit, btn_clear;

	@ViewById
	TextView error_info;

	@AfterViews
	void init() {
		error_info.setText("");
	}

	@Click
	void btn_submit() {
		if (checkInput()) {
			Intent intent = new Intent();
			intent.setClass(this, SearchResultActivity_.class);
			intent.putExtra("search_word", search_input.getText().toString().trim());
			startActivityForResult(intent, ActivityCode.SEARCH_RESULT_ACTIVITY);
		}
	}

	@Click
	void btn_clear() {
		search_input.setText("");
	}

	@AfterTextChange(R.id.search_input)
	void onInputChange(Editable text) {
		if (UtilTools.isEmpty(text.toString())) {
			btn_clear.setVisibility(View.GONE);
		} else {
			btn_clear.setVisibility(View.VISIBLE);
		}
	}
	
	private boolean checkInput() {
		if (!UtilTools.isMobileNumber(search_input.getText().toString().trim())) {
			error_info.setText("手机号码格式不正确");
			return false;
		}
		error_info.setText("");
		return true;
	}
}
