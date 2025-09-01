package com.santechture.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.entity.PriceLog;
import com.santechture.pojo.PriceMarket;
import com.santechture.pojo.RequestMetaData;
import com.santechture.pojo.ResponseMetaData;
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
//    Qatar
    @Channel("price-engine-responses-qatar")
    Emitter<Record<UUID, String>> priceQatarResponseEmitter;
    @Channel("price-engine-failure-responses-qatar")
    Emitter<Record<UUID, String>> priceQatarFailureResponseEmitter;
    @Channel("price-engine-logs-qatar")
    Emitter<Record<UUID, String>> priceQatarLogEmitter;
//    KSA
    @Channel("price-engine-responses-ksa")
    Emitter<Record<UUID, String>> priceKsaResponseEmitter;
    @Channel("price-engine-failure-responses-ksa")
    Emitter<Record<UUID, String>> priceKsaFailureResponseEmitter;
    @Channel("price-engine-logs-ksa")
    Emitter<Record<UUID, String>> priceKsaLogEmitter;

    @Inject
    ObjectMapper objectMapper;
//    @ConfigProperty(name = "mp.messaging.outgoing.price-engine-requests.topic")
//    String topic;
//    @Outgoing("price-engine-requests")
//    public Multi<Message<String>> sendingRequestToKafka(UUID uuid, String requestMetaData) {
//
//           Message<String> message = Message.of(requestMetaData)
//                    .addMetadata(OutgoingKafkaRecordMetadata.<String>builder()
//                            .withKey(uuid.toString())
//                            .withTopic(topic)
//                            .withHeaders(new RecordHeaders().add("type", "request".getBytes())));
////            priceRequestEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(requestMetaData)));
//        return Multi.createFrom().item(message);
//
//    }
//    public void sendRequestToKafka(UUID uuid,RequestMetaData requestMetaData) {
//        try{
//
//            priceRequestEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(requestMetaData)));
//        }catch (Exception e){
//            log.error("Couldn't send request to kafka ", e);
//        }
//    }
    public void sendResponseToKafka(UUID uuid, ResponseMetaData claim, PriceMarket market) {
        try{
            log.info("sending response to kafka topic for market {} with id {}", market, uuid);
            switch (market){
                case UAE ->  priceResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(claim)));
                case QATAR ->  priceQatarResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(claim)));
                case KSA ->  priceKsaResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(claim)));
            }


        }catch (Exception e){
            log.error("Couldn't send response to kafka ", e);
        }
    }
    public void sendFailureResponseToKafka(UUID uuid, ResponseMetaData metaData, PriceMarket market) {
        try{
            switch (market){
                case UAE -> priceFailureResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(metaData)));
                case QATAR -> priceQatarFailureResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(metaData)));
                case KSA -> priceKsaFailureResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(metaData)));
            }

        }catch (Exception e){
            log.error("Couldn't send failure response to kafka ", e);
        }
    }

    public void sendLogToKafka(UUID uuid, PriceLog priceLog, PriceMarket market) {
        try{
            switch (market){
                case UAE -> priceLogEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(priceLog)));
                case QATAR -> priceQatarLogEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(priceLog)));
                case KSA -> priceKsaLogEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(priceLog)));
            }

        }catch (Exception e){
            log.error(String.format("Couldn't send log with request id %s to kafka. ",uuid), e);
        }
    }


}