package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadUserByTaskIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadUserByTaskIdJPA implements LoadUserByTaskIdDriverPort{

	private AppGenericMapper<UserJPA, User> mapper;
	private TaskJPARepository repository;
	
	@Autowired
	public LoadUserByTaskIdJPA(AppGenericMapper<UserJPA, User> mapper, TaskJPARepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}


	@Override
	public User load(long taskId) throws AppTaskNotFoundException {
		TaskJPA task = repository.findById(taskId).orElseThrow(() -> 
			new AppTaskNotFoundException("Task not found with id: "+taskId));
		return mapper.map(task.getProject().getUser());
	}

}
