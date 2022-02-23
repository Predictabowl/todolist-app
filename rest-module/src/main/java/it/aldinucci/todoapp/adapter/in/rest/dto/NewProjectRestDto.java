package it.aldinucci.todoapp.adapter.in.rest.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;

public class NewProjectRestDto {
	
	@NotEmpty
	private String name;
	
	public NewProjectRestDto() {
	}

	public NewProjectRestDto(@NotEmpty String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		NewProjectRestDto other = (NewProjectRestDto) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "NewProjectRestDto [name=" + name + "]";
	}
	
}
