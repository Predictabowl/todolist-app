package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadProjectsByUserDriverPort;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadProjectsByUserIdService.class})
class LoadProjectsByUserIdServiceTest {

	@MockBean
	private LoadProjectsByUserDriverPort port;
	
	@Autowired
	private LoadProjectsByUserIdService service;
	
	@Test
	void test_serviceOrderProjectsByName() throws AppUserNotFoundException {
		UserIdDTO userId = new UserIdDTO("test@email.it");
		List<Project> projects = new LinkedList<Project>();
		Project project1 = new Project(3L, "Test Project");
		Project project2 = new Project(5L, "Some Project");
		Project project3 = new Project(7L, "First Project");
		projects.add(project1);
		projects.add(project2);
		projects.add(project3);
		
		when(port.load(isA(String.class))).thenReturn(projects);
		
		List<Project> loadedProjects = service.load(userId);
		
		verify(port).load("test@email.it");
		assertThat(loadedProjects).containsExactly(project3, project2, project1);
	}

}
