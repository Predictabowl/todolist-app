package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class TaskJpaToTaskMapper implements AppGenericMapper<TaskJPA, Task>{

	@Override
	public Task map(TaskJPA model) {
		return new Task(model.getId().toString(),
				model.getName(),
				model.getDescription(),
				model.isCompleted(),
				model.getOrderInProject());
	}

}
