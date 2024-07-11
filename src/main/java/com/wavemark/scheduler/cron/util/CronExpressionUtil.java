package com.wavemark.scheduler.cron.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;

public abstract class CronExpressionUtil {

    public static Frequency determineFrequency(List<Integer> hoursList, List<DayOfMonth> daysOfMonthList,
                                               List<DayOfWeek> daysOfWeekList) {
        Frequency frequency;

        if (hoursList.size() > 1) {
            frequency = Frequency.HOURLY;
        } else if (!daysOfMonthList.isEmpty()) {
            frequency = Frequency.MONTHLY;
        } else if (!daysOfWeekList.isEmpty()) {
            frequency = Frequency.WEEKLY;
        } else if (hoursList.isEmpty()) {
            frequency = Frequency.QUARTER_HOUR;
        } else {
            frequency = Frequency.DAILY;
        }

        return frequency;
    }

    public static int[] getDaysDifferenceAndHourBetweenTimezones(int timezoneHoursDifference, Integer time)
            throws ParseException {
        int[] result = new int[2];
        String dateStringFormat = 5 + " " + time;
        SimpleDateFormat parser = new SimpleDateFormat("d HH");
        Date date;

        date = parser.parse(dateStringFormat);
        long convertedDateInMillis = date.getTime() + timezoneHoursDifference;

        Date dateConverted = new Date(convertedDateInMillis);
        String dayHourString = parser.format(dateConverted);

        String[] convertedArray = dayHourString.split(" ");

        int convertedDay = Integer.parseInt(convertedArray[0]);
        int convertedTime = Integer.parseInt(convertedArray[1].split(":")[0]);

        result[0] = (convertedDay - 5);
        result[1] = convertedTime;

        return result;
    }

    public static int getTimezoneHoursDifference(TimeZone fromTimezone, TimeZone toTimezone) {
        long currentTime = System.currentTimeMillis();

        int defaultTimezoneOffset = fromTimezone.getOffset(currentTime);
        int chosenTimezoneOffset = toTimezone.getOffset(currentTime);

        return chosenTimezoneOffset - defaultTimezoneOffset;
    }

    public static String[] getConvertedDayHourArray(CronDescription cronDescription,
                                                    int timezoneHoursDifference) throws CronExpressionException {

        String dayHourString = "";
        for (DayOfWeek dayOfWeek : cronDescription.getDaysOfWeek()) {
            String daysOfWeekStr = dayOfWeek.toString();
            int hour = cronDescription.getHours().get(0);
            String dateStringFormat = daysOfWeekStr + " " + hour;

            dayHourString += getDayHourString("EEEE HH", dateStringFormat, timezoneHoursDifference) + " ";
        }
        return dayHourString.split(" ");
    }

    public static List<Integer> getConvertedHours(CronDescription cronDescription,
                                                  int timezoneHoursDifference) throws CronExpressionException {
        List<Integer> convertedHoursList = new ArrayList<>();

        for (Integer hour : cronDescription.getHours()) {
            String dateStringFormat = hour + "";

            String dayHourString = getDayHourString("HH", dateStringFormat, timezoneHoursDifference);

            int convertedHour = Integer.parseInt(dayHourString.split(":")[0]);

            convertedHoursList.add(convertedHour);
        }
        return convertedHoursList;
    }

    public static String getDayHourString(String parserFormat, String dateStringFormat, int timezoneHoursDifference)
            throws CronExpressionException {
        SimpleDateFormat parser = new SimpleDateFormat(parserFormat);
        Date date;

        try {
            date = parser.parse(dateStringFormat);
        } catch (ParseException e) {
            throw new CronExpressionException("Cron expression parse exception", e);
        }

        long convertedDateInMillis = date.getTime() + timezoneHoursDifference;

        Date dateConverted = new Date(convertedDateInMillis);

        return parser.format(dateConverted);
    }

}
