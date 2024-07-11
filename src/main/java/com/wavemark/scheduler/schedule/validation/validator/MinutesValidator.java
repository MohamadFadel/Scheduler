package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.schedule.validation.annotation.MinutesValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MinutesValidator implements ConstraintValidator<MinutesValidation, List<Integer>> {

    @Override
    public boolean isValid(List<Integer> minutes, ConstraintValidatorContext context) {
        for (Integer minute : minutes) {
            if (minute < 0 || minute > 59)
                return false;
        }

        return true;
    }

}
