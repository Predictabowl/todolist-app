package it.aldinucci.todoapp.adapter.out.persistence.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TaskJPA {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(length = 1024)
	private String description;
	
	private boolean completed;
	
	@ManyToOne(optional = false)
	private ProjectJPA project;
	
	private int orderInProject;
	
	public TaskJPA() {
		this.orderInProject = 0;
	}

	public TaskJPA(Long id, String name, String description, boolean completed, ProjectJPA project) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = completed;
		this.project = project;
		this.orderInProject = 0;
	}

	public TaskJPA(String name, String description, boolean completed, ProjectJPA project) {
		this.id = null;
		this.name = name;
		this.description = description;
		this.completed = completed;
		this.project = project;
		this.orderInProject = 0;
	}
	
	public TaskJPA(Long id, String name, String description, boolean completed, ProjectJPA project,
			int orderInProject) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = completed;
		this.project = project;
		this.orderInProject = orderInProject;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public ProjectJPA getProject() {
		return project;
	}

	public void setProject(ProjectJPA project) {
		this.project = project;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public final int getOrderInProject() {
		return orderInProject;
	}

	public final void setOrderInProject(int orderInProject) {
		this.orderInProject = orderInProject;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskJPA other = (TaskJPA) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "TaskJPA [id=" + id + ", name=" + name + ", description=" + description + ", completed=" + completed
				+ ", project=" + project + ", orderInProject=" + orderInProject + "]";
	}

}
