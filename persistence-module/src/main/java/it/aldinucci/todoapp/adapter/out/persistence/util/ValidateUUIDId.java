package it.aldinucci.todoapp.adapter.out.persistence.util;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.util.ValidationUtils;

@Component
public class ValidateUUIDId implements ValidateId<UUID> {

	@Override
	public Optional<UUID> isValid(String id) {
		if(ValidationUtils.isValidUUID(id)) {
			return Optional.of(UUID.fromString(id));
		}
		return Optional.empty();
	}
}
