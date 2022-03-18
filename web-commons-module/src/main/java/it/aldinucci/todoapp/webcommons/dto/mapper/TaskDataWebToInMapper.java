package it.aldinucci.todoapp.webcommons.dto.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;

@Component
public class TaskDataWebToInMapper implements AppGenericMapper<TaskDataWebDto, TaskDataDTOIn>{

	@Override
	public TaskDataDTOIn map(TaskDataWebDto model) {
		return new TaskDataDTOIn(model.name(), model.description());
	}

}
