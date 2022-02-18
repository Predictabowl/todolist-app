package it.aldinucci.todoapp.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.list.SetUniqueList;

/**
 * There's actually no need to make the list of tasks to contains only unique id
 * because only a new task can be added to a project, and no task can't exist without a project.
 * If in the future a new service could break this constraint then is that service's
 * responsibility to upheld this business rule.
 * @author piero
 *
 */

public class Project{

	private Long id;
	private String name;
	private SetUniqueList<Task> tasks;
	private User user;
	
	public Project(Long id, String name, User user, List<Task> tasks) {
		this.id = id;
		this.name = name;
		this.user = user;
		this.tasks = SetUniqueList.setUniqueList(tasks);
	}
	
	public Project(Long id, String name, User user) {
		this.id = id;
		this.name = name;
		this.user = user;
		this.tasks = SetUniqueList.setUniqueList(new LinkedList<Task>());
	}
	
	public Project(Long id, String name) {
		this.id = id;
		this.name = name;
		this.user = null;
		this.tasks = SetUniqueList.setUniqueList(new LinkedList<Task>());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
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
		return "Project [id=" + id + ", name=" + name + ", tasks=" + tasks + ", user=" + user + "]";
	}
	
}
