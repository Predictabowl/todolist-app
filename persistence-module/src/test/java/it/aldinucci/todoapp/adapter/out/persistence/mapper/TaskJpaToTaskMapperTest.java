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
		TaskJPA taskJpa = new TaskJPA(12L, "name", "description", true, null, 17);
		
		Task task = mapper.map(taskJpa);
		
		assertThat(task.getDescription()).matches("description");
		assertThat(task.getId()).isEqualTo(12L);
		assertThat(task.getName()).matches("name");
		assertThat(task.isCompleted()).isTrue();
		assertThat(task.getOrderInProject()).isEqualTo(17);
	}

}
