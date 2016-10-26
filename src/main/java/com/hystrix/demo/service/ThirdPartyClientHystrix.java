package com.hystrix.demo.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ThirdPartyClientHystrix {

    private final CloseableHttpClient httpClient;

    /**
     * Hystrix handles creating a work queue and thread pool.
     * Each command that is executed with the same group will use the same work queue and thread pool.
     * The group is defined by passing it to super() when extending a Hystrix command.
     **/
    public ThirdPartyClientHystrix(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String callService() {
        return new ThirdPartyClientCommand().execute();
    }

    class ThirdPartyClientCommand extends HystrixCommand<String> {

        public ThirdPartyClientCommand() {
            super(HystrixCommandGroupKey.Factory.asKey("ThirdPartyService"));
        }

        @Override
        protected String run() throws Exception {
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

        @Override
        protected String getFallback() {
            return "This is a Hystrix fallback response";
        }
    }
}
