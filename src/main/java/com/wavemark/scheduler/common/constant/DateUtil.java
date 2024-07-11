package com.wavemark.scheduler.common.constant;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateUtil {

    private static final DateTimeFormatter ZONED_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a zzz");

    public static String convertToZonedDateTime(Instant instant) {
        if (instant == null)
            return null;

        return instant
                .atZone(SecurityUtilsV2.getTimezone())
                .format(ZONED_DATE_TIME_FORMATTER);
    }

    public static String convertToZonedDateTime(Instant instant, TimeZone timeZone) {
        if (instant == null)
            return null;

        return instant
                .atZone(timeZone.toZoneId())
                .format(ZONED_DATE_TIME_FORMATTER);
    }

}
