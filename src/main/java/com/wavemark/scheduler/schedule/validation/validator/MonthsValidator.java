package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.schedule.validation.annotation.MonthsValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MonthsValidator implements ConstraintValidator<MonthsValidation, List<Integer>> {

    @Override
    public boolean isValid(List<Integer> months, ConstraintValidatorContext context) {
        for (Integer month : months) {
            if (month < 0 || month > 12)
                return false;
        }

        return true;
    }

}
