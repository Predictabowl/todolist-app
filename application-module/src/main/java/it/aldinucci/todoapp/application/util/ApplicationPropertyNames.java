package it.aldinucci.todoapp.application.util;

public enum ApplicationPropertyNames {

	VERIFICATION_TOKEN_DURATION("verification.token.duration"),
	VERIFICATION_TOKEN_LENGTH("verification.token.length");
	
	private String property;
	
	private ApplicationPropertyNames(String property) {
		this.property = property;
	}

	public String get() {
		return property;
	}
	
}
