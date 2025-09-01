package com.santechture.price.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.price.pojo.RequestMetaData;
import com.santechture.price.pojo.ResponseMetaData;
import com.santechture.price.pojo.TypeEnum;
import com.santechture.request.Claim;
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
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Provider
@Priority(1000)
@Loggable
public class RequestResponseLoggerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RequestResponseLoggerFilter.class);
    @Inject
    ObjectMapper objectMapper;
    @Inject
    Logger logger;
    @Inject
    KafkaMessagingService kafkaMessagingService;
    @Inject
    RequestService requestService;
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
    private static String responseString ;
    private static Claim claim;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("ContainerRequestContext filter");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        log.info("ContainerRequestContext ContainerResponseContext filter");
        if (responseContext.getStatus() >= 400 && requestContext.getUriInfo().getRequestUri().getPath().startsWith("/api/v1/price")){
            if (requestContext.getProperty(IS_SAVED_PROPERTY) != null && (boolean)requestContext.getProperty(IS_SAVED_PROPERTY)){

                Object responseBody = responseContext.getEntity() != null ? responseContext.getEntity() : null;
                try {
                    responseString = objectMapper.writeValueAsString(responseBody);
                }catch (Exception e){
                    responseString = "";
                }
                UUID requestId = (UUID)requestContext.getProperty(REQUEST_ID_PROPERTY);
                endTime = Instant.now();

                    logger.error("Sending failure response to kafka ");
                    try {
                        kafkaMessagingService.sendFailureResponseToKafka(requestId,
                                new ResponseMetaData(null, requestId, responseContext.getStatus(), Timestamp.from(endTime),
                                        java.time.Duration.between((Instant)requestContext.getProperty(REQUEST_TIME_PROPERTY), endTime).toMillis(), responseString));
                        responseContext.getHeaders().add("P-HANDLE-ERROR","TRUE");

                    }catch (Exception e){
                        logger.error("Couldn't send failure response to kafka", e);
                    }

            }
        }

    }
}