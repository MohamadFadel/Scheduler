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
class DaysOfWeekValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    DaysOfWeekValidator daysOfWeekValidator = new DaysOfWeekValidator();

    @Test
    void testIsValidReturnsTrue() {
        List<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("1");

        boolean result = daysOfWeekValidator.isValid(daysOfWeek, constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        List<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("");

        boolean result = daysOfWeekValidator.isValid(daysOfWeek, constraintValidatorContext);

        assertFalse(result);
    }

}