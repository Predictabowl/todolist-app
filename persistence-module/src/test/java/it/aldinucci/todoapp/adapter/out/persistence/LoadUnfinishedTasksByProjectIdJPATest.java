package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class LoadUnfinishedTasksByProjectIdJPATest {

	@Mock
	private TaskJPARepository repository;
	
	@Mock
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@Mock
	private ValidateId<Long> validator;
	
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
		Task task1 = new Task("2L", "task1", "descr1", false);
		Task task2 = new Task("7L", "task2", "descr2", false);
		when(repository.findByProjectIdAndCompletedFalse(anyLong()))
			.thenReturn(tasksJpa);
		when(mapper.map(isA(TaskJPA.class)))
			.thenReturn(task1)
			.thenReturn(task2);
		when(validator.getValidId(anyString()))
			.thenReturn(Optional.of(2L));
		
		List<Task> tasks = adapter.load("2");
		
		verify(repository).findByProjectIdAndCompletedFalse(2);
		verify(validator).getValidId("2");
		assertThat(tasks).containsExactly(task1,task2);
	}
	
	@Test
	void test_loadWhenInvalidId() {
		when(validator.getValidId(anyString())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> adapter.load("test"))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Could not find project with id: test");
		
		verify(validator).getValidId("test");
	}

}
