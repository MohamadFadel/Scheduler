package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.schedule.validation.annotation.DaysOfWeekValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DaysOfWeekValidator implements ConstraintValidator<DaysOfWeekValidation, List<String>> {

    @Override
    public boolean isValid(List<String> daysOfWeek, ConstraintValidatorContext context) {
        for (String dayOfWeek : daysOfWeek) {
            DayOfWeek day = DayOfWeek.get(dayOfWeek);

            if (day == null && !dayOfWeek.equals("0"))
                return false;
        }

        return true;
    }

}
