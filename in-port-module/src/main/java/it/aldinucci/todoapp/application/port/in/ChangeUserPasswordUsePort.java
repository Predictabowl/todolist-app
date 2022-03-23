package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.in.model.AppPassword;

public interface ChangeUserPasswordUsePort {

	public boolean change(StringTokenDTOIn resetToken, AppPassword newPassword);
}
