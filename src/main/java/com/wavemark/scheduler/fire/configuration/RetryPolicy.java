package com.wavemark.scheduler.fire.configuration;

import lombok.Getter;

@Getter
public class RetryPolicy {

    private final int delay;
    private final int maxRetries;

    private RetryPolicy(int delay, int maxRetries) {
        this.delay = delay;
        this.maxRetries = maxRetries;
    }

    public static Builder builder() {
         return new Builder();
    }

    public static class Builder {
        private int delay;
        private int maxRetries;

        public Builder withDelay(int minutes){
            this.delay = minutes;
            return this;
        }

        public Builder withMaxRetries(int retries) {
            this.maxRetries = retries;
            return this;
        }

        public RetryPolicy build(){
            return new RetryPolicy(delay, maxRetries);
        }
    }
}

