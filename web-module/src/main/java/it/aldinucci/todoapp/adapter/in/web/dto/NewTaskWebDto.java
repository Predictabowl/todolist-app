package it.aldinucci.todoapp.adapter.in.web.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewTaskWebDto {
	
	@NotEmpty
	@NotNull
	@Size(max = 255)
	private String name;
	
	@NotNull
	@Size(max = 1024)
	private String description;

	public NewTaskWebDto(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewTaskWebDto other = (NewTaskWebDto) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "NewTaskWebDto [name=" + name + ", description=" + description + "]";
	}
	
}
