package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class UrlLinkDTO extends AutoValidatingInputModel<UrlLinkDTO>{

	@NotNull
	@URL
	@NotEmpty
	private String link;

	public UrlLinkDTO(String link) {
		this.link = link;
		validateSelf();
	}

	public String getLink() {
		return link;
	}

	@Override
	public int hashCode() {
		return Objects.hash(link);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlLinkDTO other = (UrlLinkDTO) obj;
		return Objects.equals(link, other.link);
	}

	@Override
	public String toString() {
		return "VerificationLinkDTO [link=" + link + "]";
	}

}
