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
class MonthsValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    MonthsValidator monthsValidator = new MonthsValidator();

    @Test
    void testIsValidReturnsTrue() {
        List<Integer> months = new ArrayList<>();
        months.add(0);

        boolean result = monthsValidator.isValid(months, constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        List<Integer> months = new ArrayList<>();
        months.add(13);

        boolean result = monthsValidator.isValid(months, constraintValidatorContext);

        assertFalse(result);
    }

}