package it.aldinucci.todoapp.application.mapper;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;

public interface TaskMapper {

	public NewTaskDTOOut map(NewTaskDTOIn task);
}
