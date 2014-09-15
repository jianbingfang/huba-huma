package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.new_tag_button)
public class NewTagButtonView extends RelativeLayout {

	@ViewById
	Button tag;

	public NewTagButtonView(Context context) {
		super(context);
	}

	public void bind(String tag) {
		this.tag.setText(tag);
	}
}