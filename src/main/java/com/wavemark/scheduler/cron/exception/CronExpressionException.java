package com.wavemark.scheduler.cron.exception;

import com.wavemark.scheduler.logging.WMException;

public class CronExpressionException extends WMException {

    public CronExpressionException(String message) {
        super(message);
    }

    public CronExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CronExpressionException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

}
