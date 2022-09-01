package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import it.aldinucci.todoapp.application.port.in.model.AppEmail;

public class UserIdDTO{

	private final AppEmail id;
	
	public UserIdDTO(String id) {
		this.id = new AppEmail(id);
	}

	public String getId() {
		return id.getEmail();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserIdDTO other = (UserIdDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UserIdDTO [id=" + id + "]";
	}	
	
}
