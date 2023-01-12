package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.req.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/diff")
@RegisterRestClient(configKey = "external-api")
@RegisterClientHeaders
public interface ZenRestClientService {

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    String getDiff(@HeaderParam(value = "Authorization") String authorization, RequestBody body);
}
