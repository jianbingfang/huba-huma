package com.hubahuma.mobile.profile;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.profile.WriteCommentActivity.CommentType;
import com.hubahuma.mobile.utils.UtilTools;
import com.hubahuma.mobile.view.CircleImageView;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_profile_parents)
public class ProfileParentsActivity extends Activity {

	@ViewById
	TextView phone_number, name, remark, address, tag;

	@ViewById
	CircleImageView portrait;

	@ViewById
	ProgressBar progress_bar;

	@Extra
	UserEntity user;

	@AfterViews
	void init() {

		if (!UtilTools.isEmpty(user.getPortrait())) {
			Bitmap bitmap = UtilTools.string2Bitmap(user.getPortrait());
			if (bitmap != null) {
				portrait.setImageBitmap(bitmap);
			}
		}

		phone_number.setText(user.getPhone());
		name.setText(user.getName());
		address.setText(user.getAddress());
		remark.setText(user.getRemark());
		List<String> tagList = user.getTags();
		if (tagList != null && !tagList.isEmpty()) {
			String str = tagList.get(0);
			for (int i = 1; i < tagList.size(); i++) {
				str += "    " + tagList.get(i);
			}
			tag.setText(str);
		}
	}

	@Click
	void btn_add_to_managebook() {

	}

	@Click
	void btn_follow() {

	}

	@Click
	void latest_article_panel() {
		Intent intent = new Intent(this, PublishedArticleActivity_.class);
		startActivityForResult(intent, ActivityCode.PUBLISHED_ARTICLE_ACTIVITY);
	}

	@Click
	void btn_phone() {
		phone_panel();
	}

	@Click
	void phone_panel() {
		if (!UtilTools.isEmpty(phone_number.getText().toString())) {
			Uri callToUri = Uri.parse("tel:"
					+ phone_number.getText().toString());
			Intent intent = new Intent(Intent.ACTION_DIAL, callToUri);
			startActivity(intent);
		}
	}

	@Click
	void location_panel() {
		progress_bar.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, LocationActivity_.class);
		intent.putExtra("name", name.getText().toString());
		intent.putExtra("address", address.getText().toString());
		intent.putExtra("phone", phone_number.getText().toString());
		startActivityForResult(intent, ActivityCode.LOCATION_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.LOCATION_ACTIVITY)
	void onReturnFromLocationActivity() {
		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ProfileTeacherActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
