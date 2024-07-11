package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.schedule.validation.annotation.TimeZoneValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.TimeZone;

public class TimeZoneValidator implements ConstraintValidator<TimeZoneValidation, String> {

    @Override
    public boolean isValid(String timezone, ConstraintValidatorContext context) {
        return (timezone.equals("GMT") || !TimeZone.getTimeZone(timezone).getID().equals("GMT"));
    }

}
