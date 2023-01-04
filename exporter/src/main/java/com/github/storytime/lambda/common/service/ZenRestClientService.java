package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.req.RequestBody;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
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
    @Produces(MediaType.APPLICATION_JSON)
    ZenResponse getDiff(@HeaderParam(value = "Authorization") String authorization, RequestBody body);
}
