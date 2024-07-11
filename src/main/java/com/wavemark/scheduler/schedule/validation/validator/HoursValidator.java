package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.schedule.validation.annotation.HoursValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class HoursValidator implements ConstraintValidator<HoursValidation, List<Integer>> {

    @Override
    public boolean isValid(List<Integer> hours, ConstraintValidatorContext context) {
        for (Integer hour : hours) {
            if (hour < 0 || hour > 23)
                return false;
        }

        return true;
    }

}
