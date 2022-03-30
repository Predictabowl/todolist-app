package it.aldinucci.todoapp.adapter.out.persistence.util;

import org.springframework.stereotype.Component;

@Component
public class ValidateLongId implements ValidateId<Long> {

	private Long id;

	public ValidateLongId() {
		this.id = null;
	}
	
	
	@Override
	public boolean isValid(String id) {
		try {
			this.id = Long.parseLong(id);
			return true;
		} catch (Exception e) {
			this.id = null;
		}
		return false;
	}

	@Override
	public Long getId() {
		return id;
	}

}
