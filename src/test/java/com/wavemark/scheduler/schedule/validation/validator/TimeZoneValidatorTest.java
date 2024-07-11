package com.wavemark.scheduler.schedule.validation.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimeZoneValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    TimeZoneValidator timeZoneValidator = new TimeZoneValidator();

    @Test
    void testIsValidReturnsTrue_timeZoneIsGMT() {
        boolean result = timeZoneValidator.isValid("GMT", constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsTrue_timeZoneIsNotGMT() {
        boolean result = timeZoneValidator.isValid("GMT+1", constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        boolean result = timeZoneValidator.isValid("", constraintValidatorContext);

        assertFalse(result);
    }

}