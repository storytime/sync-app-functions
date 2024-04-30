package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.req.RequestBody;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import static com.github.storytime.lambda.exporter.configs.Constant.AUTHORIZATION;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/diff")
@RegisterRestClient(configKey = "external-api")
@RegisterClientHeaders
public interface ZenRestClientService {

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    ZenResponse getDiff(@HeaderParam(value = AUTHORIZATION) String authorization, RequestBody body);
}
