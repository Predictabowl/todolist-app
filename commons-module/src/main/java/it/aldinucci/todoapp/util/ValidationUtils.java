package it.aldinucci.todoapp.util;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidationUtils {

	private static final Pattern UUID_REGEX =
			Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
	
	private ValidationUtils() {
	}
	
	public static boolean isValidUUID(String uuid) {
		if (Objects.isNull(uuid))
			return false;
		
		return UUID_REGEX.matcher(uuid).matches();
	}
}
