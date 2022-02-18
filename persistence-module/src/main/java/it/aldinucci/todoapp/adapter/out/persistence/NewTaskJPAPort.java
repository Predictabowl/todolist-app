package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.domain.Task;

@Component
public class NewTaskJPAPort implements CreateTaskDriverPort{

	@Override
	public Task create(NewTaskDTOOut task) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

}
