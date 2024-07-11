package com.wavemark.scheduler.cron.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.constant.Month;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.util.CronExpressionDescriptor;
import com.wavemark.scheduler.cron.util.CronExpressionParser;
import com.wavemark.scheduler.cron.util.CronExpressionUtil;
import com.wavemark.scheduler.schedule.dto.request.TaskFrequencyInput;

import org.springframework.stereotype.Service;

@Service
public class CronExpressionService {

    public String generateCronExpression(TaskFrequencyInput taskFrequencyInput) throws CronExpressionException {
        CronDescription cronDescription = new CronDescription(taskFrequencyInput);

        convertCronListsToTimezone(cronDescription, TimeZone.getTimeZone(taskFrequencyInput.getTimezone()).getID(), true);

        return createCronExpressionStr(cronDescription);
    }

    public CronDescription reverseCronExpression(String cronExpressionStr, String timezone) throws CronExpressionException {
        CronDescription cronDescription = new CronDescription();

        String[] expSplit = cronExpressionStr.split(" ");

        List<Integer> minuteList = CronExpressionDescriptor.reverseMinuteCronExpression(expSplit[1]);
        List<Integer> hourList = CronExpressionDescriptor.reverseHourCronExpression(expSplit[2]);
        List<DayOfMonth> dayOfMonthList = CronExpressionDescriptor.reverseDayOfMonthCronExpression(expSplit[3]);
        List<Month> monthList = CronExpressionDescriptor.reverseMonthCronExpression(expSplit[4]);
        List<DayOfWeek> dayOfWeekList = CronExpressionDescriptor.reverseDayOfWeekCronExpression(expSplit[5]);

        cronDescription.setMinutes(minuteList);
        cronDescription.setHours(hourList);
        cronDescription.setMonths(monthList);
        cronDescription.setDaysOfMonth(dayOfMonthList);
        cronDescription.setDaysOfWeek(dayOfWeekList);

        cronDescription.setFrequency(CronExpressionUtil.determineFrequency(hourList, dayOfMonthList, dayOfWeekList));

        if (timezone != null && !timezone.trim().isEmpty()) {
            convertCronListsToTimezone(cronDescription, timezone, false);
        }

        return cronDescription;
    }

    public String createCronExpressionStr(CronDescription cronDescription) throws CronExpressionException {
        String cronExpressionStr = "";
        String cronDelimiter = " ";

        String cronSeconds = "0";
        String cronYears = "*";

        String cronMinutes = CronExpressionParser.buildMinutesCronExpression(cronDescription.getMinutes(),
                cronDescription.getFrequency());

        String cronHours = CronExpressionParser.buildHoursCronExpression(cronDescription.getHours(),
                cronDescription.getFrequency());

        String cronDaysOfMonth = CronExpressionParser.buildDaysOfMonthCronExpression(cronDescription.getDaysOfMonth(),
                cronDescription.getFrequency());

        String cronMonths = CronExpressionParser.buildMonthsCronExpression(cronDescription.getMonths(),
                cronDescription.getFrequency());

        String cronDaysOfWeek = CronExpressionParser.buildDaysOfWeekCronExpression(cronDescription.getDaysOfWeek(),
                cronDescription.getDaysOfMonth(), cronDescription.getFrequency());

        cronExpressionStr += cronSeconds + cronDelimiter + cronMinutes + cronDelimiter + cronHours + cronDelimiter
                + cronDaysOfMonth + cronDelimiter + cronMonths + cronDelimiter + cronDaysOfWeek + cronDelimiter
                + cronYears;

        return cronExpressionStr;
    }

