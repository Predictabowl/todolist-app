package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class LoadUnfinishedTasksByProjectIdJPATest {

	@Mock
	private TaskJPARepository repository;
	
	@Mock
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@InjectMocks
	private LoadUnfinishedTasksByProjectIdJPA adapter;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_successful() {
		TaskJPA taskJpa1 = new TaskJPA(2L,"task1", "descr1", false, null);
		TaskJPA taskJpa2 = new TaskJPA(7L,"task2", "descr2", false, null);
		List<TaskJPA> tasksJpa = Arrays.asList(taskJpa1,taskJpa2);
		Task task1 = new Task(2L, "task1", "descr1", false);
		Task task2 = new Task(7L, "task2", "descr2", false);
		when(repository.findByProjectIdAndCompletedFalse(anyLong()))
			.thenReturn(tasksJpa);
		when(mapper.map(isA(TaskJPA.class)))
			.thenReturn(task1)
			.thenReturn(task2);
		
		List<Task> tasks = adapter.load(2);
		
		verify(repository).findByProjectIdAndCompletedFalse(2);
		assertThat(tasks).containsExactly(task1,task2);
	}

}
