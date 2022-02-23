package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.mapper.ProjectMapper;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;
import it.aldinucci.todoapp.domain.Project;

class CreateNewProjectServiceTest {

	@Mock
	private CreateProjectDriverPort port;
	
	@Mock
	private ProjectMapper mapper;
	
	private CreateNewProjectService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		service = new CreateNewProjectService(port, mapper);
	}
	
	@Test
	void test_serviceShouldCallPort() {
		NewProjectDTOIn newProjectIn = new NewProjectDTOIn("test project","test@email.com");
		NewProjectDTOOut newProjectOut = new NewProjectDTOOut("test project", "test@email.com");
		Project createdProject = new Project(1L, "first project");
		when(port.create(isA(NewProjectDTOOut.class))).thenReturn(createdProject);
		when(mapper.map(newProjectIn)).thenReturn(newProjectOut);
		
		Project resultProject = service.create(newProjectIn);
		
		verify(port).create(newProjectOut);
		assertThat(resultProject).isSameAs(createdProject);
	}


}
