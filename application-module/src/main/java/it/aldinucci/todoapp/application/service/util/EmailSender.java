package it.aldinucci.todoapp.application.service.util;

public interface EmailSender {

	public void send(String to, String subject,  String content);
}
