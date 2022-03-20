package it.aldinucci.todoapp.application.port.out.dto;

public record UserData(
	String username,
	String email,
	String password,
	boolean enabled) {
}