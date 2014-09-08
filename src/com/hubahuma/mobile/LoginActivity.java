package com.hubahuma.mobile;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.hubahuma.mobile.entity.AuthResp;
import com.hubahuma.mobile.service.UserService;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

	@ViewById
	EditText username, password;

	@RestService
	UserService userService;

	@Click
	void btn_login() {
		handleLogin();
	}
	
	@Background
	void handleLogin(){
		AuthResp resp = userService.login(username.getText().toString(),
				password.getText().toString());
		
		System.out.println("result:"+resp.isResult()+", token:"+resp.getToken()+", type:"+resp.getType());
	}

}
