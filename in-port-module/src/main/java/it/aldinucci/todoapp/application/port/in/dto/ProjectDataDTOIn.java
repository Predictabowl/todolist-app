package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class ProjectDataDTOIn extends AutoValidatingInputModel<ProjectDataDTOIn>{

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String name;

	public ProjectDataDTOIn(String name) {
		super();
		this.name = name;
		validateSelf();
	}

	public final String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectDataDTOIn other = (ProjectDataDTOIn) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "ProjectDataDTOIn [name=" + name + "]";
	}
	
}
