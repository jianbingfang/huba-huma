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
@EActivity(R.layout.activity_profile_teacher)
public class ProfileTeacherActivity extends Activity {

	@ViewById
	TextView phone_number, name, address, tag, remark, introduce,
			qualification;

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

		name.setText(user.getName());
		remark.setText(user.getRemark());
		phone_number.setText(user.getPhone());
		introduce.setText(user.getIntroduce());
		address.setText(user.getAddress());
		if (user.isVerified()) {
			qualification.setVisibility(View.VISIBLE);
		} else {
			qualification.setVisibility(View.GONE);
		}

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
	void btn_comment() {
		Intent intent = new Intent(this, WriteCommentActivity_.class);
		intent.putExtra("type", CommentType.TEACHER_COMMENT);
		startActivityForResult(intent, ActivityCode.WRITE_COMMENT_ACTIVITY);
	}

	@Click
	void comment_panel() {
		Intent intent = new Intent(this, CommentsActivity_.class);
		startActivityForResult(intent, ActivityCode.COMMENTS_ACTIVITY);
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
	void btn_back() {
		Intent intent = getIntent();
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
