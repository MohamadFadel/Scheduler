package com.wavemark.scheduler.schedule.validation.annotation;


import com.wavemark.scheduler.schedule.validation.validator.EmailListValidator;

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
@Constraint(validatedBy = EmailListValidator.class)
public @interface EmailListValidation {

    String message() default "Invalid email list";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
