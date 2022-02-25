package it.aldinucci.todoapp.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;

public interface TaskJPARepository extends JpaRepository<TaskJPA, Long>{

	public List<TaskJPA> findByProjectIdAndCompletedFalse(long projectId);
}
