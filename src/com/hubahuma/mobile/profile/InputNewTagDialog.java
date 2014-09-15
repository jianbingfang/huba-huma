package com.hubahuma.mobile.profile;

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
public class InputNewTagDialog extends DialogFragment {

	public interface InputNewTagDialogConfirmListener {
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
		dialog_title.setText("新增标签");
		dialog_subtitle.setText("请输入自定义的标签名称");
		input_box.setHint("长度限制为8个字以内");
		btn_confirm.setText("添加");
	}

	@AfterTextChange
	void input_box(TextView text) {
		if (UtilTools.isEmpty(text.getText().toString().trim())
				|| text.getText().toString().length() > 8) {
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
		InputNewTagDialogConfirmListener activity = (InputNewTagDialogConfirmListener) getActivity();
		activity.onDialogConfirm(name);
		this.dismiss();
	}
}
