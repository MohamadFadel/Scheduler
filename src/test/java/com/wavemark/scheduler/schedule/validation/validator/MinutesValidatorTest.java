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
class MinutesValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    MinutesValidator minutesValidator = new MinutesValidator();

    @Test
    void testIsValidReturnsTrue() {
        List<Integer> minutes = new ArrayList<>();
        minutes.add(0);

        boolean result = minutesValidator.isValid(minutes, constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        List<Integer> minutes = new ArrayList<>();
        minutes.add(60);

        boolean result = minutesValidator.isValid(minutes, constraintValidatorContext);

        assertFalse(result);
    }

}