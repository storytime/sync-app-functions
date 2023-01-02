package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.req.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/diff")
@RegisterRestClient(configKey = "external-api")
@RegisterClientHeaders
public interface ZenRestClientService {

    @POST
    @Consumes("application/json")
    @Produces(MediaType.TEXT_PLAIN)
    String getDiff(@HeaderParam(value = "Authorization") String authorization, RequestBody body);
}
