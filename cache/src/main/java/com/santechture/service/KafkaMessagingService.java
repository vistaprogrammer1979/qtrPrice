package com.santechture.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class KafkaMessagingService {
    final static Logger log  = LoggerFactory.getLogger(KafkaMessagingService.class);

    @Channel("price-cache-management")
    Emitter<Record<String, String>> priceCacheEmitter;

    @Channel("price-cache-management-qatar")
    Emitter<Record<String, String>> priceQatarCacheEmitter;

    @Channel("price-cache-management-ksa")
    Emitter<Record<String, String>> priceKSACacheEmitter;

    @Inject
    ObjectMapper objectMapper;


    public void sendUpdateModelKeyToKafka(String model) {
        try{
            if (model.equals("all")) {
                priceCacheEmitter.send(Record.of("", "all"));
                log.info("Sent update event for all models to kafka.");
            } else {
                priceCacheEmitter.send(Record.of("", model));
                log.info("Sent update event for model {} to kafka.",model);
            }

        }catch (Exception e){
            log.error(String.format("Couldn't send update event with key %s to kafka. ",model), e);
        }
    }

    public void sendUpdateModelKeyToKafkaQatar(String model) {
        try{
            if (model.equals("all")) {
                priceQatarCacheEmitter.send(Record.of("", "all"));
                log.info("Sent update event for all models to kafka - Qatar.");
            } else {
                priceQatarCacheEmitter.send(Record.of("", model));
                log.info("Sent update event for model {} to kafka- Qatar .",model);
            }

        }catch (Exception e){
            log.error(String.format("Couldn't send update event with key %s to kafka- Qatar. ",model), e);
        }
    }

    public void sendUpdateModelKeyToKafkaKSA(String model) {
        try{
            if (model.equals("all")) {
                priceKSACacheEmitter.send(Record.of("", "all"));
                log.info("Sent update event for all models to kafka - KSA.");
            } else {
                priceKSACacheEmitter.send(Record.of("", model));
                log.info("Sent update event for model {} to kafka- KSA .",model);
            }

        }catch (Exception e){
            log.error(String.format("Couldn't send update event with key %s to kafka- Qatar. ",model), e);
        }
    }

}