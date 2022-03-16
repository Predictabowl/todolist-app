package it.aldinucci.todoapp.application.port.in.dto;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;

public record NewUserDtoOut(User user, VerificationToken token) {
}