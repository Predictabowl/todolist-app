package it.aldinucci.todoapp.domain;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.list.SetUniqueList;

public class Project{

	private Long id;
	private String name;
	private SetUniqueList<Task> tasks;
	
	public Project(Long id, String name, List<Task> tasks) {
		this.id = id;
		this.name = name;
		this.tasks = SetUniqueList.setUniqueList(tasks);
	}
	
	public Project(Long id, String name) {
		this.id = id;
		this.name = name;
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
	
}
