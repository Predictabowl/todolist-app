package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import it.aldinucci.todoapp.application.port.in.model.AppEmail;
import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class EmailLinkDTO extends AutoValidatingInputModel<EmailLinkDTO>{

	@NotNull
	@URL
	@NotEmpty
	private final String link;
	
	private final AppEmail email;

	public EmailLinkDTO(String link, String email) {
		this.link = link;
		this.email = new AppEmail(email);
		validateSelf();
	}

	public String getLink() {
		return link;
	}

	public String getEmail() {
		return email.getEmail();
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, link);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailLinkDTO other = (EmailLinkDTO) obj;
		return Objects.equals(email, other.email) && Objects.equals(link, other.link);
	}

	@Override
	public String toString() {
		return "EmailLinkDTO [link=" + link + ", email=" + email + "]";
	}

}
