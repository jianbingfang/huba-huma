package com.hubahuma.mobile.service;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Rest(converters = { StringHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
public interface SmsService extends RestClientErrorHandling {

	RestTemplate getRestTemplate();

	void setRestTemplate(RestTemplate restTemplate);

	@Post("http://gbk.sms.webchinese.cn/?Uid={uid}&Key={key}&smsMob={phone}&smsText={content}")
	String sendSMS(String uid, String key, String phone, String content);

}
