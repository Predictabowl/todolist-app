package it.aldinucci.todoapp.adapter.in.web.service;

public interface VerificationHelperService {

	public void sendVerifcationMail(String userEmail, String verificationUrl);
}
