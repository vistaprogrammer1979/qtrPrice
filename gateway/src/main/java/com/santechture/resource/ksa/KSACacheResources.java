package com.santechture.resource.ksa;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Path("api/v1/cache/ksa")
public class KSACacheResources {
    private static final Logger log = LoggerFactory.getLogger(KSACacheResources.class);
    @RestClient
    KSAPriceCacheAPI priceCacheAPI;
    @RestClient
    KSAPriceCacheAPI ksaPriceCacheAPI;


    @POST
    public Response cache() {
        log.info("received start caching request - KSA");
        startCacheAsync();
        return Response.ok().build();
    }
    private void startCacheAsync() {
        CompletableFuture.runAsync(() -> ksaPriceCacheAPI.startCache());
    }
    private void startUpdateCacheAsync(String model) {
        CompletableFuture.runAsync(() -> ksaPriceCacheAPI.startUpdateCache(model));
    }
    @GET
    @Path("info")
    public Response getStatus()  {
        log.info("received get cache status request - KSA");
        return ksaPriceCacheAPI.getCacheStatus();
    }
    @POST
    @Path("update/{model}")
    public Response cache(@PathParam("model") String model) {
        log.info("received update caching request - KSA");
        startUpdateCacheAsync(model);
//         startUpdateCacheAsync(model);
       return Response.ok().build();
    }

}
