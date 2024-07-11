package com.wavemark.scheduler.fire.retry;

import java.util.Calendar;
import java.util.Date;

import com.wavemark.scheduler.fire.configuration.RetryConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryValidator {

    public static boolean isValid(Integer retryCount, Date nextFireTime) {

        return isBelowMaxReties(retryCount) && isNextRetryBeforeNextFireTime(nextFireTime);
    }

    private static boolean isBelowMaxReties(int retryCount) {

        log.info("Retry count " + retryCount);
        return retryCount <= RetryConfiguration.retryPolicy().getMaxRetries();
    }

    private static boolean isNextRetryBeforeNextFireTime(Date nextFireTime) {

        log.info("Next Fire Time " + nextFireTime);
        return nextFireTime == null || timeOfTheNextRetry().before(nextFireTime);
    }

    private static Date timeOfTheNextRetry() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, RetryConfiguration.retryPolicy().getDelay());
        return calendar.getTime();
    }
}
