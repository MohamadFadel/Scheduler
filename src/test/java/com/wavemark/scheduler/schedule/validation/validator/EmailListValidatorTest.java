package com.wavemark.scheduler.schedule.validation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class EmailListValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    EmailListValidator emailListValidator = new EmailListValidator();

    @Test
    void testIsValidReturnsTrue() {
        boolean result = emailListValidator
                .isValid("test1@test.com, test2@test.com", constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsTrueForEmptyEmails() {
        boolean result = emailListValidator
                .isValid("", constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        boolean result = emailListValidator
                .isValid("test1@test.com, test2", constraintValidatorContext);

        assertFalse(result);
    }

}