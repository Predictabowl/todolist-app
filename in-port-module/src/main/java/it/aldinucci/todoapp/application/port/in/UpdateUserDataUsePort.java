package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.UserDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;

public interface UpdateUserDataUsePort {

	public Optional<User> update(UserIdDTO userId, UserDataDTOIn userData);
}
