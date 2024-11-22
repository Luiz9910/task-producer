package com.task.validation.yearValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearValid.class)
public @interface YearValidator {
    String message() default "Data limite inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
