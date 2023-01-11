package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.pb.PbArchiveData;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static com.github.storytime.lambda.exporter.configs.Constant.DATE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RegisterRestClient(configKey = "pb-api")
@RegisterClientHeaders
public interface PBCurrencyClientService {

    @GET
    @Produces(APPLICATION_JSON)
    PbArchiveData getRates(@QueryParam(DATE) String date);
}
