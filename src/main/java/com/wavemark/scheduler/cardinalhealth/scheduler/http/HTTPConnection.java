package com.wavemark.scheduler.cardinalhealth.scheduler.http;

import com.wavemark.scheduler.fire.http.property.HttpProperty;
import com.wavemark.scheduler.fire.http.response.ResponseHandler;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HTTPConnection {
  private final ResponseHandler responseHandler;

  public void sendHTTPRequest(List<NameValuePair> postParams, HttpProperty httpProperty)
          throws IOException, ClientProtocolException, SchedulerException, EntryNotFoundException {
    Logger logger = LogManager.getLogger(HTTPConnection.class);

    long time = System.currentTimeMillis();

    HttpClient client = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost(httpProperty.getUrl());
    post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
    HttpResponse response = client.execute(post);
    int statusCode = response.getStatusLine().getStatusCode();

    try {
      responseHandler.handleJob(httpProperty, response, System.currentTimeMillis() - time);
    } catch (Exception e) {
      if (!e.getMessage().startsWith("[FIRED]"))
        responseHandler.handleJobError(httpProperty, e, System.currentTimeMillis() - time);
      throw new JobExecutionException(e);
    }

    logger.info("Response return with status code: " + statusCode);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      Header encodingHeader = entity.getContentEncoding();
      Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
      String result = EntityUtils.toString(entity, encoding);
      logger.info("Response return with result: " + result);
    }
//    return statusCode;
  }
}

//  public void sendHTTPRequest(List<NameValuePair> postParams, HttpProperty httpProperty)
//          throws IOException, ClientProtocolException, SchedulerException, EntryNotFoundException {
//
////    Logger logger = LogManager.getLogger(HTTPConnection.class);
////    HttpClient client = HttpClientBuilder.create().build();
////    HttpPost post = new HttpPost(httpProperty.getUrl());
////    post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
////    HttpResponse response = client.execute(post);
////    int statusCode = response.getStatusLine().getStatusCode();
//
//    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//    RequestBody body = RequestBody.create(httpProperty.getBodyParam(), mediaType);
//
////    FormBody.Builder formBodyBuilder = new FormBody.Builder();
////    for (NameValuePair pair : postParams) {
////      formBodyBuilder.add(pair.getName(), pair.getValue());
////    }
////    RequestBody body = formBodyBuilder.build();
//
//    Request request = new Request.Builder()
//            .url(httpProperty.getUrl())
//            .method("POST", body)
//            .addHeader("Content-Type", "application/json; charset=utf-8")
//            .build();
//
//    long time = System.currentTimeMillis();
//    try (Response response = HttpClient.getInstance().getClient().newCall(request).execute()){
//      responseHandler.handleJob(httpProperty, response, System.currentTimeMillis() - time);
//    } catch (Exception e) {
//      if (!e.getMessage().startsWith("[FIRED]"))
//        responseHandler.handleJobError(httpProperty, e, System.currentTimeMillis() - time);
//      throw new JobExecutionException(e);
//    }
//
////    logger.info("Response return with status code: " + statusCode);
////    HttpEntity entity = response.getEntity();
////    if(entity != null)
////    {
////        Header encodingHeader = entity.getContentEncoding();
////        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
////        String result = EntityUtils.toString(entity, encoding);
////        logger.info("Response return with result: " + result);
////    }
////    return statusCode;
//  }
//}
