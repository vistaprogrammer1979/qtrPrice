package com.santechture.resource.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.entity.GenericGeneralResponse;
import com.santechture.filter.Loggable;
import com.santechture.request.Claim;
import com.santechture.resource.v2.PriceFactAPI;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


@Path("/api/v2")
public class ClaimAPI {

    private static final Logger log = LoggerFactory.getLogger(ClaimAPI.class);
    @RestClient
    PriceFactAPI priceFact;
    @Inject
    ObjectMapper objectMapper;


    @POST
    @Path("price/qatar")
    @Loggable
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GenericGeneralResponse<Claim> validateQatar(@Valid Claim claim, @HeaderParam("X-Request-ID") String requestId) throws JsonProcessingException, InterruptedException {
        log.info("received validate request");
        log.info("RequestId: {}", requestId);
        log.info(objectMapper.writeValueAsString(claim));
        var res = priceFact.priceQatar(claim, UUID.fromString(requestId));
        log.info(objectMapper.writeValueAsString(res));
        return res;

    }

}
