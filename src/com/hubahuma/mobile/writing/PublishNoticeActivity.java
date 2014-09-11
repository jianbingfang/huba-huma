package com.hubahuma.mobile.writing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hubahuma.mobile.LoadingDialog_;
import com.hubahuma.mobile.PromptDialog.PromptDialogListener;
import com.hubahuma.mobile.MyApplication;
import com.hubahuma.mobile.PromptDialog_;
import com.hubahuma.mobile.R;
import com.hubahuma.mobile.SelectImgPopupWindow;
import com.hubahuma.mobile.entity.NoticeEntity;
import com.hubahuma.mobile.utils.GlobalVar;
import com.hubahuma.mobile.utils.UtilTools;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_publish_notice)
public class PublishNoticeActivity extends FragmentActivity implements
		PromptDialogListener {

	@App
	MyApplication myApp;
	
	@ViewById
	ProgressBar progress_bar;

	@ViewById
	ImageView portrait, image;

	@ViewById
	EditText title, content;

	@ViewById
	TextView name, date;

	@ViewById
	ImageButton btn_submit;

	@ViewById
	Button btn_add_img;

	private LoadingDialog_ loadingDialog;

	private PromptDialog_ promptDialog;

	private boolean publishSucc = false;

	private boolean imgAdded = false;

	private Bitmap photo;

	SelectImgPopupWindow menuWindow;

	private NoticeEntity newNotice;

	@AfterViews
	void init() {
		loadingDialog = new LoadingDialog_();
		promptDialog = new PromptDialog_();
		promptDialog.setDialogListener(this);
		btn_submit.setVisibility(View.GONE);
		name.setText(myApp.getCurrentUser().getUsername());
		date.setText(UtilTools.parseDate(new Date()));
	}

	@AfterTextChange(R.id.content)
	void onContentTextChange(Editable text) {
		if (UtilTools.isEmpty(text.toString())) {
			btn_submit.setVisibility(View.GONE);
		} else {
			btn_submit.setVisibility(View.VISIBLE);
		}
	}

	@Click
	void btn_add_img() {
		if (imgAdded) {
			image.setImageDrawable(null);
			btn_add_img.setText("添加照片");
			imgAdded = false;
		} else {
			menuWindow = new SelectImgPopupWindow(this, itemsOnClick);
			menuWindow.showAtLocation(
					this.findViewById(R.id.publish_notice_layout),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}

	@Click
	void btn_submit() {
		NoticeEntity notice = new NoticeEntity();
		notice.setUser(myApp.getCurrentUser());
		notice.setTitle(title.getText().toString().trim());
		notice.setContent(content.getText().toString().trim());
		notice.setDate(new Date());
		notice.setImage(null);
		showLoadingDialog();
		handlePublishNotice(notice);
	}

	@UiThread
	void showLoadingDialog() {
		loadingDialog.show(getSupportFragmentManager(), "dialog_loading");
	}

	@UiThread
	void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	@UiThread
	void showPromptDialog(String title, String content) {
		promptDialog.setTitle(title);
		promptDialog.setContent(content);
		promptDialog.show(getSupportFragmentManager(), "dialog_prompt");
	}

	@UiThread
	void dismissPromptDialog() {
		promptDialog.dismiss();
	}

	@Background(delay = 1000)
	void handlePublishNotice(NoticeEntity notice) {
		publishSucc = publishNotice(notice);
		dismissLoadingDialog();
		if (publishSucc) {
			showPromptDialog("提示", "发布成功!");
			newNotice = notice;
		} else {
			showPromptDialog("错误", "发布失败!");
		}
	}

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

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	// 创建一个以当前时间为名称的文件
	// private final String IMAGE_FILE_LOCATION = getPhotoFileName();
	File tempFile = new File(Environment.getExternalStorageDirectory(),
			getPhotoFileName());

	// Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

	boolean publishNotice(NoticeEntity notice) {
		// TODO 发送notice数据给后台

		return true;
	}

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
		// if (imageUri != null) {
		// Bitmap bitmap = decodeUriAsBitmap(imageUri);// decode bitmap
		// image.setImageBitmap(bitmap);
		// imgAdded = true;
		// btn_add_img.setText("删除图片");
		// }
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 300);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	// 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			photo = bundle.getParcelable("data");
			image.setImageBitmap(photo);
			imgAdded = true;
			btn_add_img.setText("删除图片");
		}
	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'file:///sdcard/hubahuma/IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from PublishNoticeActivity");
		if (publishSucc) {
			intent.putExtra("newNotice", newNotice);
			this.setResult(1, intent);
		} else {
			this.setResult(0, intent);
		}
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	@Override
	public void onDialogConfirm() {
		dismissPromptDialog();
		if (publishSucc) {
			btn_back();
		}
	}
}
