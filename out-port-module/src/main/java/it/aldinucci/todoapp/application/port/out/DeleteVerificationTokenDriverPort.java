package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.exceptions.AppVerificationTokenNotFoundException;

public interface DeleteVerificationTokenDriverPort {

	public void delete(String token) throws AppVerificationTokenNotFoundException;
}
