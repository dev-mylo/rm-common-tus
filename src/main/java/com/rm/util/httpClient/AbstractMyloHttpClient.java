package com.rm.util.httpClient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;

@Slf4j
public abstract class AbstractMyloHttpClient implements MyloHttp {

    private URIBuilder builder = null;
    private CloseableHttpClient httpClient = null;
    private HttpUriRequest request = null;

    protected AbstractMyloHttpClient() {

    }
    public AbstractMyloHttpClient build(String uri) throws URISyntaxException {
        builder = new URIBuilder(uri);
        return this;
    }

    public AbstractMyloHttpClient post(HttpEntity entity, Header[] headers) throws URISyntaxException {
        HttpPost httpPost = new HttpPost(builder.build());
        httpPost.setEntity(entity);
        httpPost.setHeaders(headers);

        request = httpPost;
        return this;
    }

    public static Header[] getJsonHeader() {
        return getJsonHeader(null);
    }

    public static Header[] getJsonHeader(String customUserAgent) {
        List<Header> headerList = new ArrayList<>();
        headerList.add(new BasicHeader(USER_AGENT, StringUtils.isBlank(customUserAgent) ? USER_AGENT : customUserAgent));
        headerList.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        return headerList.toArray(new Header[0]);
    }

    public AbstractMyloHttpClient get(Header[] headers) throws URISyntaxException {
        request = new HttpGet(builder.build());
        request.setHeaders(headers);
        return this;
    }

    public AbstractMyloHttpClient get() throws URISyntaxException {
        request = new HttpGet(builder.build());
        return this;
    }
    public AbstractMyloHttpClient delete(Header[] headers) throws URISyntaxException {
        request = new HttpDelete(builder.build());
        request.setHeaders(headers);
        return this;
    }

    public AbstractMyloHttpClient delete() throws URISyntaxException {
        request = new HttpDelete(builder.build());
        return this;
    }
    public AbstractMyloHttpClient getWithJsonHeader() throws URISyntaxException {
        request = new HttpGet(builder.build());
        request.addHeader(ACCEPT, "application/json");
        return this;
    }

    public String getRespones() throws ClientProtocolException {
        String responseBody = null;
        try {
            httpClient = HttpClientBuilder.create().build();
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseBody = httpClient.execute(request, responseHandler);
            return responseBody;
        } catch (IOException e) {
            log.error("error !!!!!!!!!!!!!!!! "+e.getMessage(), e);
            throw new ClientProtocolException(e.getMessage());
        }
    }
    public static List<Header> getJsonHeaderList() {
        List<Header> headerList = new ArrayList<>();
        headerList.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        headerList.add(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
        return headerList;
    }

}
