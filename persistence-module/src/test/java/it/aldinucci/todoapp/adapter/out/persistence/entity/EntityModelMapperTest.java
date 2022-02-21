package it.aldinucci.todoapp.adapter.out.persistence.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.domain.User;

class EntityModelMapperTest {

	private ModelMapper mapper;
	
	@BeforeEach
	void setUp() {
		mapper = new ModelMapper();
	}
	
	@Test
	void test_TaskJPAToTask() {
		TaskJPA taskJpa = new TaskJPA(2L, "test name", "test description", true, null);
		
		Task task = mapper.map(taskJpa, Task.class);
		
		assertThat(task.getId()).isEqualTo(taskJpa.getId());
		assertThat(task.getName()).isEqualTo(taskJpa.getName());
		assertThat(task.getDescription()).isEqualTo(taskJpa.getDescription());
		assertThat(task.isCompleted()).isEqualTo(taskJpa.isCompleted());
	}
	
	@Test
	void test_ProjectJPAToProject() {
		ProjectJPA projectJPA = new ProjectJPA(5L, "name", null);
		
		Project project = mapper.map(projectJPA, Project.class);
		
		assertThat(project.getId()).isEqualTo(projectJPA.getId());
		assertThat(project.getName()).isEqualTo(projectJPA.getName());
	}
	
	@Test
	void test_UserJPAToUser() {
		UserJPA userJPA = new UserJPA(7L, "email", "username", "password");
		
		User user = mapper.map(userJPA, User.class);
		
		assertThat(user.getEmail()).isEqualTo(userJPA.getEmail());
		assertThat(user.getUsername()).isEqualTo(userJPA.getUsername());
		assertThat(user.getPassword()).isEqualTo(userJPA.getPassword());
	}
}
