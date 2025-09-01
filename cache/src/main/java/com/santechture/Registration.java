package com.santechture;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.UUID;

@ApplicationScoped
public class Registration {
    @ConfigProperty(name = "registration.status")
    boolean status;

    private static final Logger LOGGER = Logger.getLogger("Registration");
    String serverId;
    ConsulClient client;
    @Inject
    Vertx vertx;
    @ConfigProperty(name = "consul.host") String host;
    @ConfigProperty(name = "consul.port") int port;

    @ConfigProperty(name = "container.name", defaultValue = "price-cache") String containerName;
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
        LOGGER.info(status);
        if (status){
            this.client = ConsulClient.create(vertx, new ConsulClientOptions().setHost(host).setPort(port));
            this.serverId = UUID.randomUUID().toString();
            LOGGER.info(serverId);
            var httpCheckURL = String.format("http://%s:%s/%s",quarkusConsulHost,httpPort,checkHttpPath);
            LOGGER.info("The application  URL is: " + httpCheckURL);
            CheckOptions checkOptions = new CheckOptions()
                    .setDeregisterAfter("1s")

                    .setHttp(httpCheckURL) // Adjust the URL as needed
                    .setInterval("5s");
            client.registerServiceAndAwait(
                    new ServiceOptions()
                            .setCheckOptions(checkOptions)
                            .setPort(httpPort)
                            .setAddress(containerName).setName("price-cache")
                            .setId(this.serverId));
        }
        else {
            LOGGER.info("No need to registration in Console");
        }

    }

    void onShutdown(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        if(status){
            this.client.deregisterService(this.serverId).subscribe()
                    .with(
                            success -> System.out.println("Service deregistered"),
                            failure -> System.err.println("Failed to deregister service: " + failure.getMessage())
                    );
        }
        else {
            LOGGER.info("No need to de-registration from Console");
        }

    }

}