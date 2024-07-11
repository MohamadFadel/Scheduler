package com.wavemark.scheduler.cron.util;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CronExpressionDescriptor {

    public static List<Integer> reverseMinuteCronExpression(String minuteExpression) {
        String delimiter = ",";
        List<Integer> minutesList = new ArrayList<>();

        String[] minutes = minuteExpression.split(delimiter);

        for (String minute : minutes) {
            if (!minute.equals("*") && !minute.contains("/") && !minute.contains("-"))
                minutesList.add(Integer.valueOf(minute));
        }

        return minutesList;
    }

    public static List<Integer> reverseHourCronExpression(String expression) {
        return Arrays.stream(expression.split(","))
                .filter(val -> !val.equals("*"))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    public static List<DayOfMonth> reverseDayOfMonthCronExpression(String expression) {
        return Arrays.stream(expression.split(","))
                .filter(val -> !val.equals("*") && !val.equals("?") && !val.equals("1/1"))
                .map(DayOfMonth::get)
                .collect(Collectors.toList());
    }

    public static List<Month> reverseMonthCronExpression(String expression) {
        return Arrays.stream(expression.split(","))
                .filter(val -> !val.equals("*") && !val.equals("?"))
                .map(Month::get)
                .collect(Collectors.toList());
    }

    public static List<DayOfWeek> reverseDayOfWeekCronExpression(String expression) {
        return Arrays.stream(expression.split(","))
                .filter(val -> !val.equals("*") && !val.equals("?"))
                .map(DayOfWeek::get)
                .collect(Collectors.toList());
    }

}
