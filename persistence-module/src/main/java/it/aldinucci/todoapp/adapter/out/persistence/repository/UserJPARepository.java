package it.aldinucci.todoapp.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;

public interface UserJPARepository extends JpaRepository<UserJPA, Long>{

	public Optional<UserJPA> findByEmail(String email);
	
}
