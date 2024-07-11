package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.schedule.validation.annotation.FrequencyValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FrequencyValidator implements ConstraintValidator<FrequencyValidation, String> {

    @Override
    public boolean isValid(String frequency, ConstraintValidatorContext context) {
        Frequency freq = Frequency.get(frequency);

        return freq != null;
    }

}
