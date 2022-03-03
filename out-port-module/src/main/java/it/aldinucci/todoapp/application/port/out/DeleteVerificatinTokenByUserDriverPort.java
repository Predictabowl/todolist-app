package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

public interface DeleteVerificatinTokenByUserDriverPort {

	public void delete(String email) throws AppUserNotFoundException;
}
