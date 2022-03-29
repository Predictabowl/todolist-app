package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.CreateResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.ResetPasswordTokenData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppUserAlreadyHaveResetPasswordTokenException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class CreateResetPasswordTokenJPA implements CreateResetPasswordTokenDriverPort{

	private ResetPasswordTokenJPARepository tokenRepo;
	private UserJPARepository userRepo;
	private AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper;
	

	@Autowired
	public CreateResetPasswordTokenJPA(ResetPasswordTokenJPARepository tokenRepo, UserJPARepository userRepo,
			AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper) {
		super();
		this.tokenRepo = tokenRepo;
		this.userRepo = userRepo;
		this.mapper = mapper;
	}



	@Override
	public ResetPasswordToken create(ResetPasswordTokenData tokenData)
			throws AppUserNotFoundException {
		
		UserJPA user = userRepo.findByEmail(tokenData.userEmail()).orElseThrow(()
				->	new AppUserNotFoundException("User not found with email: "+tokenData.userEmail()));
		
		if(tokenRepo.findByUserEmail(tokenData.userEmail()).isPresent())
			throw new AppUserAlreadyHaveResetPasswordTokenException();
		
		ResetPasswordTokenJPA resetPasswordTokenJPA = new ResetPasswordTokenJPA(user, tokenData.expiryDate());
		resetPasswordTokenJPA = tokenRepo.save(resetPasswordTokenJPA);
		return mapper.map(resetPasswordTokenJPA);
	}

}
