package com.wavemark.scheduler.cron.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public enum Frequency {

    HOURLY("hourly"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    QUARTER_HOUR("15min"),
    MINUTES("minutes");

    private static final Map<String, Frequency> lookup = new HashMap<>();

    static {
        for (Frequency freq : EnumSet.allOf(Frequency.class))
            lookup.put(freq.getCronExpression(), freq);
    }

    final String cronExpression;

    Frequency(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getCapitalizedCronExpression() {
        return StringUtils.capitalize(cronExpression);
    }

    public static Frequency get(String cronExpression) {
        return lookup.get(cronExpression);
    }

}
