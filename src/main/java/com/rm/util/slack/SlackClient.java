package com.rm.util.slack;

import com.rm.common.core.util.JsonUtils;
import com.rm.util.httpClient.MyloHttpClient;
import com.rm.util.httpClient.AbstractMyloHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SlackClient {

    public static void slackSend(String message, String url, boolean env) {
        // 운영계
        if (env) {
            Map<String, Object> data = new HashMap<>();
            data.put("text", message);
            try {
                new MyloHttpClient().build(url)
                        .post(new StringEntity(JsonUtils.toJson(data), "UTF-8"), AbstractMyloHttpClient.getJsonHeader()).getRespones();
            } catch (URISyntaxException e) {
                log.error("error  URISyntaxException error : " + e.getMessage());
            } catch (ClientProtocolException e) {
                log.error("error  ClientProtocolException error : " + e.getMessage());
            }
            log.warn("slackSend :", message);
        }
        // 개발계
        else {

        }
    }

}
