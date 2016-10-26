package com.hystrix.demo.api;

import com.codahale.metrics.annotation.Timed;
import com.hystrix.demo.service.ThirdPartyClientHystrix;
import com.hystrix.demo.service.ThirdPartyClientNoHystrix;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationResource {

    private ThirdPartyClientNoHystrix clientNoHystrix;
    private ThirdPartyClientHystrix clientHystrix;

    public ApplicationResource(ThirdPartyClientNoHystrix clientNoHystrix, ThirdPartyClientHystrix clientHystrix) {
        this.clientNoHystrix = clientNoHystrix;
        this.clientHystrix = clientHystrix;
    }

    @GET
    @Path("using-hystrix")
    @Timed
    public String resourceWithHystrix() {
        String thirdPartyResponseMessage = clientHystrix.callService();
        return "{\"message\": \"" + thirdPartyResponseMessage + "\", \"resource\": \"using hystrix\"}";
    }

    @GET
    @Path("no-hystrix")
    @Timed
    public String resourceNoHystrix() {
        String thirdPartyResponseMessage = clientNoHystrix.callService();
        return "{\"message\": \"" + thirdPartyResponseMessage + "\", \"resource\": \"no hystrix\"}";
    }
}
