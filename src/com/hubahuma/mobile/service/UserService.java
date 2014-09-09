package com.hubahuma.mobile.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.SetsCookie;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.resp.AuthResp;

@Rest(rootUrl = "http://101.5.127.27:8080/server/", converters = { MappingJacksonHttpMessageConverter.class })
public interface UserService {

	@Get("/authenticate?username={username}&password={password}")
	@Accept(MediaType.APPLICATION_JSON)
	AuthResp login(String username, String password);

	@Get("/getUser/{id}")
	@Accept(MediaType.APPLICATION_JSON)
	UserEntity getUser(String id);

}