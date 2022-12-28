package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.GetTaskMaxOrderInProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppInvalidIdException;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@Service
@Transactional
class CreateNewTaskService implements CreateTaskUsePort{

	private final CreateTaskDriverPort newTaskPort;
	private final GetTaskMaxOrderInProjectDriverPort getMaxOrderTask;
	
	public CreateNewTaskService(CreateTaskDriverPort newTaskPort, GetTaskMaxOrderInProjectDriverPort getMaxOrderTask) {
		super();
		this.newTaskPort = newTaskPort;
		this.getMaxOrderTask = getMaxOrderTask;
	}


	@Override
	public Task create(NewTaskDTOIn task) throws AppProjectNotFoundException  {
		int order = 0;
		try {
			order = getMaxOrderTask.get(task.getProjectId()).orElse(-1)+1;
		} catch (AppInvalidIdException e) {
			throw new AppProjectNotFoundException("Project not found with id: " + task.getProjectId(), e);
		}
		
		return newTaskPort.create(new NewTaskData(
				task.getName(), 
				task.getDescription(),
				false,
				task.getProjectId(),
				order));
	}

}
