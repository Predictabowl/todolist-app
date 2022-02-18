package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.DeleteTaskDTOIn;

public interface DeleteTaskByIdUsePort {

	public boolean delete(DeleteTaskDTOIn task);
}
