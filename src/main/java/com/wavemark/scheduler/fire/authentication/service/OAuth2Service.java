package com.wavemark.scheduler.fire.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemark.scheduler.fire.authentication.dto.OAuth2Token;
import com.wavemark.scheduler.fire.http.client.HttpClient;
import com.wavemark.scheduler.fire.http.property.HttpProperty;
import com.wavemark.scheduler.fire.http.response.HttpResponse;
import com.wavemark.scheduler.fire.http.response.ResponseFactory;
import com.wavemark.scheduler.fire.http.response.ResponseHandler;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.wavemark.scheduler.common.constant.DataMapProperty.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final ResponseHandler responseHandler;

    @Value("${application.host}")
    private String host;

    public String getWardenAuthToken(JobDataMap jobDataMap) throws SchedulerException, EntryNotFoundException, JsonProcessingException {
        HttpProperty authHttpProperty = fetchAuthTokenRequestProperty(jobDataMap);
        HttpResponse authTokenResponse = postWardenResponse(authHttpProperty);

        ObjectMapper mapper = new ObjectMapper();
        OAuth2Token token = mapper.readValue(authTokenResponse.getMessage(), OAuth2Token.class);

        if (Integer.parseInt(token.getExpires_in()) <= 300) {
            authHttpProperty = fetchRefreshTokenRequestProperty(jobDataMap, token.getRefresh_token());
            authTokenResponse = postWardenResponse(authHttpProperty);

            token = mapper.readValue(authTokenResponse.getMessage(), OAuth2Token.class);
        }

        return token.getAccess_token();
    }

    private HttpProperty fetchAuthTokenRequestProperty(JobDataMap jobDataMap) {
        String url = host + "/warden/oauth/token?username=" + jobDataMap.getString(ENDPOINT_ID) + "&grant_type=password";

        return HttpProperty.builder()
                .taskName(jobDataMap.getString(NAME))
                .endpointName(jobDataMap.getString(ENDPOINT_NAME))
                .url(url)
                .build();
    }

    private HttpProperty fetchRefreshTokenRequestProperty(JobDataMap jobDataMap, String refreshToken) {
        String url = host + "/warden/oauth/token?refresh_token=" + refreshToken + "&grant_type=refresh_token";

        return HttpProperty.builder()
                .taskName(jobDataMap.getString(NAME))
                .endpointName(jobDataMap.getString(ENDPOINT_NAME))
                .url(url)
                .build();
    }

    private HttpResponse postWardenResponse(HttpProperty httpProperty) throws SchedulerException, EntryNotFoundException {
        RequestBody body = RequestBody.create("", null);
        String credential = Credentials.basic("Scheduler", "hlYporLpsI17SZUSRpsYkvbQdAGhJcJJ");

        Request request = new Request.Builder()
                .url(httpProperty.getUrl())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", credential)
                .build();

        long time = System.currentTimeMillis();

        try (Response response = HttpClient.getInstance().getClient().newCall(request).execute()) {
            HttpResponse httpResponse = ResponseFactory.initializeHttpResponse(response);

            return handleResponse(httpResponse);
        } catch (IOException e) {
            log.info("throwing Warden Exception due to failure");

            responseHandler.handleError(httpProperty, e, System.currentTimeMillis() - time);

            throw new JobExecutionException(e);
        }
    }

    private HttpResponse handleResponse(HttpResponse httpResponse) throws IOException {
        if (httpResponse.isSuccess())
            return httpResponse;

        throw new IOException(httpResponse.getMessage());
    }

}
