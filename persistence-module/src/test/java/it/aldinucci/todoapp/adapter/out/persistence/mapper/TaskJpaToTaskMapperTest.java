package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.domain.Task;

class TaskJpaToTaskMapperTest {

	private TaskJpaToTaskMapper mapper;
	
	@BeforeEach
	void setUp() {
		mapper = new TaskJpaToTaskMapper();
	}
	
	@Test
	void test() {
		TaskJPA taskJpa = new TaskJPA("name", "description", true, null);
		
		Task task = mapper.map(taskJpa);
		
		assertThat(task.getDescription()).isEqualTo(taskJpa.getDescription());
		assertThat(task.getId()).isEqualTo(taskJpa.getId());
		assertThat(task.getName()).isEqualTo(taskJpa.getName());
		assertThat(task.isCompleted()).isTrue();
	}

}
