package com.santechture;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

@QuarkusMain
public class Main {

    public static void main(String ... args) {
        Logger log = Logger.getLogger(Main.class);
        log.info("Running main method");
        Quarkus.run(args);
    }
}
