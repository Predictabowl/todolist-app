package it.aldinucci.todoapp.application.port.out;

import java.util.List;

import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.User;

public interface LoadAllUserProjectsDriverPort {
	
	public List<Project> load(User user);
}
