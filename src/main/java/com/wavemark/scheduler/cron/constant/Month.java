package com.wavemark.scheduler.cron.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Month
{
	January("1"),
	February("2"),
	March("3"),
	April("4"),
	May("5"),
	June("6"),
	July("7"),
	August("8"),
	September("9"),
	October("10"),
	November("11"),
	December("12");

	private static final Map<String, Month> lookup = new HashMap<String, Month>();

	static
	{
		for (Month month: EnumSet.allOf(Month.class))
			lookup.put(month.getCronExpression(), month);
	}

	final String cronExpression;

	private Month(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public static Month get(String cronExpression)
	{
		return lookup.get(cronExpression);
	}
}