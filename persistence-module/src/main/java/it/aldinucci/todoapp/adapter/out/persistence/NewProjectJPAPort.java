package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.NewProjectDTOOut;
import it.aldinucci.todoapp.application.port.out.NewProjectDriverPort;
import it.aldinucci.todoapp.domain.Project;

 @Component
public class NewProjectJPAPort implements NewProjectDriverPort{

	@Override
	public Project create(NewProjectDTOOut project) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

}
