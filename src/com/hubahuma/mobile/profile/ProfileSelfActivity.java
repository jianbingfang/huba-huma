package com.hubahuma.mobile.profile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import com.hubahuma.mobile.ActivityCode;
import com.hubahuma.mobile.LoginActivity_;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.SelectImgPopupWindow;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.profile.ChangeInfoActivity.InfoType;
import com.hubahuma.mobile.profile.WriteCommentActivity.CommentType;
import com.hubahuma.mobile.service.SharedPrefs_;
import com.hubahuma.mobile.utils.GlobalVar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_profile_self)
public class ProfileSelfActivity extends Activity {

	@App
	MyApplication myApp;

	@Pref
	SharedPrefs_ prefs;

	@ViewById
	ImageButton btn_setup, setup;

	@ViewById
	ImageView portrait, custom_bg;

	@ViewById
	TextView remark, qualification, phone, name_title, name, name2, address,
			email, tag, introduction_title, introduction, open_time;

	@ViewById
	CheckBox notify_switch, search_switch;

	@ViewById
	RelativeLayout introduction_layout, open_time_layout, address_layout,
			img_manage_layout;

	@ViewById
	ProgressBar progress_bar;

	private ArrayList<String> tagList;

	private SelectImgPopupWindow menuWindow;

	private ImageView targetImgView = null;

	private Bitmap photo;

	@AfterViews
	void init() {

		switch (myApp.getCurrentUser().getType()) {
		case UserType.PARENTS:
			qualification.setVisibility(View.GONE);
			introduction_layout.setVisibility(View.GONE);
			open_time_layout.setVisibility(View.GONE);
			address_layout.setVisibility(View.GONE);
			img_manage_layout.setVisibility(View.GONE);

			name_title.setText("姓名");

			break;
		case UserType.TEACHER:
			open_time_layout.setVisibility(View.GONE);
			address_layout.setVisibility(View.GONE);
			img_manage_layout.setVisibility(View.GONE);

			name_title.setText("姓名");
			introduction_title.setText("教师介绍");
			break;

		case UserType.ORGANIZTION:
			remark.setVisibility(View.GONE);
			break;
		case UserType.ADMIN:
			break;
		}

		clearFields();

		preLoadData();
		loadData();
	}

	void clearFields() {
		remark.setText("");
		qualification.setVisibility(View.GONE);
		phone.setText("");
		name.setText("");
		name2.setText("");
		address.setText("");
		// id.setText("");
		email.setText("");
		tag.setText("");
		introduction.setText("");
		open_time.setText("");
	}

	@Background(delay = 1000)
	void loadData() {
		tagList = getTestData();
		postLoadData();
	}

