package com.santechture.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.price.entity.PriceLog;
import com.santechture.price.pojo.ResponseMetaData;
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
    @Channel("price-engine-requests")
    Emitter<Record<UUID, String>> priceRequestEmitter;

    @Channel("price-engine-responses")
    Emitter<Record<UUID, String>> priceResponseEmitter;
    @Channel("price-engine-failure-responses")
    Emitter<Record<UUID, String>> priceFailureResponseEmitter;
    @Channel("price-engine-logs")
    Emitter<Record<UUID, String>> priceLogEmitter;
    @Inject
    ObjectMapper objectMapper;

    public void sendResponseToKafka(UUID uuid, ResponseMetaData claim) {
        try{

            priceResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(claim)));
        }catch (Exception e){
            log.error("Couldn't send response to kafka ", e);
        }
    }
    public void sendFailureResponseToKafka(UUID uuid, ResponseMetaData metaData) {
        try{

            priceFailureResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(metaData)));
        }catch (Exception e){
            log.error("Couldn't send failure response to kafka ", e);
        }
    }

    public void sendLogToKafka(UUID uuid, PriceLog priceLog) {
        try{
            priceLogEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(priceLog)));
        }catch (Exception e){
            log.error(String.format("Couldn't send log with request id %s to kafka. ",uuid), e);
        }
    }


}