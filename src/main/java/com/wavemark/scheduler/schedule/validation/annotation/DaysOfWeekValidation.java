package com.wavemark.scheduler.schedule.validation.annotation;

import com.wavemark.scheduler.schedule.validation.validator.DaysOfWeekValidator;

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
@Constraint(validatedBy = DaysOfWeekValidator.class)
public @interface DaysOfWeekValidation {

    String message() default "Invalid days of week";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}