package it.aldinucci.todoapp.application.port.out;

import java.util.List;

import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface LoadProjectsByUserDriverPort {
	
	public List<Project> load(String userEmail) throws AppUserNotFoundException;
}
