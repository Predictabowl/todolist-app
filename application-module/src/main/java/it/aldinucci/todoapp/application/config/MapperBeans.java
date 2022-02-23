package it.aldinucci.todoapp.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.aldinucci.todoapp.application.mapper.ProjectMapper;
import it.aldinucci.todoapp.application.mapper.ProjectMapperImpl;
import it.aldinucci.todoapp.application.mapper.TaskMapper;
import it.aldinucci.todoapp.application.mapper.TaskMapperImpl;

@Configuration
public class MapperBeans {
	
	@Bean
	public ProjectMapper getProjectMapper() {
		return new ProjectMapperImpl();
	}
	
	@Bean
	public TaskMapper getTaskMapper() {
		return new TaskMapperImpl();
	}

}
