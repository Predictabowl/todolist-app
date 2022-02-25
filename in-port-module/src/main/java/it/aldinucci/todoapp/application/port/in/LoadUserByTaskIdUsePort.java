package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.User;

public interface LoadUserByTaskIdUsePort {

	public User load(TaskIdDTO taskId);
}
