package com.github.storytime.lambda.exporter.common.service;

import com.github.storytime.lambda.exporter.common.model.req.RequestBody;
import com.github.storytime.lambda.exporter.common.model.zen.ZenResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/diff")
@RegisterRestClient(configKey = "external-api")
@RegisterClientHeaders
public interface RestClientService {

    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    ZenResponse requestUser(@HeaderParam(value = "Authorization") String authorization,
                            RequestBody body);
}
