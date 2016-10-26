package com.hystrix.demo.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ThirdPartyClientNoHystrix {

    private final CloseableHttpClient httpClient;

    public ThirdPartyClientNoHystrix(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String callService() {
        try {
            HttpGet request = new HttpGet("http://192.168.99.100:1080/service");
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Returned a non 200 response");
            } else {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
