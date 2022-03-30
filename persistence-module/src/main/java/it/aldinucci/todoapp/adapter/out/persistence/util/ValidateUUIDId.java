package it.aldinucci.todoapp.adapter.out.persistence.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.util.ValidationUtils;

@Component
public class ValidateUUIDId implements ValidateId<UUID> {

	private UUID id;
	
	public ValidateUUIDId() {
		this.id = null;
	}
	
	@Override
	public boolean isValid(String id) {
		if(ValidationUtils.isValidUUID(id)) {
			this.id = UUID.fromString(id);
			return true;
		}
		this.id = null;
		return false;
	}

	@Override
	public UUID getId() {
		return id;
	}

}
