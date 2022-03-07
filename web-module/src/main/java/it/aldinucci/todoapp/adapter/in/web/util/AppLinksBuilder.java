package it.aldinucci.todoapp.adapter.in.web.util;

import it.aldinucci.todoapp.application.port.in.dto.VerificationLinkDTO;

public class AppLinksBuilder {

	private AppLinksBuilder() {
	}
	
	public static VerificationLinkDTO buildVerificationLink(String contextPath, String tokenCode, String email) {
		return new VerificationLinkDTO(	contextPath+"/user/register/verification/"+tokenCode, email);
	}
}
