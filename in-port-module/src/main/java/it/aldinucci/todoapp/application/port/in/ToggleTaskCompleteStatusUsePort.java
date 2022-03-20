package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

public interface ToggleTaskCompleteStatusUsePort {

	public void toggle(TaskIdDTO taskId) throws AppTaskNotFoundException;
}
