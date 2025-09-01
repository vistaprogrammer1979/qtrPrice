package com.santechture.resource.qatar;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


/**
 * The REST Client interface.
 *
 * Notice the `baseUri`. It uses `stork://` as URL scheme indicating that the called service uses Stork to locate and
 * select the service instance. The `my-service` part is the service name. This is used to configure Stork discovery
 * and selection in the `application.properties` file.
 */

@RegisterRestClient(baseUri = "stork://price-cache")
@Path("api/v1/qatar/cache")
public interface QatarPriceCacheAPI {
    @POST
    Response startCache();

    @GET
    @Path("info")
    Response getCacheStatus();

    @POST
    @Path("update/{model}")
     Response startUpdateCache(@PathParam("model") String model);

}