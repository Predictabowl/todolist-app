package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.IsUserPresentDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class CreateNewProjectServiceTest {

	@Mock
	private CreateProjectDriverPort newProjectport;
	
	@Mock
	private IsUserPresentDriverPort isUser;
	
	@Mock
	private AppGenericMapper<NewProjectDTOIn, NewProjectData> mapper;
	
	@InjectMocks
	private CreateNewProjectService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
//		service = new CreateNewProjectService(newProjectport, isUser, mapper);
	}
	
	@Test
	void test_newProject_whenUserNotPresent_shouldThrow(){
		NewProjectDTOIn newProjectIn = new NewProjectDTOIn("test project","test@email.com");
		when(isUser.isPresent(anyString())).thenReturn(false);
		
		assertThatThrownBy(() -> service.create(newProjectIn))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: test@email.com");
		
		verify(isUser).isPresent("test@email.com");
		verifyNoInteractions(newProjectport);
	}
	
	@Test
	void test_newProjectService_shouldCallPort(){
		when(isUser.isPresent(anyString())).thenReturn(true);
		NewProjectDTOIn newProjectIn = new NewProjectDTOIn("test project","test@email.com");
		NewProjectData newProjectOut = new NewProjectData("test project", "test@email.com");
		Project createdProject = new Project("1L", "first project");
		when(newProjectport.create(isA(NewProjectData.class))).thenReturn(createdProject);
		when(mapper.map(newProjectIn)).thenReturn(newProjectOut);
		
		Project resultProject = service.create(newProjectIn);
		
		verify(isUser).isPresent("test@email.com");
		verify(newProjectport).create(newProjectOut);
		assertThat(resultProject).isSameAs(createdProject);
	}
}
