package com.wavemark.scheduler.schedule.dto.response.autoorder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;

import org.junit.jupiter.api.Test;

class AutoOrderConfigurationDiffTest {

	@Test
	void testGetDiffWorksCorrectly() {

		AutoOrderConfiguration previousAutoOrderConfiguration = AutoOrderConfiguration.builder()
				.includeMissing(true).includeScannedToCart(true).orderReplacements30Days(false)
				.build();
		AutoOrderConfiguration newAutoOrderConfiguration = AutoOrderConfiguration.builder()
				.includeMissing(false).includeScannedToCart(false).orderReplacements30Days(true)
				.build();

		List<TaskUpdateLogResponse> taskUpdateLogResponseList =
				AutoOrderConfigurationDiff.getDiff(previousAutoOrderConfiguration, newAutoOrderConfiguration);

		assertNotNull(taskUpdateLogResponseList);
		assertEquals(3, taskUpdateLogResponseList.size());
	}
}