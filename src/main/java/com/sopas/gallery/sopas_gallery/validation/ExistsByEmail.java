package com.sopas.gallery.sopas_gallery.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Constraint(validatedBy = ExistsByEmailValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByEmail {
String message() default "Is already in use.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
