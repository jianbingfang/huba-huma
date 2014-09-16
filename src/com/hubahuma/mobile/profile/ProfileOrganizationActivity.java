package com.hubahuma.mobile.profile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.profile.WriteCommentActivity.CommentType;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_profile_organization)
public class ProfileOrganizationActivity extends Activity {

	@ViewById
	TextView phone_number, name, address;

	@ViewById(R.id.environment_img_list)
	LinearLayout imgListView;

	@ViewById
	ProgressBar progress_bar;

	private LayoutInflater mInflater;

	@AfterViews
	void init() {
		mInflater = LayoutInflater.from(this);

		// render the img list
		for (int i = 0; i < 4; i++) {
			View view = mInflater.inflate(R.layout.item_profile_org_img,
					imgListView, false);
			ImageView img = (ImageView) view.findViewById(R.id.environment_img);
			// TODO 获取真实头像
			img.setImageResource(R.drawable.teaching_diary_img);
			imgListView.addView(view);
		}
	}

	@Click
	void btn_comment() {
		Intent intent = new Intent(this, WriteCommentActivity_.class);
		intent.putExtra("type", CommentType.ORGANIZATION_COMMENT);
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
		// TODO 加入真实号码
		Uri callToUri = Uri.parse("tel:" + phone_number.getText().toString());
		Intent intent = new Intent(Intent.ACTION_DIAL, callToUri);
		startActivity(intent);
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ProfileOrganiztionActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Click
	void environment() {
		Intent intent = new Intent(this, SchoolImgManageActivity_.class);
		startActivityForResult(intent, ActivityCode.SCHOOL_IMG_MANAGE_ACTIVITY);
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

}
