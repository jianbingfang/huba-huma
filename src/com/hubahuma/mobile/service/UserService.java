package com.hubahuma.mobile.service;

import java.util.List;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.hubahuma.mobile.entity.Organization;
import com.hubahuma.mobile.entity.Parent;
import com.hubahuma.mobile.entity.service.AuthParam;
import com.hubahuma.mobile.entity.service.AuthResp;
import com.hubahuma.mobile.entity.service.BindPhoneParam;
import com.hubahuma.mobile.entity.service.BoolResp;
import com.hubahuma.mobile.entity.service.ChangePasswordParam;
import com.hubahuma.mobile.entity.service.ChangePasswordResp;
import com.hubahuma.mobile.entity.service.DeleteContactsParam;
import com.hubahuma.mobile.entity.service.DenyVerificationRequestParentParam;
import com.hubahuma.mobile.entity.service.FetchAnnouncementParam;
import com.hubahuma.mobile.entity.service.FetchAnnouncementResp;
import com.hubahuma.mobile.entity.service.FetchContactsParam;
import com.hubahuma.mobile.entity.service.FetchContactsResp;
import com.hubahuma.mobile.entity.service.FetchDetailOrgResp;
import com.hubahuma.mobile.entity.service.FetchDetailParentParam;
import com.hubahuma.mobile.entity.service.FetchDetailParentResp;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherParam;
import com.hubahuma.mobile.entity.service.FetchDetailTeacherResp;
import com.hubahuma.mobile.entity.service.FetchVerificationRequestParentParam;
import com.hubahuma.mobile.entity.service.PassVerificationRequestParentParam;
import com.hubahuma.mobile.entity.service.RegisterOrgParam;
import com.hubahuma.mobile.entity.service.RegisterOrgResp;
import com.hubahuma.mobile.entity.service.RegisterParentParam;
import com.hubahuma.mobile.entity.service.RegisterParentResp;
import com.hubahuma.mobile.entity.service.RegisterTeacherParam;
import com.hubahuma.mobile.entity.service.RegisterTeacherResp;
import com.hubahuma.mobile.entity.service.SearchOrgParam;
import com.hubahuma.mobile.entity.service.SearchParentParam;
import com.hubahuma.mobile.entity.service.SearchTeacherParam;
import com.hubahuma.mobile.entity.service.SearchTeacherResp;
import com.hubahuma.mobile.entity.service.SendAnnouncementReadReceiptParam;
import com.hubahuma.mobile.entity.service.SendSmsToParentParam;
import com.hubahuma.mobile.entity.service.SendVerificationRequestParentParam;
import com.hubahuma.mobile.entity.service.UpdateOrgParam;
import com.hubahuma.mobile.entity.service.UpdateParentParam;
import com.hubahuma.mobile.entity.service.UpdateTeacherParam;
import com.hubahuma.mobile.entity.service.VerifyBindPhoneParam;
import com.hubahuma.mobile.entity.service.WriteAnnouncementParam;
import com.hubahuma.mobile.entity.service.WriteAnnouncementResp;

/**
 * "https://182.92.131.156:8080/api" "http://192.168.2.103:8080/server"
 */
@Rest(rootUrl = "http://182.92.131.156:8080/api", converters = { MappingJacksonHttpMessageConverter.class }, interceptors = { HttpBasicAuthenticatorInterceptor.class })
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
	SearchTeacherResp searchTeacher(SearchTeacherParam searchTeacher);

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

	@Post("/fetch-detail-teacher")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailTeacherResp fetchDetailTeacher(FetchDetailTeacherParam param);

	// @Post("/fetch-detail-teacher?_accessToken={token}")
	// @Accept(MediaType.APPLICATION_JSON)
	// FetchDetailTeacherResp fetchDetailTeacherByUserId(List<String> userId,
	// String token);
	//
	// @Post("/fetch-detail-teacher?_accessToken={token}")
	// @Accept(MediaType.APPLICATION_JSON)
	// FetchDetailTeacherResp fetchDetailTeacherByUsername(List<String>
	// username,
	// String token);

	@Post("/fetch-detail-parent")
	@Accept(MediaType.APPLICATION_JSON)
	FetchDetailParentResp fetchDetailParent(FetchDetailParentParam param);

	// @Post("/fetch-detail-parent?_accessToken={token}")
	// @Accept(MediaType.APPLICATION_JSON)
	// FetchDetailParentResp fetchDetailParentByUserId(List<String> userId,
	// String token);
	//
	// @Post("/fetch-detail-parent?_accessToken={token}")
	// @Accept(MediaType.APPLICATION_JSON)
	// FetchDetailParentResp fetchDetailParentByUsername(List<String> username,
	// String token);

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
	void bindPhone(BindPhoneParam bindPhoneParam);

	@Post("/verify-bind-phone")
	@Accept(MediaType.APPLICATION_JSON)
	BoolResp verifyBindPhone(VerifyBindPhoneParam param);

	@Post("/fetch-announcement")
	@Accept(MediaType.APPLICATION_JSON)
	FetchAnnouncementResp fetchAnnouncement(FetchAnnouncementParam param);

	@Post("/fetch-announcement-teacher")
	@Accept(MediaType.APPLICATION_JSON)
	FetchAnnouncementResp fetchAnnouncementTeacher(FetchAnnouncementParam param);
	
	@Post("/write-announcement")
	@Accept(MediaType.APPLICATION_JSON)
	WriteAnnouncementResp writeAnnouncement(WriteAnnouncementParam param);

	@Post("/send-verification-request-parent")
	@Accept(MediaType.APPLICATION_JSON)
	void sendVerificationRequestParent(SendVerificationRequestParentParam param);

	@Post("/fetch-verification-request-parent")
	@Accept(MediaType.APPLICATION_JSON)
	FetchVerificationRequestParentResp fetchVerificationRequestParent(
			FetchVerificationRequestParentParam param);

	@Post("/send-announcement-read-receipt")
	@Accept(MediaType.APPLICATION_JSON)
	void sendAnnouncementReadReceipt(SendAnnouncementReadReceiptParam param);

	@Post("/pass-verification-request-parent")
	@Accept(MediaType.APPLICATION_JSON)
	void passVerificationRequestParent(PassVerificationRequestParentParam param);
	
	@Post("/deny-verification-request-parent")
	@Accept(MediaType.APPLICATION_JSON)
	void denyVerificationRequestParent(DenyVerificationRequestParentParam param);
	
	@Post("/delete-contacts")
	@Accept(MediaType.APPLICATION_JSON)
	void deleteContacts(DeleteContactsParam param);
	
	@Post("/fetch-contacts")
	@Accept(MediaType.APPLICATION_JSON)
	FetchContactsResp fetchContacts(FetchContactsParam param);
	
	@Post("/teacher-send-sms-to-parent")
	@Accept(MediaType.APPLICATION_JSON)
	BoolResp sendSmsToParent(SendSmsToParentParam param);
}
