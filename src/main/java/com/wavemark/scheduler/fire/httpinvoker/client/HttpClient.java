package com.wavemark.scheduler.fire.httpinvoker.client;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public class HttpClient {

    private static HttpClient INSTANCE;

    private final OkHttpClient client;

    private HttpClient() {

        client = new OkHttpClient().newBuilder()
                .callTimeout(Duration.ofMinutes(3L))
                .connectTimeout(Duration.ofMinutes(3L))
                .readTimeout(Duration.ofMinutes(3L))
                .writeTimeout(Duration.ofMinutes(3L))
                .build();
    }

    public static HttpClient getInstance() {

        if(INSTANCE == null) {
            log.info("--Creating a new OkHttpClient--");
            INSTANCE = new HttpClient();
        }

        return INSTANCE;
    }

    public OkHttpClient getClient() {

        log.info("--Http client connection count: " + client.connectionPool().connectionCount());
        return client;
    }
}
