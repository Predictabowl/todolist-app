package it.aldinucci.todoapp.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;

public interface ResetPasswordTokenJPARepository extends JpaRepository<ResetPasswordTokenJPA, Long>{

	public Optional<ResetPasswordTokenJPA> findByToken(String token);
	public Optional<ResetPasswordTokenJPA> findByUserEmail(String email);
}
