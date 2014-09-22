package com.hubahuma.mobile.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.hubahuma.mobile.utils.UtilTools;

import android.util.Log;

public class HttpBasicAuthenticatorInterceptor implements
		ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] data,
			ClientHttpRequestExecution execution) throws IOException {
		// do something
		Log.d("Http Request", request.getURI().toString());
//		Map<String, String> map = request.getHeaders().values();
//		for(String key : request.getHeaders().keySet()){
//			map.put(key, request.getHeaders().);
//		}
		Log.d("Http RequestHead", UtilTools.object2json(request.getHeaders().values()));
		return execution.execute(request, data);
	}
}