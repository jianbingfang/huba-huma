package com.hubahuma.mobile.discovery;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.utils.UtilTools;

@EFragment(R.layout.diaglog_one_input)
public class VerifyChildDialog extends DialogFragment {

	public interface OnVerifyChildDialogConfirmListener {
		void onDialogConfirm(String inputText);
	}

	@ViewById
	TextView dialog_title, dialog_subtitle;

	@ViewById
	EditText input_box;

	@ViewById
	Button btn_confirm;

	@AfterViews
	void init() {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		dialog_title.setText("验证信息");
		dialog_subtitle.setText("输入孩子姓名，便于教师验证");
		input_box.setHint("某某的家长");
		btn_confirm.setText("提交");
	}

	@AfterTextChange
	void input_box(TextView text) {
		if (UtilTools.isEmpty(text.getText().toString().trim())) {
			btn_confirm.setTextColor(getResources().getColor(R.color.grey));
			btn_confirm.setEnabled(false);
		} else {
			btn_confirm
					.setTextColor(getResources().getColor(R.color.text_blue));
			btn_confirm.setEnabled(true);
		}
	}

	@Click
	void btn_cancel() {
		this.dismiss();
	}

	@Click
	void btn_confirm() {
		String name = input_box.getText().toString().trim();
		OnVerifyChildDialogConfirmListener activity = (OnVerifyChildDialogConfirmListener) getActivity();
		activity.onDialogConfirm(name);
		this.dismiss();
	}
}
