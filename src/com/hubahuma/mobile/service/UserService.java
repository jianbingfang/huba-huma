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
import com.hubahuma.mobile.entity.service.BindPhoneParam;
import com.hubahuma.mobile.entity.service.BoolResp;
import com.hubahuma.mobile.entity.service.ChangePasswordParam;
import com.hubahuma.mobile.entity.service.ChangePasswordResp;
import com.hubahuma.mobile.entity.service.FetchAnnouncementParam;
import com.hubahuma.mobile.entity.service.FetchAnnouncementResp;
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
import com.hubahuma.mobile.entity.service.SearchParentParam;
import com.hubahuma.mobile.entity.service.SearchTeacherParam;
import com.hubahuma.mobile.entity.service.UpdateOrgParam;
import com.hubahuma.mobile.entity.service.UpdateParentParam;
import com.hubahuma.mobile.entity.service.UpdateTeacherParam;
import com.hubahuma.mobile.entity.service.VerifyBindPhoneParam;
import com.hubahuma.mobile.entity.service.WriteAnnouncementParam;
import com.hubahuma.mobile.entity.service.WriteAnnouncementResp;

/**
 * "https://182.92.131.156:8080/api"
 * "http://192.168.2.103:8080/server"
 */
@Rest(rootUrl = "http://192.168.2.103:8080/server", converters = { MappingJacksonHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
public interface UserService extends RestClientErrorHandling {

	RestTemplate getRestTemplate();

	void setRestTemplate(RestTemplate restTemplate);

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

	@Post("/search-org")
	@Accept(MediaType.APPLICATION_JSON)
	List<Organization> searchOrg(SearchOrgParam searchOrg);

	@Post("/search-teacher")
	@Accept(MediaType.APPLICATION_JSON)
	List<Teacher> searchTeacher(SearchTeacherParam searchTeacher);

	@Post("/search-parent")
	@Accept(MediaType.APPLICATION_JSON)
	List<Parent> searchParent(SearchParentParam searchParentParam);

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

	@Post("/update-org")
	@Accept(MediaType.APPLICATION_JSON)
	void updateOrg(UpdateOrgParam updateOrgParam);

	@Post("/update-teacher")
	@Accept(MediaType.APPLICATION_JSON)
	void updateTeacher(UpdateTeacherParam updateTeacherParam);

	@Post("/update-parent")
	@Accept(MediaType.APPLICATION_JSON)
	void updateParent(UpdateParentParam updateParentParam);

	@Post("/change-password")
	@Accept(MediaType.APPLICATION_JSON)
	ChangePasswordResp chagePassword(ChangePasswordParam updateParentParam);

	@Post("/bind-phone")
	@Accept(MediaType.APPLICATION_JSON)
	String bindPhone(BindPhoneParam bindPhoneParam);

	@Post("/verify-bind-phone")
	@Accept(MediaType.APPLICATION_JSON)
	BoolResp verifyBindPhone(VerifyBindPhoneParam param);
	
	@Post("/fetch-announcement")
	@Accept(MediaType.APPLICATION_JSON)
	FetchAnnouncementResp fetchAnnouncement(FetchAnnouncementParam param);
	
	@Post("/write-announcement")
	@Accept(MediaType.APPLICATION_JSON)
	WriteAnnouncementResp writeAnnouncement(WriteAnnouncementParam param);
}
