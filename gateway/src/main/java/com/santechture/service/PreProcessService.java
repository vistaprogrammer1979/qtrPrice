package com.santechture.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.kafka.common.requests.RequestContext;

import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class PreProcessService {
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


    @WithSpan("Intilaize Request Context")
    public ContainerRequestContext getRequestContext(ContainerRequestContext requestContext) {
        var startTime = Instant.now();
        UUID requestId = UUID.randomUUID();
        var hostname = requestContext.getUriInfo().getRequestUri().getHost();
        var endpoint = requestContext.getUriInfo().getRequestUri().toString();


        requestContext.setProperty(REQUEST_TIME_PROPERTY, startTime);
        requestContext.setProperty(REQUEST_ID_PROPERTY, requestId);
        requestContext.setProperty(REQUEST_HOST_IP_PROPERTY, hostname);
        requestContext.setProperty(ENDPOINT_PROPERTY, endpoint);
        requestContext.getHeaders().add("X-Request-ID", requestId.toString());

        // Capture the 'name' parameter from the query parameters
        String name = requestContext.getUriInfo().getQueryParameters().getFirst("name");
        requestContext.setProperty(NAME_PROPERTY, name);
        return requestContext;
    }
}
