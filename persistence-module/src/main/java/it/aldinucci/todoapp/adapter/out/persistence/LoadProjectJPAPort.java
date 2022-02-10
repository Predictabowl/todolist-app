package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.LoadProjectPort;
import it.aldinucci.todoapp.domain.Project;

@Component
public class LoadProjectJPAPort implements LoadProjectPort{

	@Override
	public Project loadProject(Long projectId) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

}
