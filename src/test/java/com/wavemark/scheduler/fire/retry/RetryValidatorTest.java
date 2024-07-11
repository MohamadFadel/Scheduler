package com.wavemark.scheduler.fire.retry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

class RetryValidatorTest {


	@Test
	void testIsValid_returnsTrue() {

		boolean isValid = RetryValidator.isValid(0, timeOfNextFireTime());
		assertTrue(isValid);
	}

	@Test
	void testIsValid_returnsFalse() {

		boolean isValid = RetryValidator.isValid(5, timeOfNextFireTime());
		assertFalse(isValid);
	}

	private static Date timeOfNextFireTime() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 200);
		return calendar.getTime();
	}
}