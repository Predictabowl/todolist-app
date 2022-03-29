package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Date;

public record ResetPasswordTokenData(Date expiryDate, String userEmail) {

}
