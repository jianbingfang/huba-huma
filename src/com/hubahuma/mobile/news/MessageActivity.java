package com.hubahuma.mobile.news;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

@NoTitle
@EActivity(R.layout.activity_message)
public class MessageActivity extends Activity {
	
	@Click
	void btn_back(){
		Intent intent = getIntent();
		intent.putExtra("result", "returned from MessageActivity");
        this.setResult(0, intent);
        this.finish();  
	}
	
	@Override
	public void onBackPressed(){
		btn_back();
	}
}
