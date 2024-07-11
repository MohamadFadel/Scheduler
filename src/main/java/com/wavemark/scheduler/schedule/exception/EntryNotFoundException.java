package com.wavemark.scheduler.schedule.exception;

import com.wavemark.scheduler.logging.WMException;

public class EntryNotFoundException extends WMException {

    public EntryNotFoundException() {
        super();
    }

    public EntryNotFoundException(String message) {
        super(message);
    }

    public EntryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntryNotFoundException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

}
