package com.hubahuma.mobile.utils;

import java.util.Date;

import android.text.format.DateUtils;

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
}
