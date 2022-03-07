package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;

public interface CreateOrReplaceVerificationTokenUsePort {

	public VerificationToken create(UserIdDTO userId);
}
