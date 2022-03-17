package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.User;

public interface LoadUserByTaskIdUsePort {

	public Optional<User> load(TaskIdDTO taskId);
}
