package it.aldinucci.todoapp.adapter.out.persistence.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ProjectJPA {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<TaskJPA> tasks;
	
	@ManyToOne (optional = false)
	private UserJPA user;
	
	public ProjectJPA() {
		tasks = new LinkedList<>(); 
	}
	
	public ProjectJPA(Long id, String name, List<TaskJPA> tasks, UserJPA user) {
		this.id = id;
		this.name = name;
		this.tasks = tasks;
		this.user = user;
	}
	
	public ProjectJPA(Long id, String name, UserJPA user) {
		this.id = id;
		this.name = name;
		this.tasks = new LinkedList<>();
		this.user = user;
	}
	
	public ProjectJPA(String name, UserJPA user) {
		this.id = null;
		this.name = name;
		this.user = user;
		this.tasks = new LinkedList<>();
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
	
	public UserJPA getUser() {
		return user;
	}

	public void setUser(UserJPA user) {
		this.user = user;
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
