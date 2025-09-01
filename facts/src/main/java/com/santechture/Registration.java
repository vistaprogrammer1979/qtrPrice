package com.santechture;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.mutiny.core.Vertx;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

import java.util.UUID;
@ApplicationScoped
public class Registration {
    private static final Logger LOGGER = Logger.getLogger("Registration");
    String server_id;

    @Inject
    Vertx vertx;
    ConsulClient client;

    @ConfigProperty(name = "consul.host") String host;
    @ConfigProperty(name = "consul.port") int port;

    @ConfigProperty(name = "container.name", defaultValue = "price-fact-1") String containerName;
    @ConfigProperty(name = "quarkus.http.port", defaultValue = "8080") int httpPort;

    @ConfigProperty(name = "quarkus.consul.host")
    String quarkusConsulHost;
    @ConfigProperty(name = "quarkus.consul.check.http.path")
    String checkHttpPath;



    /**
     * Register our two services in Consul.
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    @Startup
    public void init() {
        LOGGER.info("The application is starting...");
        this.server_id = UUID.randomUUID().toString();
        this.client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));
        CheckOptions checkOptions = new CheckOptions()
                .setDeregisterAfter("30s")
                .setHttp(String.format("http://%s:%s/%s",quarkusConsulHost,httpPort,checkHttpPath)) // Adjust the URL as needed
                .setInterval("5s");
        client.registerServiceAndAwait(
                new ServiceOptions().
                        setCheckOptions(checkOptions).
                        setPort(httpPort).setAddress(containerName).setName("price-fact").setId(server_id));
    }

    void onShutdown(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping.....");
        this.client.deregisterService(this.server_id).subscribe()
                .with(
                        success -> System.out.println("Service deregistered"),
                        failure -> System.err.println("Failed to deregister service: " + failure.getMessage())
                );
    }
}