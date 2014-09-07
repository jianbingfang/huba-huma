package com.hubahuma.mobile.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class UtilTools {

	public static String ParseDate(Date date) {
		return date.toLocaleString();
	}

	public static boolean isEmpty(String str) {
		if (str != null && !str.equals(""))
			return false;
		else
			return true;
	}

	public static boolean isMobileNumber(String number) {
		Pattern p = Pattern.compile("^[1][0-9]{10}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}

	public static boolean isEmail(String email) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isNetConnected(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}
		return false;
	}
}
