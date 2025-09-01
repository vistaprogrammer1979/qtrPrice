package com.santechture.resource;

import com.santechture.request.Claim;
import com.santechture.request.Data;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.UUID;


/**
 * The REST Client interface.
 *
 * Notice the `baseUri`. It uses `stork://` as URL scheme indicating that the called service uses Stork to locate and
 * select the service instance. The `my-service` part is the service name. This is used to configure Stork discovery
 * and selection in the `application.properties` file.
 */
@RegisterRestClient(baseUri = "stork://price-fact")
public interface PriceFactAPI {

    @POST
    @Path("/api/v1/price/{request-id}")
    Claim price(Claim claim,@PathParam("request-id") UUID requestId);

    @POST
    @Path("/api/v1/qatar/price/{request-id}")
    Claim priceQatar(Claim claim,@PathParam("request-id") UUID requestId);

    @POST
    @Path("/api/v1/ksa/price")
    com.santechture.request.Claim priceKSA(com.santechture.request.ksa.Claim claim);

//    @ClientExceptionMapper
//    static RuntimeException toException(Response response) {
//        if (response.getStatus() == 500) {
//            return new RuntimeException("The remote service responded with HTTP 500");
//        }
//        return null;
//    }
}