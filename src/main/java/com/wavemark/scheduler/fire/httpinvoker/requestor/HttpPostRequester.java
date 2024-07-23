package com.wavemark.scheduler.fire.httpinvoker.requestor;

import java.io.IOException;
import java.util.Objects;

import com.wavemark.scheduler.fire.httpinvoker.client.HttpClient;
import com.wavemark.scheduler.fire.httpinvoker.property.HttpProperty;
import com.wavemark.scheduler.fire.httpinvoker.response.ResponseHandler;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpPostRequester {

    private final ResponseHandler responseHandler;

    public void postTask(HttpProperty httpProperty) throws SchedulerException, EntryNotFoundException, IOException {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(httpProperty.getBodyParam(), mediaType);

        Request request = new Request.Builder()
                .url(httpProperty.getUrl())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        long time = System.currentTimeMillis();
        try (Response response = HttpClient.getInstance().getClient().newCall(request).execute()) {
            responseHandler.handle(httpProperty, response, System.currentTimeMillis() - time);
        }
        catch (SchedulerException | EntryNotFoundException e) {
            if (!e.getMessage().startsWith("[FIRED]"))
                responseHandler.handleError(httpProperty, e, System.currentTimeMillis() - time);
            throw new JobExecutionException(e);
        }
    }

    public String postWardenAuthToken(HttpProperty httpProperty) throws IOException {

        RequestBody body = RequestBody.create("", null);
        String credential = Credentials.basic("Scheduler", "hlYporLpsI17SZUSRpsYkvbQdAGhJcJJ");

        Request request = new Request.Builder()
                .url(httpProperty.getUrl())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", credential)
                .build();

        Response response = HttpClient.getInstance().getClient().newCall(request).execute();
        String responseBody = Objects.requireNonNull(response.body()).string();

        ObjectMapper mapper = new ObjectMapper();
        AuthToken authToken = mapper.readValue(responseBody, AuthToken.class);

        return authToken.getAccess_token();
    }

}
