package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.Project;

public interface LoadProjectByIdDriverPort {

	public Project loadById(long id); 
}
