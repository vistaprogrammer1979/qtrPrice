package com.santechture.filter;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.pojo.PriceMarket;
import com.santechture.pojo.RequestMetaData;
import com.santechture.pojo.ResponseMetaData;
import com.santechture.pojo.TypeEnum;
import com.santechture.request.Activity;
import com.santechture.request.Claim;
import com.santechture.request.CodeType;
import com.santechture.service.KafkaMessagingService;
import com.santechture.service.RequestService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Provider
@Priority(1000)
@Loggable
public class RequestResponseLoggerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String REQUEST_TIME_PROPERTY = "request-time";
    private static final String REQUEST_BODY_PROPERTY = "request-body";
    private static final String NAME_PROPERTY = "name";
    private static final String REQUEST_ID_PROPERTY = "request-id";
    private static final String REQUEST_HOST_IP_PROPERTY = "host-name";
    private static final String ENDPOINT_PROPERTY = "endpoint";
    private static final String IS_SAVED_PROPERTY = "is-saved";
    private static Instant startTime;
    private static Instant endTime;
    //    private static UUID requestId;
    private static String hostname;
    private static String requestBody;
    private static String endpoint;
    private static String responseString;
    private static Claim claim;
    private final ModelMapper mapper = new ModelMapper();
    @Inject
    ObjectMapper objectMapper;
    @Inject
    Logger logger;
    @Inject
    KafkaMessagingService kafkaMessagingService;
    @Inject
    RequestService requestService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (requestContext.getUriInfo().getRequestUri().getPath().startsWith("/api/v1/price")) {
            startTime = Instant.now();
            UUID requestId = UUID.randomUUID();
            hostname = requestContext.getUriInfo().getRequestUri().getHost();
            endpoint = requestContext.getUriInfo().getRequestUri().toString();


            requestContext.setProperty(REQUEST_TIME_PROPERTY, startTime);
            requestContext.setProperty(REQUEST_ID_PROPERTY, requestId);
            requestContext.setProperty(REQUEST_HOST_IP_PROPERTY, hostname);
            requestContext.setProperty(ENDPOINT_PROPERTY, endpoint);
            requestContext.getHeaders().add("X-Request-ID", requestId.toString());

            // Capture the 'name' parameter from the query parameters
            String name = requestContext.getUriInfo().getQueryParameters().getFirst("name");
            requestContext.setProperty(NAME_PROPERTY, name);


            // Read the request body and cache it for later use
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream requestStream = requestContext.getEntityStream();
            copyStream(requestStream, baos);


            requestBody = baos.toString(StandardCharsets.UTF_8);

            requestContext.setProperty(REQUEST_BODY_PROPERTY, requestBody);

            var market = PriceMarket.UAE;
            var type = TypeEnum.DHA;
            if (requestContext.getUriInfo().getPath().contains("qatar")) {
                market = PriceMarket.QATAR;
                type = TypeEnum.QTR;
            } else if (requestContext.getUriInfo().getPath().contains("ksa")) {
                market = PriceMarket.KSA;
                type = TypeEnum.KSA;
            }

            //send request to kafka

            try {
                if (requestContext.getUriInfo().getPath().contains("ksa")) {
                    var ksaClaim = objectMapper.readValue(requestBody, com.santechture.request.ksa.Claim.class);


                    claim = objectMapper.readValue(requestBody, Claim.class);
                    if (ksaClaim.getActivity() != null) {
                        var activities = new ArrayList<Activity>();
                        for (var ka : ksaClaim.getActivity()) {
                            var a = mapper.map(ka, Activity.class);
                            a.setType(CodeType.fromKSACodeType(ka.getType()));
                            activities.add(a);
                        }
                        claim.setActivity(activities);
                    }
                } else {
                    claim = objectMapper.readValue(requestBody, Claim.class);
                }

                claim.setId(claim.getRootID());

                requestContext.setProperty(IS_SAVED_PROPERTY, false);
                var isSaved = requestService.save(new RequestMetaData(claim, requestId, Timestamp.from(startTime), hostname, endpoint, name, requestBody, type, market));
                requestBody = objectMapper.writeValueAsString(claim);
                requestContext.setProperty(IS_SAVED_PROPERTY, isSaved);
                requestContext.getHeaders().add("X-Is-Request-Saved", String.valueOf(isSaved));

//            requestService.saveRequest(requestId, Timestamp.from(startTime), claim.getReceiverID(), claim.getId() != null ? claim.getId().toString() : "", span.getSpanContext().getTraceId(), TypeEnum.DHA, market);
//            kafkaMessagingService.sendRequestToKafka(requestId, new RequestMetaData(claim, requestId, Timestamp.from(startTime), hostname, endpoint, name, null, TypeEnum.DHA));
//            kafkaMessagingService.sendingRequestToKafka(requestId, objectMapper.writeValueAsString(new RequestMetaData(claim, requestId, Timestamp.from(startTime), hostname, endpoint, name, null, TypeEnum.DHA));
            }catch (JsonMappingException e){
                logger.error(e.getMessage());
                throw e;
            } catch (Exception e) {
                logger.warn(e.getMessage());
//            kafkaMessagingService.sendRequestToKafka(requestId, new RequestMetaData(null, requestId, Timestamp.from(startTime), hostname, endpoint, name, requestBody, TypeEnum.DHA));
            }

            // Replace the request entity stream with a new stream that reads from the cached bytes


            requestContext.setEntityStream(new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8)));
        }

    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (requestContext.getUriInfo().getRequestUri().getPath().startsWith("/api/v1/price")) {
            if (requestContext.getProperty(IS_SAVED_PROPERTY) != null && (boolean) requestContext.getProperty(IS_SAVED_PROPERTY)) {
                Object responseBody = responseContext.getEntity() != null ? responseContext.getEntity() : null;
                try {
                    responseString = objectMapper.writeValueAsString(responseBody);
                } catch (Exception e) {
                    responseString = "";
                }
                UUID requestId = (UUID) requestContext.getProperty(REQUEST_ID_PROPERTY);
                endTime = Instant.now();
                var market = PriceMarket.UAE;
                if (requestContext.getUriInfo().getPath().contains("qatar")) {
                    market = PriceMarket.QATAR;

                } else if (requestContext.getUriInfo().getPath().contains("ksa")) {
                    market = PriceMarket.KSA;

                }
                if (responseContext.getStatus() < 400) {
                    try {
                        claim = objectMapper.readValue(responseString, Claim.class);
                        kafkaMessagingService.sendResponseToKafka(requestId, new ResponseMetaData(claim, requestId, responseContext.getStatus(), Timestamp.from(endTime), java.time.Duration.between((Instant) requestContext.getProperty(REQUEST_TIME_PROPERTY), endTime).toMillis(), null), market);
                    } catch (Exception e) {
                        logger.error("Couldn't send response", e);
                        logger.error(e.getMessage());

                    }
                } else {
                    logger.error("Sending failure response to kafka ");
//                    requestService.saveErrorMessage(requestId, responseString);
                    try {
                        kafkaMessagingService.sendFailureResponseToKafka(requestId, new ResponseMetaData(null, requestId, responseContext.getStatus(), Timestamp.from(endTime), java.time.Duration.between((Instant) requestContext.getProperty(REQUEST_TIME_PROPERTY), endTime).toMillis(), responseString), market);

                    } catch (Exception e) {
                        logger.error("Couldn't send failure response to kafka", e);
                    }
                }


//        RequestLog log = new RequestLog(ipSender, requestTime, name, requestBody, responseString, processingPeriod);
//        logService.saveLog(log);
            }
        }

    }

    private void copyStream(InputStream input, ByteArrayOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
    }
}