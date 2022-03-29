package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.UpdateProjectDriverPort;
import it.aldinucci.todoapp.domain.Project;

class UpdateProjectServiceTest {

	@Mock
	private UpdateProjectDriverPort updateProject;
	
	@InjectMocks
	private UpdateProjectService sut;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_updateWhenProjectDontExists() {
		when(updateProject.update(isA(Project.class))).thenReturn(Optional.empty());
		
		Optional<Project> optionalProject = sut.update(new ProjectIdDTO("3"), new ProjectDataDTOIn("new name"));
		
		assertThat(optionalProject).isEmpty();
		verify(updateProject).update(new Project("3", "new name"));
	}
	
	@Test
	void test_updateWhenProject_success() {
		Project project = new Project("4L", "something");
		when(updateProject.update(isA(Project.class))).thenReturn(Optional.of(project));
		
		Optional<Project> optionalProject = sut.update(new ProjectIdDTO("3"), new ProjectDataDTOIn("new name"));
		
		assertThat(optionalProject).containsSame(project);
		verify(updateProject).update(new Project("3", "new name"));
	}

}
