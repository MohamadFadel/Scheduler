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
class HoursValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    HoursValidator hoursValidator = new HoursValidator();

    @Test
    void testIsValidReturnsTrue() {
        List<Integer> hours = new ArrayList<>();
        hours.add(0);

        boolean result = hoursValidator.isValid(hours, constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        List<Integer> hours = new ArrayList<>();
        hours.add(24);

        boolean result = hoursValidator.isValid(hours, constraintValidatorContext);

        assertFalse(result);
    }

}