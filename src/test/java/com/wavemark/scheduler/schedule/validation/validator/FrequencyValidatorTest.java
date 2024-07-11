package com.wavemark.scheduler.schedule.validation.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FrequencyValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    FrequencyValidator frequencyValidator = new FrequencyValidator();

    @Test
    void testIsValidReturnsTrue() {
        boolean result = frequencyValidator.isValid("hourly", constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        boolean result = frequencyValidator.isValid("", constraintValidatorContext);

        assertFalse(result);
    }

}