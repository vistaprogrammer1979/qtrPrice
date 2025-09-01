package com.santechture;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@QuarkusMain
@ApplicationScoped
@Startup

public class Main {
    Logger log = Logger.getLogger("main");
    @ConfigProperty(name = "quarkus.datasource.qatar.jdbc.url")
    String url;
    @ConfigProperty(name = "quarkus.datasource.qatar.password")
    String password;
    @ConfigProperty(name = "quarkus.datasource.qatar.username")
    String username;
    @Startup
    public void init(){
//        log.info("DB Qatar url " + url);
//        log.info("DB Qatar  password " + password);
//        log.info("DB Qatar username {}" + username);
    }

    public static void main(final String[] args) {
        Logger log = Logger.getLogger("main");
        log.info("Startup Price Cache");



        Quarkus.run();
    }
}
