package com.wavemark.scheduler.schedule.validation.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DaysOfMonthValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    DaysOfMonthValidator daysOfMonthValidator = new DaysOfMonthValidator();

    @Test
    void testIsValidReturnsTrue() {
        List<String> daysOfMonth = new ArrayList<>();
        daysOfMonth.add("1");

        boolean result = daysOfMonthValidator.isValid(daysOfMonth, constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        List<String> daysOfMonth = new ArrayList<>();
        daysOfMonth.add("");

        boolean result = daysOfMonthValidator.isValid(daysOfMonth, constraintValidatorContext);

        assertFalse(result);
    }

}