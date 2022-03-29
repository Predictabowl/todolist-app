package it.aldinucci.todoapp.application.port.in.validator.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;


@Constraint(validatedBy = {})
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Pattern(regexp = "[1-9][0-9]*")
@Documented
public @interface EntityId {
	
	String message() default"{invalid.id}";
	Class<?>[] groups() default{};
	Class<? extends Payload>[] payload() default{};
}
