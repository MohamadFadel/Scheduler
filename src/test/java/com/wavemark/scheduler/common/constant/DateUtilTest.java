package com.wavemark.scheduler.common.constant;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

class DateUtilTest {

	@Test
	void convertToZonedDateTime() {

		String date = DateUtil.convertToZonedDateTime(Instant.now(), TimeZone.getDefault());

		assertNotNull(date);
	}

	@Test
	void testConvertToZonedDateTimeWithNullInstant() {
		assertDoesNotThrow(() -> DateUtil.convertToZonedDateTime(null, TimeZone.getDefault()));
	}
}