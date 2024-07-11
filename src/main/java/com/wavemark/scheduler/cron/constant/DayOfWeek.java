package com.wavemark.scheduler.cron.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DayOfWeek
{
	Sunday("1"),
	Monday("2"),
	Tuesday("3"),
	Wednesday("4"),
	Thursday("5"),
	Friday("6"),
	Saturday("7"),
	Weekdays("2,3,4,5,6"),
	Everyday("*"),
	LastDayOfWeek("L");

	private static final Map<String, DayOfWeek> lookup = new HashMap<String, DayOfWeek>();

	static
	{
		for (DayOfWeek day: EnumSet.allOf(DayOfWeek.class))
			lookup.put(day.getCronExpression(), day);
	}

	final String cronExpression;

	private DayOfWeek(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public static DayOfWeek get(String cronExpression)
	{
		return lookup.get(cronExpression);
	}
}