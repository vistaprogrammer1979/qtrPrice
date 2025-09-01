package com.santechture.facts.exception;

import com.santechture.facts.entity.PriceLog;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.UUID;

//@Provider
public class ApplicationExceptionHandler  implements ExceptionMapper<RuntimeException> {
    final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    @Inject
    ContainerRequestContext requestContext;
    @Inject
    com.santechture.facts.uae.service.KafkaMessagingService kafkaMessagingServiceUAE;
    @Inject
    com.santechture.facts.qatar.service.KafkaMessagingService kafkaMessagingServiceQatar;
    @Inject
    com.santechture.facts.qatar.service.KafkaMessagingService kafkaMessagingServiceKSA;

    @Override
    public Response toResponse(RuntimeException exception )
    {
        if(requestContext.getUriInfo().getPath().contains("qatar")){
            logger.error(exception.getMessage());
            try {
                var uuidstr = requestContext.getHeaders().getFirst("X-Request-ID");
                UUID uuid = UUID.fromString(uuidstr);
                kafkaMessagingServiceQatar.sendLogToKafka(
                        uuid,
                        new PriceLog(
                                uuid,
                                Arrays.toString(exception.getStackTrace()),
                                exception.getMessage())
                );
            for (StackTraceElement ste : exception.getStackTrace()) {
                logger.error(ste.toString());
            }

            var resp = new GeneralResponse(500, "Error in validate request", "Please contact with the administrator.");

                return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
            }
            catch (Exception e){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        }
        else {
            logger.error(exception.getMessage());
            try {
                var uuidstr = requestContext.getHeaders().getFirst("X-Request-ID");
                UUID uuid = UUID.fromString(uuidstr);
                kafkaMessagingServiceUAE.sendLogToKafka(
                        uuid,
                        new PriceLog(
                                uuid,
                                Arrays.toString(exception.getStackTrace()),
                                exception.getMessage())
                );
                var resp = new GeneralResponse(500, "Error in validate request", "Please contact with the administrator.");

                return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
            }
            catch (Exception e){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }
        }

    }

}
