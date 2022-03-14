package it.aldinucci.todoapp.adapter.in.web.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewProjectWebDto {

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String name;

	public NewProjectWebDto(@NotNull @NotEmpty String name) {
		super();
		this.name = name;
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
		NewProjectWebDto other = (NewProjectWebDto) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "NewProjectWebDto [name=" + name + "]";
	}
	
	
}
