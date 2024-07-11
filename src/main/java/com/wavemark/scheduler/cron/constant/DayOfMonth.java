package com.wavemark.scheduler.cron.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DayOfMonth
{
	//R6.7 Added L-1, L-2 (before last day of month)
	Day_1("1"),
	Day_2("2"),
	Day_3("3"),
	Day_4("4"),
	Day_5("5"),
	Day_6("6"),
	Day_7("7"),
	Day_8("8"),
	Day_9("9"),
	Day_10("10"),
	Day_11("11"),
	Day_12("12"),
	Day_13("13"),
	Day_14("14"),
	Day_15("15"),
	Day_16("16"),
	Day_17("17"),
	Day_18("18"),
	Day_19("19"),
	Day_20("20"),
	Day_21("21"),
	Day_22("22"),
	Day_23("23"),
	Day_24("24"),
	Day_25("25"),
	Day_26("26"),
	Day_27("27"),
	Day_28("28"),
	Day_29("29"),
	Day_30("30"),
	Day_31("31"),
	LastDayOfMonth("L"),
	OneDayBeforeTheLastDayOfMonth("L-1"),
	TwoDaysBeforeTheLastDayOfMonth("L-2");

	private static final Map<String, DayOfMonth> lookup = new HashMap<String, DayOfMonth>();

	static
	{
		for (DayOfMonth day: EnumSet.allOf(DayOfMonth.class))
			lookup.put(day.getCronExpression(), day);
	}

	final String cronExpression;

	private DayOfMonth(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public static DayOfMonth get(String cronExpression)
	{
		return lookup.get(cronExpression);
	}
}