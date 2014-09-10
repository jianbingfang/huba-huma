package com.hubahuma.mobile.service;

import org.androidannotations.annotations.EBean;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.util.Log;

@EBean
public class MyErrorHandler implements RestErrorHandler {
	@Override
	public void onRestClientExceptionThrown(RestClientException e) {
		// Do whatever you want here.
		Log.e("Http Error", e.getMessage());
	}
}