package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.pb.PbArchiveData;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import static com.github.storytime.lambda.exporter.configs.Constant.DATE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RegisterRestClient(configKey = "pb-api")
@RegisterClientHeaders
public interface PBCurrencyClientService {

    @GET
    @Produces(APPLICATION_JSON)
    PbArchiveData getRates(@QueryParam(DATE) String date);
}
