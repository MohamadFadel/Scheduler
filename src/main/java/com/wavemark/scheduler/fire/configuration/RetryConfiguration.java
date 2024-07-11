package com.wavemark.scheduler.fire.configuration;


public class RetryConfiguration {

    public static final int MAX_RETRIES = 4;
    public static final Integer RETRY_INTERVAL_MINUTES = 15;

    public static RetryPolicy retryPolicy(){

        return RetryPolicy.builder()
                .withMaxRetries(MAX_RETRIES)
                .withDelay(RETRY_INTERVAL_MINUTES)
                .build();
    }
}
