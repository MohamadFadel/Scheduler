package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.schedule.validation.annotation.DaysOfMonthValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DaysOfMonthValidator implements ConstraintValidator<DaysOfMonthValidation, List<String>> {

    @Override
    public boolean isValid(List<String> daysOfMonth, ConstraintValidatorContext context) {
        for (String dayOfMonth : daysOfMonth) {
            DayOfMonth day = DayOfMonth.get(dayOfMonth);

            if (day == null && !dayOfMonth.equals("0"))
                return false;
        }

        return true;
    }

}
