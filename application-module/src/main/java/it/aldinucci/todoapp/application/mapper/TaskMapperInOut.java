package it.aldinucci.todoapp.application.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class TaskMapperInOut implements AppGenericMapper<NewTaskDTOIn, NewTaskDTOOut> {

	@Override
	public NewTaskDTOOut map(NewTaskDTOIn task) {
		return new  NewTaskDTOOut(task.getName(), task.getDescription(), task.getProjectId());
	}

}
