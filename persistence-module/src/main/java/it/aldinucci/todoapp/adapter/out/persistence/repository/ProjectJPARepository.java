package it.aldinucci.todoapp.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;


public interface ProjectJPARepository extends JpaRepository<ProjectJPA, Long>{

}
