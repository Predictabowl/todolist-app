package it.aldinucci.todoapp.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.aldinucci.todoapp.application.mapper.ProjectMapperInOut;
import it.aldinucci.todoapp.application.mapper.TaskMapperInOut;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Configuration
public class AppMapperBeans {
	
	@Bean
	public AppGenericMapper<NewTaskDTOIn, NewTaskDTOOut> getTaskMapper() {
		return new TaskMapperInOut();
	}
	
	@Bean
	public AppGenericMapper<NewProjectDTOIn, NewProjectDTOOut> getNewProjectMapper(){
		return new ProjectMapperInOut();
	}

}
