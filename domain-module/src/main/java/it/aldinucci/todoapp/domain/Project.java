package it.aldinucci.todoapp.domain;

import java.util.Objects;

/**
 * There's actually no need to make the list of tasks to contains only unique id
 * because only a new task can be added to a project, and no task can't exist without a project.
 * If in the future a new service could break this constraint then is that service's
 * responsibility to upheld this business rule.
 * @author piero
 *
 */

public class Project{

	private String id;
	private String name;

	public Project() {
	}

	public Project(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + "]";
	}
	

}
