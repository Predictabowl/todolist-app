package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Date;

public record VerificationTokenData(String token, Date expiryDate, String userEmail) {
}
