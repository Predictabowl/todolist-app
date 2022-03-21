package it.aldinucci.todoapp.application.port.out;

public interface UserExistsDriverPort {

	public boolean exists(String email);
}
