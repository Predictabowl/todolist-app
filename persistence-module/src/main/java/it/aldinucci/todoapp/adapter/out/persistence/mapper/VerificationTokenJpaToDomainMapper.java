package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class VerificationTokenJpaToDomainMapper implements AppGenericMapper<VerificationTokenJPA, VerificationToken>{

	@Override
	public VerificationToken map(VerificationTokenJPA model) {
		return new VerificationToken(model.getToken().toString(), model.getExpiryDate(),model.getUser().getEmail());
	}

}
