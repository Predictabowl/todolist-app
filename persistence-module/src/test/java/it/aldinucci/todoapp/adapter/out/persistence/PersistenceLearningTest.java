package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class PersistenceLearningTest {
	
	@Autowired
	TaskJPARepository taskRepository;
	
	@Autowired
	ProjectJPARepository projectRepository;
	
	@Autowired
	TestEntityManager entityManager;

	@Test
	void test_context_loading() {
		
	}
	
	@Test
	void test() {
		ProjectJPA project = new ProjectJPA();
		project.setName("project name");
		entityManager.persistAndFlush(project);
		entityManager.detach(project);
		assertThat(project.getId()).isNotNull();
		
		TaskJPA task = new TaskJPA();
		task.setName("Mario");
		task.setDescription("Qualcosa");
		project.getTasks().add(task);
		task.setProject(project);
		taskRepository.save(task);
		projectRepository.save(project);
		
		
		ProjectJPA project2 = projectRepository.getById(project.getId());
		TaskJPA task2 = taskRepository.getById(task.getId());
		assertThat(task2.getProject()).isEqualTo(project2);
//		entityManager.persist(task);
	}
}
