package com.wavemark.scheduler.schedule.dto.response.autoorder;

import java.util.ArrayList;
import java.util.List;

import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;

public class AutoOrderConfigurationDiff {

	public static List<TaskUpdateLogResponse> getDiff(AutoOrderConfiguration previousValue, AutoOrderConfiguration newValue)
	{
		List<TaskUpdateLogResponse> taskUpdateLogResponseList = new ArrayList<>();

		if (previousValue.isIncludeMissing() != newValue.isIncludeMissing())
			taskUpdateLogResponseList.add(TaskUpdateLogResponse.builder()
					.updatedField("Order replacements for missing items")
					.previousValue(previousValue.isIncludeMissing() ? "Enabled" : "Disabled")
					.newValue(newValue.isIncludeMissing() ? "Enabled" : "Disabled")
					.build());

		if (previousValue.isIncludeScannedToCart() != newValue.isIncludeScannedToCart())
			taskUpdateLogResponseList.add(TaskUpdateLogResponse.builder()
					.updatedField("Order replacements for cart items")
					.previousValue(previousValue.isIncludeScannedToCart() ? "Enabled" : "Disabled")
					.newValue(newValue.isIncludeScannedToCart() ? "Enabled" : "Disabled")
					.build());

		if (previousValue.isOrderReplacements30Days() != newValue.isOrderReplacements30Days())
			taskUpdateLogResponseList.add(TaskUpdateLogResponse.builder()
					.updatedField("Order replacements for items used in the last 30 days")
					.previousValue(previousValue.isOrderReplacements30Days() ? "Enabled" : "Disabled")
					.newValue(newValue.isOrderReplacements30Days() ? "Enabled" : "Disabled")
					.build());

		return taskUpdateLogResponseList;
	}
}
