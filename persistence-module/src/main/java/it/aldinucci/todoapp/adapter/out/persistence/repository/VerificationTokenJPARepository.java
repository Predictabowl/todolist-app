package it.aldinucci.todoapp.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;

public interface VerificationTokenJPARepository extends JpaRepository<VerificationTokenJPA, Long>{

	public Optional<VerificationTokenJPA> findByToken(String token);
	public Optional<VerificationTokenJPA> findByUser(UserJPA user);
	public Optional<VerificationTokenJPA> findByUserEmail(String email);
}
