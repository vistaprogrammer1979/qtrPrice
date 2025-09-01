package com.santechture.facts.qatar.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.facts.qatar.service.DataService;
import com.santechture.facts.qatar.service.KafkaMessagingService;
import com.santechture.facts.qatar.service.cache.RedisService;
import com.santechture.request.Activity;
import com.santechture.request.Claim;
import com.santechture.request.Data;
import com.santechture.request.Facts;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Path("api/v1/qatar")
public class PriceQatarResource {

    final static Logger logger = LoggerFactory.getLogger(PriceQatarResource.class);
    final static ModelMapper modelMapper = new ModelMapper();
    private static final String REQUEST_ID_PROPERTY = "request-id";
    @RestClient
    PriceEngineQatart priceEngine;
    @Inject
    RedisService cacheService;
    @Inject
    ObjectMapper mapper;
    @Inject
    DataService dataService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    KafkaMessagingService kafkaMessagingService;
    @Inject
    RedisService redisService;
    @Inject
    Tracer tracer;
    @ConfigProperty(name = "container.name")
    String containerName;

    @PostConstruct
    public void init() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @POST
    @Path("price/{request-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @Loggable
    public Claim price(Claim claim, @PathParam("request-id") UUID requestId) throws JsonProcessingException {
        Span span = tracer.spanBuilder("Price Facts").setAttribute("containerName", containerName).setParent(Context.current().with(Span.current())).setSpanKind(SpanKind.INTERNAL).startSpan();
        logger.info("Receive qatar price request id: {}", requestId);
        logger.info(objectMapper.writeValueAsString(claim));
        Data data = new Data();
        data.setClaim(claim);
//        var isValid = redisService.isValid();
//        if (!isValid) {
//            return Response.status(404,    "The requested data in Redis is either corrupted or outdated. "+
//                      "Please retry after triggering a cache. If the issue persists, contact support" ).build();
//        }

        resetPriceData(claim);
//        dataService.addDataParallel(data);
        dataService.addData(data);
        logger.info(objectMapper.writeValueAsString(data));
        redisService.saveRequest(requestId.toString(), data);
        data = priceEngine.price(data);
        kafkaMessagingService.sendFactToKafka(requestId, modelMapper.map(data, Facts.class));
        logger.info(objectMapper.writeValueAsString(data.getClaim()));
        span.end();
        return data.getClaim();
    }

    private void resetPriceData(Claim claim) {
        claim.setSPC_ID(null);
        claim.setCUS_ID(null);
        claim.setNet(null);
        claim.setGross(null);
        claim.setPatientShare(null);
        claim.setOutcome(null);
        claim.setMultipleProcedures(null);
        for (Activity act : claim.getActivity()) {
            act.setDiscount(null);
            act.setSPCFactor(null);
            if (act.getCustom_Price_Types() == null || !act.getCustom_Price_Types().contains("3")) {
                act.setList(null);
            }
            act.setDiscountPercentage(null);
            act.setDeductible(null);
            act.setCopayment(null);
            act.setCoInsurance(null);
            act.setGross(null);
            act.setNet(null);
            act.setOutcome(null);
            act.setPatientShare(null);
            act.setAnaesthesiaBaseUnits(null);
            act.setActivityGroup(null);
            act.setListPricePredifined(null);
        }
    }
}
