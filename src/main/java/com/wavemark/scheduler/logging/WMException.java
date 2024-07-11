package com.wavemark.scheduler.logging;

import lombok.Getter;

@Getter
public class WMException extends Exception {

    private int errorCode;

    public WMException() {
        super();
    }

    public WMException(String message) {
        super(message);
    }

    public WMException(String message, Throwable cause) {
        super(message, cause);
    }

    public WMException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}