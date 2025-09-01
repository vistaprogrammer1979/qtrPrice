package com.santechture.resource.qatar;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Path("api/v1/cache/qtr")
public class QatarCacheResources {
    private static final Logger log = LoggerFactory.getLogger(QatarCacheResources.class);
    @RestClient
    QatarPriceCacheAPI priceCacheAPI;
    @RestClient
    QatarPriceCacheAPI qatarPriceCacheAPI;


    @POST
    public Response cache() {
        log.info("received start caching request - Qatar");
        startCacheAsync();
        return Response.ok().build();
    }
    private void startCacheAsync() {
        CompletableFuture.runAsync(() -> qatarPriceCacheAPI.startCache());
    }
    private void startUpdateCacheAsync(String model) {
        CompletableFuture.runAsync(() -> qatarPriceCacheAPI.startUpdateCache(model));
    }
    @GET
    @Path("info")
    public Response getStatus()  {
        log.info("received get cache status request - Qatar");
        return qatarPriceCacheAPI.getCacheStatus();
    }
    @POST
    @Path("update/{model}")
    public Response cache(@PathParam("model") String model) {
        log.info("received update caching request - Qatar");
         startUpdateCacheAsync(model);
       return Response.ok().build();
    }

}
