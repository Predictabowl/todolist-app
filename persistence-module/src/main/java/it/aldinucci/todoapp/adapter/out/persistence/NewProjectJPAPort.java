package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.CreateProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.User;

 @Component
public class NewProjectJPAPort implements CreateProjectDriverPort{

	@Override
	public Project create(NewProjectDTOOut project) {
//		throw new UnsupportedOperationException("Method not yet implemented");
		return new Project(1L, "first project", new User("this@email.com", "mrio", "password"));
	}

}
