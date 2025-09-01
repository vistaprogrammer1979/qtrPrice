package com.santechture.resource.v2;

import com.santechture.entity.GenericGeneralResponse;
import com.santechture.request.Claim;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
    @Path("/api/v2/qatar/price/{request-id}")
    GenericGeneralResponse<Claim> priceQatar(Claim claim, @PathParam("request-id") UUID requestId);


}