package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class TaskJpaToTaskMapper implements AppGenericMapper<TaskJPA, Task>{

	@Override
	public Task map(TaskJPA model) {
		Task task = new Task(model.getId(), model.getName(), model.getDescription());
		task.setCompleted(model.isCompleted());
		return task;
	}

}
