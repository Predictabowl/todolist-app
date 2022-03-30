package it.aldinucci.todoapp.adapter.out.persistence;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(UpdateProjectJPA.class)
class UpdateProjectJPATest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UpdateProjectJPA sut;
	
	@MockBean
	private ValidateId<Long> validator;
	
	@MockBean
	private AppGenericMapper<ProjectJPA, Project> mapper;
	
	@Test
	void test_update_whenProjectIsMissing() {
		when(validator.isValid(anyString())).thenReturn(true);
		when(validator.getId()).thenReturn(2L);
		
		Optional<Project> optional = sut.update(new Project("2", "name"));
		
		assertThat(optional).isEmpty();
		assertThat(entityManager.getEntityManager().createQuery("from ProjectJPA", ProjectJPA.class).getResultList())
			.isEmpty();
		
		verifyNoInteractions(mapper);
		verify(validator).isValid("2");
	}
	
	@Test
	void test_update_whenInvalidId() {
		when(validator.isValid(anyString())).thenReturn(false);
		
		Optional<Project> optional = sut.update(new Project("test", "name"));
		
		assertThat(optional).isEmpty();
		assertThat(entityManager.getEntityManager().createQuery("from ProjectJPA", ProjectJPA.class).getResultList())
			.isEmpty();
		
		verifyNoInteractions(mapper);
		verify(validator).isValid("test");
		verify(validator, times(0)).getId();
	}
	
	@Test
	void test_update_success() {
		when(validator.isValid(anyString())).thenReturn(true);
		UserJPA  user = entityManager.persist(new UserJPA("email", "name", "pass"));
		ProjectJPA projectJpa = entityManager.persist(new ProjectJPA("test", user));
		user.getProjects().add(projectJpa);
		entityManager.flush();
		entityManager.detach(projectJpa);
		Project testProject = new Project("11", "different");
		when(mapper.map(isA(ProjectJPA.class))).thenReturn(testProject);
		when(validator.getId()).thenReturn(projectJpa.getId());
		
		Project newProject = new Project(projectJpa.getId().toString(), "new name");
		
		Optional<Project> optional = sut.update(newProject);
		
		assertThat(optional).containsSame(testProject);
		ProjectJPA loadedProjectJpa = entityManager.find(ProjectJPA.class, projectJpa.getId());
		assertThat(loadedProjectJpa).usingRecursiveComparison().isNotEqualTo(projectJpa);
		assertThat(loadedProjectJpa.getName()).isEqualTo("new name");
		projectJpa.setName("new name");
		assertThat(loadedProjectJpa).usingRecursiveComparison().isEqualTo(projectJpa);
		verify(mapper).map(projectJpa);
		verify(validator).isValid(projectJpa.getId().toString());
	}

}
