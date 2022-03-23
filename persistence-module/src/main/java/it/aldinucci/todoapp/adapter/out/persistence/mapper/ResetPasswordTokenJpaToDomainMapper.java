package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class ResetPasswordTokenJpaToDomainMapper implements AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken>{

	@Override
	public ResetPasswordToken map(ResetPasswordTokenJPA model) {
		return new ResetPasswordToken(model.getToken(), model.getExpiryDate(),model.getUser().getEmail());
	}

}
