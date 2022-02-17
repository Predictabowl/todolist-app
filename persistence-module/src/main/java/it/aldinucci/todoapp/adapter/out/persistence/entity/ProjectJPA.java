package it.aldinucci.todoapp.adapter.out.persistence.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ProjectJPA {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany
	private List<TaskJPA> tasks;
	
	public ProjectJPA() {
		tasks = new LinkedList<>(); 
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TaskJPA> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskJPA> tasks) {
		this.tasks = tasks;
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
		ProjectJPA other = (ProjectJPA) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ProjectJPA [id=" + id + ", name=" + name + "]";
	}
	
}
