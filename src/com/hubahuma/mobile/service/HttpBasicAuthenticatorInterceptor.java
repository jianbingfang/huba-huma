package com.hubahuma.mobile.service;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import android.util.Log;

public class HttpBasicAuthenticatorInterceptor implements
		ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] data,
			ClientHttpRequestExecution execution) throws IOException {
		// do something
		Log.d("Http Request", request.getURI().toString());
		return execution.execute(request, data);
	}
}