package com.hubahuma.mobile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.utils.UtilTools;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

@EFragment(R.layout.fragment_prompt_dialog)
public class PromptDialog extends DialogFragment {

	public interface PromptDialogListener {
		void onDialogConfirm();
	}

	@ViewById
	TextView dialog_title, dialog_subtitle;

	@ViewById
	Button btn_confirm;

	public void setTitle(String text) {
		dialog_title.setText(text);
	}

	public void setSubtitle(String text) {
		if (!UtilTools.isEmpty(text)) {
			dialog_subtitle.setText(text);
			dialog_subtitle.setVisibility(View.VISIBLE);
		}
	}

	public void setButtonText(String text) {
		btn_confirm.setText(text);
	}

	@AfterViews
	void init() {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
	}

	@Click
	void btn_confirm() {
		this.dismiss();
	}
}
