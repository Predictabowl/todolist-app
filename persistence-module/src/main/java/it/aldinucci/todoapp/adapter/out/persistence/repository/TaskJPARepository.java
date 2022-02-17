package it.aldinucci.todoapp.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;

public interface TaskJPARepository extends JpaRepository<TaskJPA, Long>{

}
