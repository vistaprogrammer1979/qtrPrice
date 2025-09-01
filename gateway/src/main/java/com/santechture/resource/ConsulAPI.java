package com.santechture.resource;

import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.mutiny.core.Vertx;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/api/v1/consul")
public class ConsulAPI {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "consul.host") String host;
    @ConfigProperty(name = "consul.port") int port;

    @POST
    @Path("/price-facts/{host}/{port}")
    public void init(@PathParam("host") String service_host, @PathParam("port") Integer service_port) {
        ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));
        client.registerServiceAndAwait(
                new ServiceOptions().setPort(service_port).setAddress(service_host).setName("price-facts"));
    }
    @DELETE
    @Path("/{server_id}")
    public void deregister(@PathParam("server_id") String service_id) {
        ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));
        client.deregisterService(service_id)
                .subscribe()
                .with(
                        success -> System.out.println("Service deregistered"),
                        failure -> System.err.println("Failed to deregister service: " + failure.getMessage())
                );

    }
}
