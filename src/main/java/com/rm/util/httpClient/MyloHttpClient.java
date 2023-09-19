package com.rm.util.httpClient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;

import java.net.URISyntaxException;

public class MyloHttpClient extends AbstractMyloHttpClient {

    public MyloHttpClient() {
        super();
    }

    @Override
    public AbstractMyloHttpClient build(String uri) throws URISyntaxException {
        return super.build(uri);
    }

    @Override
    public AbstractMyloHttpClient post(HttpEntity entity, Header[] headers) throws URISyntaxException {
        return super.post(entity, headers);
    }

    @Override
    public AbstractMyloHttpClient get() throws URISyntaxException {
        return super.get();
    }
    @Override
    public AbstractMyloHttpClient delete() throws URISyntaxException {
        return super.delete();
    }
    @Override
    public String getRespones() throws ClientProtocolException {
        return super.getRespones();
    }

}
