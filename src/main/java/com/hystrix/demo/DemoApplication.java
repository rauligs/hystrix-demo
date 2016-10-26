package com.hystrix.demo;

import com.hystrix.demo.api.ApplicationResource;
import com.hystrix.demo.service.ThirdPartyClientHystrix;
import com.hystrix.demo.service.ThirdPartyClientNoHystrix;
import com.netflix.config.ConfigurationManager;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mockserver.client.server.MockServerClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class DemoApplication extends Application<DemoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "Demo";
    }

    @Override
    public void initialize(final Bootstrap<DemoConfiguration> bootstrap) {
    }

    @Override
    public void run(final DemoConfiguration configuration,
                    final Environment environment) {

        ConfigurationManager.install(new MapConfiguration(configuration.getDefaultHystrixConfig()));

        new MockServerClient("192.168.99.100", 1080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/service")
                )
                .respond(
                        response()
                                .withHeader("Content-type", "text/plain")
                                .withStatusCode(200)
                                .withBody("response from third party")
                        //.withDelay(new Delay(SECONDS, 1))
                );


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        final ApplicationResource applicationResource = new ApplicationResource(new ThirdPartyClientNoHystrix(httpClient), new ThirdPartyClientHystrix(httpClient));
        environment.jersey().register(applicationResource);
    }
}
