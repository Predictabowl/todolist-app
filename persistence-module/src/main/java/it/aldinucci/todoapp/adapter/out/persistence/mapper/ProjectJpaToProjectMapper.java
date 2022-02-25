package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class ProjectJpaToProjectMapper implements AppGenericMapper<ProjectJPA, Project>{

	@Override
	public Project map(ProjectJPA model) {
		return new Project(model.getId(), model.getName());
	}

}
