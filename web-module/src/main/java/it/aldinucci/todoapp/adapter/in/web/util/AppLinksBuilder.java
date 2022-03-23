package it.aldinucci.todoapp.adapter.in.web.util;

import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;

public class AppLinksBuilder {

	private AppLinksBuilder() {
	}
	
	public static EmailLinkDTO buildVerificationLink(String contextPath, String tokenCode, String email) {
		return new EmailLinkDTO(contextPath+"/user/register/verification/"+tokenCode, email);
	}
	
	public static EmailLinkDTO buildResetPasswordLink(String contextPath, String tokenCode, String email) {
		return new EmailLinkDTO(contextPath+"/user/register/password/reset/perform/"+tokenCode, email);
	}
}
