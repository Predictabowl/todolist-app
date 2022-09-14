package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class CreateNewProjectServiceTest {

	@Mock
	private CreateProjectDriverPort port;
	
	@Mock
	private AppGenericMapper<NewProjectDTOIn, NewProjectData> mapper;
	
	@InjectMocks
	private CreateNewProjectService service;
	
	@Test
	void test_serviceShouldCallPort() throws Exception {
		AutoCloseable closeable = openMocks(this);
		NewProjectDTOIn newProjectIn = new NewProjectDTOIn("test project","test@email.com");
		NewProjectData newProjectOut = new NewProjectData("test project", "test@email.com");
		Project createdProject = new Project("1L", "first project");
		when(port.create(isA(NewProjectData.class))).thenReturn(createdProject);
		when(mapper.map(newProjectIn)).thenReturn(newProjectOut);
		
		Project resultProject = service.create(newProjectIn);
		
		verify(port).create(newProjectOut);
		assertThat(resultProject).isSameAs(createdProject);
		closeable.close();
	}


}
