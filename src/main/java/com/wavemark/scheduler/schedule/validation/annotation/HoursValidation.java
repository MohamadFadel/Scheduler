package com.wavemark.scheduler.schedule.validation.annotation;

import com.wavemark.scheduler.schedule.validation.validator.HoursValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = HoursValidator.class)
public @interface HoursValidation {

    String message() default "Invalid hours";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}