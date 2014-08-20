package com.hubahuma.mobile.news.managebook;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.utils.UtilTools;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

@SuppressWarnings("deprecation")
@EFragment(R.layout.diaglog_change_group)
public class ChangeGroupDialog extends DialogFragment {

	public interface EditNameDialogListener {
		void onFinishEditDialog(String inputText);
	}

	@ViewById
	EditText group_name_input_box;

	@ViewById
	Button btn_save;

	@AfterViews
	void init() {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		// input_box.requestFocus();
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// return null;
	// }

	@Click
	void btn_cancel() {
		this.dismiss();
	}

	@Click
	void btn_save() {
		
		String name = group_name_input_box.getText().toString().trim();
		System.out.println("name:"+name);
		if (UtilTools.isEmpty(name)) {
			return;
		}
		
		EditNameDialogListener activity = (EditNameDialogListener) getActivity();
		activity.onFinishEditDialog(name);
		this.dismiss();
	}
}
