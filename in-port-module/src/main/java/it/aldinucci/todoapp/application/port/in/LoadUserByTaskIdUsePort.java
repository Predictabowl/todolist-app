package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppTaskNotFoundException;

public interface LoadUserByTaskIdUsePort {

	public User load(TaskIdDTO taskId) throws AppTaskNotFoundException;
}
