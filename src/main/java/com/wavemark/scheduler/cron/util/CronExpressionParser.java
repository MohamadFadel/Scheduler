package com.wavemark.scheduler.cron.util;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.constant.Month;
import com.wavemark.scheduler.cron.exception.CronExpressionException;

import java.util.List;

public class CronExpressionParser {

    public static String buildMinutesCronExpression(List<Integer> minutes, Frequency frequency)
            throws CronExpressionException {
        StringBuilder cronMinutes = new StringBuilder("0");

        if (frequency == Frequency.MINUTES && minutes.size() == 1) {
            cronMinutes.append("/");
            cronMinutes.append(minutes.get(0));

            return cronMinutes.toString();
        }

        if (minutes != null && !minutes.isEmpty() && frequency != Frequency.QUARTER_HOUR) {
            cronMinutes = null;
            for (Integer chosenMinute : minutes) {
                if (chosenMinute > 59 || chosenMinute < 0)
                    throw new CronExpressionException("Invalid minutes supplied to the CronExpressionBuilder");

                if (cronMinutes != null)
                    cronMinutes.append(",");
                else
                    cronMinutes = new StringBuilder();

                cronMinutes.append(chosenMinute);
            }
        }

        if (frequency == Frequency.QUARTER_HOUR)
            cronMinutes = new StringBuilder("0/15");

        return cronMinutes.toString();
    }

    public static String buildHoursCronExpression(List<Integer> hours, Frequency frequency)
            throws CronExpressionException {
        StringBuilder cronHours = null;

        if (hours == null || hours.isEmpty() || frequency == Frequency.QUARTER_HOUR) {
            cronHours = new StringBuilder("*");
        } else {
            for (Integer chosenHour : hours) {
                if (chosenHour > 23 || chosenHour < 0)
                    throw new CronExpressionException("Invalid hour supplied to the CronExpressionBuilder");

                if (cronHours != null)
                    cronHours.append(",");
                else
                    cronHours = new StringBuilder();

                cronHours.append(chosenHour);
            }
        }

        return cronHours.toString();
    }

    public static String buildDaysOfMonthCronExpression(List<DayOfMonth> daysOfMonth, Frequency frequency) {
        StringBuilder cronDaysOfMonth = null;

        if (frequency == Frequency.QUARTER_HOUR) {
            cronDaysOfMonth = new StringBuilder("1/1");

        } else if (daysOfMonth == null || daysOfMonth.isEmpty()) {
            cronDaysOfMonth = new StringBuilder("?");
        } else {
            for (DayOfMonth dayOfMonth : daysOfMonth) {
                if (cronDaysOfMonth != null)
                    cronDaysOfMonth.append(",");
                else
                    cronDaysOfMonth = new StringBuilder();

                cronDaysOfMonth.append(dayOfMonth.getCronExpression());
            }
        }

        return cronDaysOfMonth.toString();
    }

    public static String buildMonthsCronExpression(List<Month> months, Frequency frequency) {
        StringBuilder cronMonths = null;

        if (months == null || months.isEmpty() || frequency == Frequency.QUARTER_HOUR) {
            cronMonths = new StringBuilder("*");
        } else {
            for (Month month : months) {
                if (cronMonths != null)
                    cronMonths.append(",");
                else
                    cronMonths = new StringBuilder();

                cronMonths.append(month.getCronExpression());
            }
        }

        return cronMonths.toString();
    }

    public static String buildDaysOfWeekCronExpression(List<DayOfWeek> daysOfWeek, List<DayOfMonth> daysOfMonth,
                                                       Frequency frequency) {
        StringBuilder cronDayOfWeek = null;

        if ((daysOfMonth != null && !daysOfMonth.isEmpty()) || frequency == Frequency.QUARTER_HOUR) {
            cronDayOfWeek = new StringBuilder("?");
        } else if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            cronDayOfWeek = new StringBuilder("*");
        } else {
            for (DayOfWeek day : daysOfWeek) {
                if (cronDayOfWeek != null)
                    cronDayOfWeek.append(",");
                else
                    cronDayOfWeek = new StringBuilder();

                cronDayOfWeek.append(day.getCronExpression());
            }
        }

        return cronDayOfWeek.toString();
    }

}
