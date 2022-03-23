package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteUserByEmailDriverPort;

@Component
public class DeleteUserByEmailJPA implements DeleteUserByEmailDriverPort {

	private UserJPARepository userRepo;
	private VerificationTokenJPARepository verificationTokenRepo;
	private ResetPasswordTokenJPARepository passwordTokenRepo;

	@Autowired
	public DeleteUserByEmailJPA(UserJPARepository userRepo, VerificationTokenJPARepository verificationTokenRepo,
			ResetPasswordTokenJPARepository passwordTokenRepo) {
		super();
		this.userRepo = userRepo;
		this.verificationTokenRepo = verificationTokenRepo;
		this.passwordTokenRepo = passwordTokenRepo;
	}


	@Override
	public void delete(String email) {
		Optional<VerificationTokenJPA> vToken = verificationTokenRepo.findByUserEmail(email);
		if (vToken.isPresent())
			verificationTokenRepo.delete(vToken.get());
		
		Optional<ResetPasswordTokenJPA> rpToken = passwordTokenRepo.findByUserEmail(email);
		if (rpToken.isPresent())
			passwordTokenRepo.delete(rpToken.get());

		Optional<UserJPA> user = userRepo.findByEmail(email);
		if (user.isPresent())
			userRepo.delete(user.get());
	}

}
