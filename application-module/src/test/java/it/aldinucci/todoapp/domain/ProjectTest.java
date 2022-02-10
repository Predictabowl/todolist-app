package it.aldinucci.todoapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProjectTest {

	@Test
	void test_noDuplicateTasks() {
		Project project = new Project(1L, "test project");
		Task task1 = new Task(3L, "first task", "test description");
		Task task2 = new Task(3L, "second task","test description");
		
		project.getTasks().add(task1);
		project.getTasks().add(task2);
		
		assertThat(project.getTasks()).hasSize(1);
		assertThat(project.getTasks().get(0)).isSameAs(task1);
	}

}
