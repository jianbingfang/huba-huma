package com.hubahuma.mobile.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
