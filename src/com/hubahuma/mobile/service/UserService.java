package com.hubahuma.mobile.service;

import java.io.IOException;
import java.util.List;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.SetsCookie;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.Parent;
import com.hubahuma.mobile.entity.Teacher;
import com.hubahuma.mobile.entity.User;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.entity.service.AuthParam;
import com.hubahuma.mobile.entity.service.AuthResp;
import com.hubahuma.mobile.entity.service.ChangePasswordParam;
import com.hubahuma.mobile.entity.service.ChangePasswordResp;
import com.hubahuma.mobile.entity.service.FetchDetailOrgParam;
import com.hubahuma.mobile.entity.service.FetchDetailOrgResp;
import com.hubahuma.mobile.entity.service.FetchDetailParentParam;
import com.hubahuma.mobile.entity.service.FetchDetailParentResp;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherParam;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherResp;
import com.hubahuma.mobile.entity.service.RegisterOrgParam;
import com.hubahuma.mobile.entity.service.RegisterOrgResp;
import com.hubahuma.mobile.entity.service.RegisterParentParam;
import com.hubahuma.mobile.entity.service.RegisterParentResp;
import com.hubahuma.mobile.entity.service.RegisterTeacherParam;
import com.hubahuma.mobile.entity.service.RegisterTeacherResp;
import com.hubahuma.mobile.entity.service.SearchOrgParam;
import com.hubahuma.mobile.entity.service.SearchTeacherParam;
import com.hubahuma.mobile.entity.service.UpdateOrgParam;
import com.hubahuma.mobile.entity.service.UpdateParentParam;
import com.hubahuma.mobile.entity.service.UpdateTeacherParam;

/**
 * "https://182.92.131.156:8080/api/"
 * "http://192.168.2.103:8080/server"
 */
@Rest(rootUrl = "http://101.5.81.34:8080/server", converters = { MappingJacksonHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
public interface UserService extends RestClientErrorHandling {

	RestTemplate getRestTemplate();

	void setRestTemplate(RestTemplate restTemplate);

	@Post("/getUser/{id}")
	@Accept(MediaType.APPLICATION_JSON)
	UserEntity getUser(String id);

	@Post("/authenticate")
	@Accept(MediaType.APPLICATION_JSON)
	AuthResp login(AuthParam auth);

	@Post("/register-org")
	@Accept(MediaType.APPLICATION_JSON)
	RegisterOrgResp registerOrg(RegisterOrgParam org);

	@Post("/register-teacher")
	@Accept(MediaType.APPLICATION_JSON)
	RegisterTeacherResp registerTeacher(RegisterTeacherParam teacher);

	@Post("/register-parent")
	@Accept(MediaType.APPLICATION_JSON)
	RegisterParentResp registerParent(RegisterParentParam parent);

	@Post("/search-org?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	List<Organization> searchOrg(SearchOrgParam searchOrg, String token);

	@Post("/search-teacher?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	List<Teacher> searchTeacher(SearchTeacherParam searchTeacher, String token);

	@Post("/search-parent?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	List<Parent> searchParent(String name, String token);

	@Post("/fetch-detail-org?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailOrgResp fetchDetailOrgByOrgId(List<String> orgId, String token);

	@Post("/fetch-detail-org?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailOrgResp fetchDetailOrgByUserId(List<String> userId, String token);

	@Post("/fetch-detail-org?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailOrgResp fetchDetailOrgByUsername(List<String> username,
			String token);

	@Post("/fetch-detail-teacher?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailTeacherResp fetchDetailTeacherByTeacherId(
			List<String> teacherId, String token);

	@Post("/fetch-detail-teacher?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailTeacherResp fetchDetailTeacherByUserId(List<String> userId,
			String token);

	@Post("/fetch-detail-teacher?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailTeacherResp fetchDetailTeacherByUsername(List<String> username,
			String token);

	@Post("/fetch-detail-parent?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailParentResp fetchDetailParentByParentId(List<String> parentId,
			String token);

	@Post("/fetch-detail-parent?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailParentResp fetchDetailParentByUserId(List<String> userId,
			String token);

	@Post("/fetch-detail-parent?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailParentResp fetchDetailParentByUsername(List<String> username,
			String token);

	@Post("/update-org?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	void updateOrg(UpdateOrgParam updateOrgParam, String token);

	@Post("/update-teacher?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	void updateTeacher(UpdateTeacherParam updateTeacherParam, String token);

	@Post("/update-parent?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	void updateParent(UpdateParentParam updateParentParam, String token);

	@Post("/change-password?_accessToken={token}")
	@Accept(MediaType.APPLICATION_JSON)
	ChangePasswordResp chagePassword(ChangePasswordParam updateParentParam,
			String token);

	@Post("/bind-phone?userId={userId}&phone={phone}")
	@Accept(MediaType.APPLICATION_JSON)
	void bindPhone(String userId, String phone);

	@Post("/verify-bind-phone?userId={userId}&code={code}")
	@Accept(MediaType.APPLICATION_JSON)
	void verifyBindPhone(String userId, String code);
}
