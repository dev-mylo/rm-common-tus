package com.rm.util.httpClient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.net.URISyntaxException;

public interface MyloHttp {

    /**
     *
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public AbstractMyloHttpClient build(String uri) throws URISyntaxException;

    /**
     * HttpEntity 상속받은 타입 무엇이든 생성시켜 넣어주면 된다.
     * @param entity
     * @return
     * @throws URISyntaxException
     */
    public AbstractMyloHttpClient post(HttpEntity entity, Header[] headers) throws URISyntaxException;

    /**
     * 앞서 작성된 uri에 값들을 넣어서 해주는 방식인데... 추후 맵과 같은 객체로 받아서 알아서 uri에 넣어주는 형태면 좋을 듯 하다. 즉 post비슷하게시리~
     * @return
     * @throws URISyntaxException
     */
    public AbstractMyloHttpClient get() throws URISyntaxException;

    /**
     *
     * @return
     * @throws IOException
     */
    public String getRespones() throws IOException;


}
