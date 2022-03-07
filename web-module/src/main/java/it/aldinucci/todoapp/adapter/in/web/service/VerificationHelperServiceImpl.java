package it.aldinucci.todoapp.adapter.in.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateOrReplaceVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.VerificationLinkDTO;
import it.aldinucci.todoapp.domain.VerificationToken;

@Service
public class VerificationHelperServiceImpl implements VerificationHelperService {

	private CreateOrReplaceVerificationTokenUsePort createVerificationToken;
	private SendVerificationEmailUsePort sendVerificationEmail;
	
	@Autowired
	public VerificationHelperServiceImpl(CreateOrReplaceVerificationTokenUsePort createVerificationToken,
			SendVerificationEmailUsePort sendVerificationEmail) {
		this.createVerificationToken = createVerificationToken;
		this.sendVerificationEmail = sendVerificationEmail;
	}

	@Override
	public void sendVerifcationMail(String userEmail, String verificationUrl) {
		VerificationToken verificationToken = createVerificationToken.create(new UserIdDTO(userEmail));
		sendVerificationEmail.send(	new VerificationLinkDTO(verificationUrl+"/"+verificationToken.getToken(),userEmail));
	}
}
