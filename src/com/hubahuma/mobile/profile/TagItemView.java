package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_tag)
public class TagItemView extends RelativeLayout {

	@ViewById
	TextView tag;

	public TagItemView(Context context) {
		super(context);
	}

	public void bind(String tag) {
		this.tag.setText(tag);
	}
}