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

@EFragment(R.layout.fragment_dialog_prompt)
public class PromptDialog extends DialogFragment {

	public interface PromptDialogListener {
		void onDialogConfirm();
	}

	@ViewById
	TextView title, content;

	@ViewById
	Button btn_confirm;

	PromptDialogListener listener;

	@UiThread
	void setTitle(String text) {
		title.setText(text);
	}

	@UiThread
	void setContent(String text) {
		if (!UtilTools.isEmpty(text)) {
			content.setText(text);
			content.setVisibility(View.VISIBLE);
		}
	}

	@UiThread
	void setButtonText(String text) {
		btn_confirm.setText(text);
	}

	public void setDialogListener(PromptDialogListener listener) {
		this.listener = listener;
	}

	@AfterViews
	void init() {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		title.setText("提示");
		content.setText("");
	}

	@Click
	void btn_confirm() {
		listener.onDialogConfirm();
	}
}
