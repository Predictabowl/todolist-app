package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Date;

public record VerificationTokenData(Date expiryDate, String userEmail) {
}
