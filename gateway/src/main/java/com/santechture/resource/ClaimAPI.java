package com.santechture.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.exception.GeneralException;
import com.santechture.filter.Loggable;
import com.santechture.request.Claim;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Collectors;


@Path("/api/v1")
public class ClaimAPI {

    private static final Logger log = LoggerFactory.getLogger(ClaimAPI.class);
    @RestClient
    PriceFactAPI priceFact;
    @Inject
    ObjectMapper objectMapper;

    @POST
    @Path("price")
    @Loggable
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Claim validate(@Valid Claim claim,@HeaderParam("X-Request-ID") String requestId) throws JsonProcessingException, InterruptedException {
        log.info("received validate request");
        log.info("RequestId: {}" , requestId);
            return priceFact.price(claim, UUID.fromString(requestId));
    }


    @POST
    @Path("price/qatar")
    @Loggable
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Claim validateQatar(
            @Valid Claim claim,
            @HeaderParam("X-Request-ID") String requestId) throws JsonProcessingException, InterruptedException {
        log.info("received validate request");
        log.info("RequestId: {}" , requestId);
        log.info(objectMapper.writeValueAsString(claim));

            return priceFact.priceQatar(claim, UUID.fromString(requestId));

    }

    @POST
    @Path("price/ksa")
//    @Loggable
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public com.santechture.request.Claim validateKSA(
            @Valid com.santechture.request.ksa.Claim claim,
            @HeaderParam("X-Request-ID") String requestId) throws JsonProcessingException, InterruptedException, GeneralException {
        log.info("received validate request for KSA");
        log.info("RequestId: {}" , requestId);
//        log.info(objectMapper.writeValueAsString(claim));
//        var activities = claim.getActivity().stream().filter(a -> a.getType() == null).map(a -> a.getCode()).toList();
//        if (activities.isEmpty()) {

            return priceFact.priceKSA(claim);
//        }
//        throw new GeneralException(String.format("Activity type is required %s ", String.join(",", activities)));

    }




}