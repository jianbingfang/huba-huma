package com.hubahuma.mobile.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.SetsCookie;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.hubahuma.mobile.entity.UserEntity;

@Rest(rootUrl = "http://192.168.0.104:8080/hubahuma", converters = { MappingJacksonHttpMessageConverter.class })
public interface UserService {

	@Post("/login?username={username}&password={password}")
	@SetsCookie("JSESSIONID")
	Boolean login(String username, String password);

	@Post("/getUser/{id}")
	@Accept(MediaType.APPLICATION_JSON)
	ResponseEntity<UserEntity> getUser(String id);
	
}