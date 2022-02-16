package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.NewTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;

@Component
public class NewTaskJPAPort implements NewTaskDriverPort{

	@Override
	public Task save(Task task) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

}