    private void convertCronListsToTimezone(CronDescription cronDescription, String timezone, boolean convertToDefaultTimezone) throws CronExpressionException {
        TimeZone defaultTimezone = TimeZone.getDefault();
        TimeZone chosenTimezone = (timezone != null && !timezone.trim().isEmpty()) ? TimeZone.getTimeZone(timezone) : defaultTimezone;

        if (!chosenTimezone.getID().equalsIgnoreCase(defaultTimezone.getID())) {
            int timezoneHoursDifference = CronExpressionUtil.getTimezoneHoursDifference(defaultTimezone, chosenTimezone);

            if (convertToDefaultTimezone)
                timezoneHoursDifference = -timezoneHoursDifference;

            if (cronDescription.getFrequency().equals(Frequency.WEEKLY)) {
                String[] dayHourArray = CronExpressionUtil.getConvertedDayHourArray(cronDescription, timezoneHoursDifference);

                List<DayOfWeek> daysOfWeek = new ArrayList<>();
                for (int i=0; i<dayHourArray.length; i=i+2)
                {
                    String convertedDay = dayHourArray[i];
                    daysOfWeek.add(DayOfWeek.valueOf(convertedDay));
                }
                int convertedHour = Integer.parseInt(dayHourArray[1].split(":")[0]);

                cronDescription.setDaysOfWeek(daysOfWeek);
                cronDescription.setHours(Collections.singletonList(convertedHour));
            } else if (cronDescription.getFrequency().equals(Frequency.DAILY) || cronDescription.getFrequency().equals(Frequency.HOURLY)) {
                List<Integer> convertedHoursList = CronExpressionUtil.getConvertedHours(cronDescription, timezoneHoursDifference);

                cronDescription.setHours(convertedHoursList);
            } else if (cronDescription.getFrequency().equals(Frequency.MONTHLY)) {
                ArrayList<Integer> convertedHoursList = new ArrayList<>();
                ArrayList<DayOfMonth> convertedDaysList = new ArrayList<>();
                boolean isLastDayOfMonthBased = false;

                Integer hour = cronDescription.getHours().get(0);
                DayOfMonth dayOfMonth = cronDescription.getDaysOfMonth().get(0);
                String dayOfMonthString = dayOfMonth.toString();

                if (dayOfMonthString.contains("L"))
                    isLastDayOfMonthBased = true;

                int dayOfMonthInt = 0;
                if (!isLastDayOfMonthBased)
                    dayOfMonthInt = Integer.parseInt(cronDescription.getDaysOfMonth().get(0).getCronExpression());

                if (!isLastDayOfMonthBased) {
                    String dateStringFormat = dayOfMonthInt + " " + hour;

                    String dayHourString = CronExpressionUtil.getDayHourString("d HH", dateStringFormat, timezoneHoursDifference);

                    String[] convertedArray = dayHourString.split(" ");

                    int convertedDay = Integer.parseInt(convertedArray[0]);
                    int convertedHour = Integer.parseInt(convertedArray[1].split(":")[0]);

                    convertedHoursList.add(convertedHour);
                    convertedDaysList.add(DayOfMonth.get(String.valueOf(convertedDay)));
                } else {
                    int numberODaysDifference;
                    int convertedHour;

                    try {
                        numberODaysDifference = CronExpressionUtil.getDaysDifferenceAndHourBetweenTimezones(timezoneHoursDifference, hour)[0];
                        convertedHour = CronExpressionUtil.getDaysDifferenceAndHourBetweenTimezones(timezoneHoursDifference, hour)[1];
                    } catch (ParseException e) {
                        throw new CronExpressionException("Cron expression parse exception", e);
                    }

                    convertedHoursList.add(convertedHour);

                    DayOfMonth convertedDay = cronDescription.getDaysOfMonth().get(0);

                    if (dayOfMonth.equals(DayOfMonth.LastDayOfMonth) || dayOfMonth.equals(DayOfMonth.OneDayBeforeTheLastDayOfMonth) ||
                            dayOfMonth.equals(DayOfMonth.TwoDaysBeforeTheLastDayOfMonth)) {
                        convertedDay = DayOfMonth.LastDayOfMonth;

                        convertedDaysList.add(convertedDay);
                    } else {
                        if (numberODaysDifference > 0) {
                            convertedDay = DayOfMonth.get(String.valueOf(numberODaysDifference));

                            convertedDaysList.add(convertedDay);
                        } else if (numberODaysDifference < 0) {
                            if (numberODaysDifference == -1)
                                convertedDay = DayOfMonth.get("L-1");

                            if (numberODaysDifference == -2)
                                convertedDay = DayOfMonth.get("L-2");

                            convertedDaysList.add(convertedDay);
                        }
                    }
                }

                cronDescription.setDaysOfMonth(convertedDaysList);
                cronDescription.setHours(convertedHoursList);
            }
        }
    }

}