package com.santechture.exception;

import com.santechture.entity.PriceLog;
import com.santechture.pojo.PriceMarket;
import com.santechture.service.KafkaMessagingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

@Provider
public class ApplicationExceptionHandler  implements ExceptionMapper<RuntimeException> {
    final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    @Inject
    ContainerRequestContext requestContext;
    @Inject
    KafkaMessagingService kafkaMessagingService;

    @Override
    public Response toResponse(RuntimeException exception )
    {
//        if(requestContext.getUriInfo().getRequestUri().getPath().startsWith("/api/v1/price")){
//            var uuidstr = requestContext.getHeaders().getFirst("X-Request-ID");
//            var market = PriceMarket.UAE;
//
//            if (requestContext.getUriInfo().getPath().contains("qatar")){
//                market = PriceMarket.QATAR;
//
//            }else if (requestContext.getUriInfo().getPath().contains("ksa")){
//                market = PriceMarket.KSA;
//
//            }
//            if (uuidstr == null){
//                logger.info("filter toResponse, request id :"+ uuidstr);
//                UUID uuid = UUID.fromString(uuidstr);
//                kafkaMessagingService.sendLogToKafka(
//                        uuid,
//                        new PriceLog(
//                                uuid,
//                                Arrays.toString(exception.getStackTrace()),
//                                exception.getMessage()), market);
//                logger.error(exception.getMessage());
//                for (StackTraceElement ste : exception.getStackTrace()) {
//                    logger.error(ste.toString());
//                }
//            }
////            throw ;
////        }
//
//        }
        logger.error(exception.getMessage());
        for (StackTraceElement ste : exception.getStackTrace()) {
            logger.error(ste.toString());
        }
        var resp = new GeneralResponse(500, exception.getMessage(), "Please contact with the administrator.");

        return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
    }
}
