package it.aldinucci.todoapp.adapter.out.persistence.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class ValidateLongId implements ValidateId<Long> {

	@Override
	public Optional<Long> getValidId(String id) {
		try {
			return  Optional.of(Long.parseLong(id));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
