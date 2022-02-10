package it.aldinucci.todoapp.adapter.in.persistence;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.NewTaskPort;
import it.aldinucci.todoapp.domain.Task;

@Component
public class NewTaskJPAPort implements NewTaskPort{

	@Override
	public Task save(Task task) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

}
