package com.santechture.resource.uae;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Path("api/v1/cache/uae")
public class CacheResources {
    private static final Logger log = LoggerFactory.getLogger(CacheResources.class);
    @RestClient
    PriceCacheAPI priceCacheAPI;

    @GET
    public Response cache() {
        log.info("received start caching request");
        startCacheAsync();
        return Response.ok().build();
    }
    private void startCacheAsync() {
        CompletableFuture.runAsync(() -> priceCacheAPI.startCache());
    }
    private void startUpdateCacheAsync(String model) {
        CompletableFuture.runAsync(() -> priceCacheAPI.startUpdateCache(model));
    }
    @GET
    @Path("info")
    public Response getStatus()  {
        log.info("received get cache status request");
        return priceCacheAPI.getCacheStatus();
    }
    @GET
    @Path("update/{model}")
    public Response cache(@PathParam("model") String model) {
        log.info("received update caching request");
         startUpdateCacheAsync(model);
       return Response.ok().build();
    }

}
