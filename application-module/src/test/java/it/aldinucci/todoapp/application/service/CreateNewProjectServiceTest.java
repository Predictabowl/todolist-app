package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.NewProjectDTOOut;
import it.aldinucci.todoapp.application.port.out.NewProjectDriverPort;
import it.aldinucci.todoapp.domain.Project;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateNewProjectService.class, ModelMapper.class})
class CreateNewProjectServiceTest {

	@MockBean
	private NewProjectDriverPort port;
	
	@Autowired
	private CreateNewProjectService service;
	
	@Test
	void test_serviceShouldCallPort() {
		NewProjectDTOIn newProjectIn = new NewProjectDTOIn("test project","test@email.com","test description");
		NewProjectDTOOut newProjectOut = new NewProjectDTOOut("test project", "test@email.com", "test description");
		Project createdProject = new Project(1L, "first project");
		when(port.create(isA(NewProjectDTOOut.class))).thenReturn(createdProject);
		
		Project resultProject = service.create(newProjectIn);
		
		verify(port).create(newProjectOut);
		assertThat(resultProject).isSameAs(createdProject);
	}


}
