package it.aldinucci.todoapp.application.port.out;

import java.util.List;

import it.aldinucci.todoapp.domain.Project;

public interface LoadAllProjectsDriverPort {

	public List<Project> load(String email);
}
