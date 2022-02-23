package it.aldinucci.todoapp.application.mapper;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;

public class TaskMapperImpl implements TaskMapper {

	@Override
	public NewTaskDTOOut map(NewTaskDTOIn task) {
		return new  NewTaskDTOOut(task.getName(), task.getDescription(), task.getProjectId());
	}

}
