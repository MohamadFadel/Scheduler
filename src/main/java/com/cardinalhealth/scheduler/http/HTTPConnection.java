package com.cardinalhealth.scheduler.http;

import com.cardinalhealth.scheduler.utility.Settings;
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class HTTPConnection
{
  public static int sendHTTPRequest(String url, List<NameValuePair> postParams)
    throws IOException, ClientProtocolException
  {
    Logger logger = LogManager.getLogger(HTTPConnection.class);
    HttpClient client = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost(url);
    post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
    HttpResponse response = client.execute(post);
    int statusCode = response.getStatusLine().getStatusCode();
    logger.info("Response return with status code: " + statusCode);
    HttpEntity entity = response.getEntity();
    if(entity != null)
    {
        Header encodingHeader = entity.getContentEncoding();
        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
        String result = EntityUtils.toString(entity, encoding);
        logger.info("Response return with result: " + result);
    }
    return statusCode;
  }

  public static String getAPIJobServerURL(String methodPath)
  {
    return getServerURL() + "/wm-ws/" + methodPath;
  }
  
  public static String getAPPJobServerURL(String actionName)
  {
    return getServerURL() + "/quartz/"+actionName+".action";
  }
  
  private static String getServerURL()
  {
    return Settings.getSettingValue("server.listen.address");
  }
  
}