	private ArrayList<String> getTestData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("英语");
		list.add("早教");
		list.add("少儿英语");
		return list;
	}

	@UiThread
	void preLoadData() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void postLoadData() {
		name.setText(myApp.getCurrentUser().getName());
		name2.setText(myApp.getCurrentUser().getName());
		remark.setText(myApp.getCurrentUser().getRemark());
		phone.setText(myApp.getCurrentUser().getPhone());
		// id.setText(myApp.getCurrentUser().getId());

		tag.setText(transTagList(tagList));

		notify_switch.setChecked(true);
		search_switch.setChecked(true);

		progress_bar.setVisibility(View.GONE);
	}

	@CheckedChange
	void notify_switch(CompoundButton button, boolean isChecked) {

	}

	@CheckedChange
	void search_switch(CompoundButton button, boolean isChecked) {

	}

	@Click(R.id.portrait)
	void onPortraitClick() {
		Toast.makeText(getApplicationContext(), "长按此处可修改头像", Toast.LENGTH_SHORT)
				.show();
	}

	@Click(R.id.custom_bg)
	void onCustomBgClick() {
		// Toast.makeText(getApplicationContext(), "长按此处可修改背景",
		// Toast.LENGTH_SHORT)
		// .show();
	}

	@LongClick(R.id.portrait)
	void onPortraitLongClick() {
		menuWindow = new SelectImgPopupWindow(this, itemsOnClick);
		menuWindow.showAtLocation(this.findViewById(R.id.profile_self),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		targetImgView = portrait;

	}

	@LongClick(R.id.custom_bg)
	void onCustomBgLongClick() {

		// 第一版不提供该功能
		if (true)
			return;

		menuWindow = new SelectImgPopupWindow(this, itemsOnClick);
		menuWindow.showAtLocation(this.findViewById(R.id.profile_self),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		targetImgView = custom_bg;
	}

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	// 创建一个以当前时间为名称的文件
	File tempFile = new File(Environment.getExternalStorageDirectory(),
			getPhotoFileName());

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			Intent intent = null;
			switch (v.getId()) {
			case R.id.btn_take_photo:
				// 调用系统的拍照功能
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定调用相机拍照后照片的储存路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
				break;
			case R.id.btn_pick_photo:
				intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
				break;
			default:
				break;
			}
		}
	};

	@OnActivityResult(PHOTO_REQUEST_TAKEPHOTO)
	void onReturnFromTakePhoto(Intent data) {
		startPhotoZoom(Uri.fromFile(tempFile));
	}

	@OnActivityResult(PHOTO_REQUEST_GALLERY)
	void onReturnFromGallery(Intent data) {
		if (data != null)
			startPhotoZoom(data.getData());
	}

	@OnActivityResult(PHOTO_REQUEST_CUT)
	void onReturnFromCuttedImage(Intent data) {
		if (data != null)
			setPicToView(data);
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		int dw = getWindowManager().getDefaultDisplay().getWidth();
		if (targetImgView.getId() == custom_bg.getId()) {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 4);
			intent.putExtra("aspectY", 3);
			// outputX,outputY 是剪裁图片的宽高
			intent.putExtra("outputX", 400);
			intent.putExtra("outputY", 300);

		} else {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX,outputY 是剪裁图片的宽高
			intent.putExtra("outputX", 80);
			intent.putExtra("outputY", 80);
			intent.putExtra("circleCrop", "true");
		}
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		// intent.putExtra("outputFormat",
		// Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	// 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			photo = bundle.getParcelable("data");
			targetImgView.setImageBitmap(photo);// (drawable);
		}
	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@OnActivityResult(ActivityCode.LOCATION_ACTIVITY)
	void onReturnFromLocationActivity() {
		progress_bar.setVisibility(View.GONE);
	}

	@Click
	void name_layout() {
		startChangeInfoActivity(InfoType.NAME, name.getText().toString());
	}

	@Click
	void email_layout() {
		startChangeInfoActivity(InfoType.EMAIL, email.getText().toString());
	}

	@Click
	void introduction_layout() {
		startChangeInfoActivity(InfoType.INTRODUCTION, introduction.getText()
				.toString());
	}

	@Click
	void open_time_layout() {
		startChangeInfoActivity(InfoType.OPENTIME, open_time.getText()
				.toString());
	}

	@Click
	void address_layout() {
		startChangeInfoActivity(InfoType.ADDRESS, address.getText().toString());
	}

	@Click
	void tag_layout() {
		Intent intent = new Intent(this, ChangeTagActivity_.class);
		intent.putStringArrayListExtra("tagList", tagList);
		startActivityForResult(intent, ActivityCode.CHANGE_TAG_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.CHANGE_TAG_ACTIVITY)
	void onReturnFromChangeTagActivit(int resultCode, Intent intent) {
		if (resultCode > 0) {
			ArrayList<String> list = intent
					.getStringArrayListExtra("newTagList");
			if (list != null) {
				tag.setText(transTagList(list));
				tagList = list;
			}
		}
	}

	private String transTagList(List<String> tags) {
		String str = "";
		if (tags != null && tags.size() > 0) {
			for (String t : tags) {
				str += t + "  ";
			}
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	@Click
	void img_manage_layout() {
		Intent intent = new Intent(this, SchoolImgManageActivity_.class);
		startActivityForResult(intent, ActivityCode.SCHOOL_IMG_MANAGE_ACTIVITY);
	}

	void startChangeInfoActivity(int type, String value) {
		Intent intent = new Intent(this, ChangeInfoActivity_.class);
		intent.putExtra("type", type);
		intent.putExtra("value", value);
		startActivityForResult(intent, ActivityCode.CHANGE_INFO_ACTIVITY);
	}

	@Click
	void password_reset_layout() {
		Intent intent = new Intent(this, PasswordResetActivity_.class);
		startActivityForResult(intent, ActivityCode.PASSWORD_RESET_ACTIVITY);
	}

	@Click
	void btn_change_phone() {
		Intent intent = new Intent(this, ChangePhoneActivity_.class);
		intent.putExtra("currNumber", phone.getText().toString());
		startActivityForResult(intent, ActivityCode.CHANGE_PHONE_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.CHANGE_PHONE_ACTIVITY)
	void onReturnFromChangePhoneActivity(int resultCode, Intent intent) {
		String newNumber = intent.getStringExtra("newNumber");
		if (newNumber != null)
			phone.setText(newNumber);
	}

	@OnActivityResult(ActivityCode.CHANGE_INFO_ACTIVITY)
	void onReturnFromChangeInfoActivity(int resultCode, Intent intent) {
		if (resultCode == 0) {
			return;
		}

		int infoType = intent.getIntExtra("type", -1);
		if (infoType == -1) {
			Toast.makeText(getApplicationContext(), "返回信息错误",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String newValue = intent.getStringExtra("newValue");
		switch (infoType) {
		case InfoType.NAME:
			name.setText(newValue);
			name2.setText(newValue);
			break;
		case InfoType.EMAIL:
			email.setText(newValue);
			break;
		case InfoType.INTRODUCTION:
			introduction.setText(newValue);
			break;
		case InfoType.OPENTIME:
			open_time.setText(newValue);
			break;
		case InfoType.ADDRESS:
			address.setText(newValue);
			break;
		default:
			break;
		}
	}

	@Click
	void btn_logout() {
		// Toast.makeText(getApplicationContext(), "退出", Toast.LENGTH_SHORT)
		// .show();
		myApp.setCurrentUser(null);
		prefs.token().remove();
		prefs.password().remove();
		Intent intent = new Intent(this, LoginActivity_.class);
		startActivityForResult(intent, ActivityCode.LOGIN_ACTIVITY);
	}

	@OnActivityResult(ActivityCode.LOGIN_ACTIVITY)
	void onReturnFromLoginActivity() {
		this.finish();
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from ProfileOrganiztionActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}
}
