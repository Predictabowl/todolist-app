package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.domain.User;

public interface CreateUserDriverPort {

	public User create(NewUserData newUser);
}
