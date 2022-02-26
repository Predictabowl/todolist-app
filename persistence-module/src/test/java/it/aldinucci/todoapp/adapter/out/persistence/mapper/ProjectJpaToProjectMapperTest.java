package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.domain.Project;

class ProjectJpaToProjectMapperTest {
	
	private ProjectJpaToProjectMapper mapper;
	
	@BeforeEach
	void setUp() {
		mapper = new ProjectJpaToProjectMapper();
	}
	
	@Test
	void test() {
		ProjectJPA projectJPA = new ProjectJPA(5L, "name", null);
		
		Project project = mapper.map(projectJPA);
		
		assertThat(project.getId()).isEqualTo(5);
		assertThat(project.getName()).isEqualTo("name");
	}

}
