package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;import org.assertj.core.internal.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadAllProjectsDriverPort;
import it.aldinucci.todoapp.domain.Project;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadAllProjectsService.class, ModelMapper.class})
class LoadAllProjectsServiceTest {

	@MockBean
	private LoadAllProjectsDriverPort port;
	
	@Autowired
	private LoadAllProjectsService service;
	
	@Test
	void test_serviceShouldMap_and_delegateToDriverPort() {
		UserIdDTO userId = new UserIdDTO("test@email.it");
		List<Project> projects = Collections.emptyList();
		when(port.load(isA(String.class))).thenReturn(projects);
		
		List<Project> loadedProjects = service.load(userId);
		
		verify(port).load("test@email.it");
		assertThat(loadedProjects).isSameAs(projects);
	}

}
