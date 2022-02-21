package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.DeleteTaskDTOIn;

public interface DeleteTaskByIdUsePort {

	public void delete(DeleteTaskDTOIn task);
}
