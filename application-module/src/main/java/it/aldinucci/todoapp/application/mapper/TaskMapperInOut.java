package it.aldinucci.todoapp.application.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class TaskMapperInOut implements AppGenericMapper<NewTaskDTOIn, NewTaskData> {

	@Override
	public NewTaskData map(NewTaskDTOIn task) {
		return new  NewTaskData(task.getName(), task.getDescription(), task.getProjectId());
	}

}
