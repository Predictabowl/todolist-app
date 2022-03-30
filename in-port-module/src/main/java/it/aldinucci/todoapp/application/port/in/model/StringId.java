package it.aldinucci.todoapp.application.port.in.model;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class StringId extends AutoValidatingInputModel<StringId>{
	
	@NotEmpty
	@NotNull
	private String id;

	public StringId(String id) {
		super();
		this.id = id;
		validateSelf();
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
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
		StringId other = (StringId) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "StringId [id=" + id + "]";
	}
	

}
