package com.wavemark.scheduler.schedule.validation.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskTypeValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    TaskTypeValidator taskTypeValidator = new TaskTypeValidator();

    @Test
    void testIsValidReturnsTrue() {
        boolean result = taskTypeValidator.isValid("Auto-Order", constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        boolean result = taskTypeValidator.isValid("", constraintValidatorContext);

        assertFalse(result);
    }

}