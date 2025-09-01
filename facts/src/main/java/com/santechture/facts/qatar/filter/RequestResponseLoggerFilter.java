package com.santechture.facts.qatar.filter;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.santechture.facts.service.KafkaMessagingService;
//import com.santechture.request.Claim;
//import jakarta.annotation.Priority;
//import jakarta.inject.Inject;
//import jakarta.ws.rs.container.ContainerRequestContext;
//import jakarta.ws.rs.container.ContainerResponseContext;
//import jakarta.ws.rs.container.ContainerResponseFilter;
//import jakarta.ws.rs.ext.Provider;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;

//@Provider
//@Priority(1000)
//@Loggable
public class RequestResponseLoggerFilter
//        implements ContainerResponseFilter
{


//    private static final String FACTS_HOSTNAME = "fact-api-hostname";
//    private static final String REQUEST_ID_PROPERTY = "X-Request-ID";
//    private final static Logger logger = LoggerFactory.getLogger(RequestResponseLoggerFilter.class);
//    private final static ObjectMapper mapper = new ObjectMapper();
//    @Inject
//    KafkaMessagingService kafkaMessagingService;
//    @ConfigProperty(name = "quarkus.application.name")
//    String applicationName;



//    @Override
//    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
//        try {
//            UUID uuid = UUID.fromString(String.valueOf(responseContext.getHeaders().getFirst(REQUEST_ID_PROPERTY)));
//            // Create a root JSON object
//            ObjectNode rootNode = mapper.createObjectNode();
//
//            // Add properties to the root object
//            rootNode.put("key", "fact");
//            rootNode.put("value", applicationName);
//            kafkaMessagingService.sendFactInstanceToKafka(
//                    uuid
//                    ,mapper.writeValueAsString(rootNode));
//        }catch (Exception e){
//            logger.error("Couldn't send fact instance to kafka ", e);
//
//        }
//
//        if (responseContext.getStatus() <= 400){
//
//            logger.error("Sending failure response to kafka ");
//
//            try {
//
//            }
//                kafkaMessagingService.sendFailureResponseToKafka(uuid,
//                        new ResponseMetaData(null, requestId, responseContext.getStatus(), Timestamp.from(endTime),
//                                java.time.Duration.between((Instant)requestContext.getProperty(REQUEST_TIME_PROPERTY), endTime).toMillis(), responseString));
//
//            }catch (Exception e){
//                logger.error("Couldn't send failure response to kafka", e);
//            }

//    }

    }
