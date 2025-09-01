package com.santechture.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.entity.PriceBody;
import com.santechture.entity.PriceLog;
import com.santechture.entity.PriceRequest;
import com.santechture.pojo.RequestMetaData;
import com.santechture.pojo.ResponseMetaData;
import com.santechture.pojo.StatusEnum;
import com.santechture.repository.LogRepository;
import com.santechture.repository.RequestRepository;
import com.santechture.request.Data;
import com.santechture.request.Facts;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.TraceId;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Request;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped

public class KafkaMessagingService {

//    @Inject
//    TraceId traceId;

    @Inject
    Span span;

    @Inject
    Logger logger;

    @Inject
    RequestService requestService;

    @Inject
    LogRepository logRepository;

    @Inject
    ObjectMapper objectMapper;
//    @Transactional
//    @Incoming("price-engine-requests")
//    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
//    public void receiveRequestToKafka(Record<UUID, String>  message) {
//        logger.info("Received request to kafka " );
//    try{
//        RequestMetaData requestMetaData = objectMapper.readValue(message.value(), RequestMetaData.class);
//        requestService.save(requestMetaData);
//    }catch(Exception e){
//        logger.error("Error wile save request "+ e.getMessage() +": \n "+ message );
//        logRepository.save(new PriceLog(e.getMessage(), Timestamp.from(Instant.now()), message.key()));
//    }
//
//
//    }
    @Transactional
    @Incoming("price-engine-responses")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveResponseToKafka(Record<UUID, String> message) {
        logger.info("Received UAE response from kafka ");
        try{
            ResponseMetaData responseMetaData = objectMapper.readValue(message.value(), ResponseMetaData.class);
            requestService.saveResponse(responseMetaData);
        }catch(Exception e){
            logger.error("Error wile save UAE  Response "+ e.getMessage() +": \n "+ message );
//            logRepository.save(new PriceLog(e.getMessage(), Timestamp.from(Instant.now()), message.key()));

        }
    }

    @Incoming("price-engine-facts")
    public void receiveFactsFromKafka(Record<UUID, String> message) {
        logger.info("Received UAE facts for request "+ message.key().toString());
        try{
            requestService.saveFacts(message.key(), message.value());
        }catch(Exception e){
            logger.error("Error wile save UAE Facts "+ e.getMessage() +": \n "+ message );
        }
    }

    @Transactional
    @Incoming("price-engine-failure-responses")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveFailureResponseToKafka(Record<UUID, String> message) {
        logger.info("Received UAE  failure response from kafka ");
        try{
            ResponseMetaData responseMetaData = objectMapper.readValue(message.value(), ResponseMetaData.class);
            requestService.saveResponse(responseMetaData);
        }catch(Exception e){
            logger.error("Error wile save  UAE Response "+ e.getMessage() +": \n "+ message );
//            logRepository.save(new PriceLog(e.getMessage(), Timestamp.from(Instant.now()), message.key()));
        }
    }

    @Transactional
    @Incoming("price-engine-logs")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveLogToKafka(Record<UUID, String> message) {
        logger.info("Received  UAE log from kafka ");
        try{
            PriceLog priceLog = objectMapper.readValue(message.value(), PriceLog.class);
            var isSaved = logRepository.save(priceLog);
            if (isSaved){
                logger.info("price UAE  log saved for request " + message.key());
            }
            else{
                logger.error("price UAE  log not saved for request " + message.key());
            }
        }catch(Exception e){
            logger.error("Error wile save UAE  Log "+ e.getMessage() +": \n "+ message );
        }
    }

//    Qatar

    @Transactional
    @Incoming("price-engine-responses-qatar")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveResponseToKafkaQatar(Record<UUID, String> message) {
        logger.info("Received qatar response from kafka ");
        try{
            ResponseMetaData responseMetaData = objectMapper.readValue(message.value(), ResponseMetaData.class);
            requestService.saveResponse(responseMetaData);
        }catch(Exception e){
            logger.error("Error wile save qatar  Response "+ e.getMessage() +": \n "+ message );
//            logRepository.save(new PriceLog(e.getMessage(), Timestamp.from(Instant.now()), message.key()));

        }
    }

    @Incoming("price-engine-facts-qatar")
    public void receiveFactsFromKafkaQatar(Record<UUID, String> message) {
        logger.info("Received  qatar facts for request "+ message.key().toString());
        try{
            requestService.saveFacts(message.key(), message.value());
        }catch(Exception e){
            logger.error("Error wile save qatar  Facts "+ e.getMessage() +": \n "+ message );
        }
    }

    @Transactional
    @Incoming("price-engine-failure-responses-qatar")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveFailureResponseToKafkaQatar(Record<UUID, String> message) {
        logger.info("Received failure  qatar response from kafka ");
        try{
            ResponseMetaData responseMetaData = objectMapper.readValue(message.value(), ResponseMetaData.class);
            requestService.saveResponse(responseMetaData);
        }catch(Exception e){
            logger.error("Error wile save  qatar Response "+ e.getMessage() +": \n "+ message );
//            logRepository.save(new PriceLog(e.getMessage(), Timestamp.from(Instant.now()), message.key()));
        }
    }

    @Transactional
    @Incoming("price-engine-logs-qatar")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveLogToKafkaQatar(Record<UUID, String> message) {
        logger.info("Received qatar  log from kafka ");
        try{
            PriceLog priceLog = objectMapper.readValue(message.value(), PriceLog.class);
            var isSaved = logRepository.save(priceLog);
            if (isSaved){
                logger.info("price  qatar log saved for request " + message.key());
            }
            else{
                logger.error("price  qatar log not saved for request " + message.key());
            }
        }catch(Exception e){
            logger.error("Error wile save qatar  Log "+ e.getMessage() +": \n "+ message );
        }
    }

//    KSA

    @Transactional
    @Incoming("price-engine-responses-ksa")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveResponseToKafkaKSA(Record<UUID, String> message) {
        logger.info("Received ksa response from kafka ");
        try{
            ResponseMetaData responseMetaData = objectMapper.readValue(message.value(), ResponseMetaData.class);
            requestService.saveResponse(responseMetaData);
        }catch(Exception e){
            logger.error("Error wile save  ksa Response "+ e.getMessage() +": \n "+ message );
//            logRepository.save(new PriceLog(e.getMessage(), Timestamp.from(Instant.now()), message.key()));

        }
    }

    @Incoming("price-engine-facts-ksa")
    public void receiveFactsFromKafkaKSA(Record<UUID, String> message) {
        logger.info("Received  ksa facts for request "+ message.key().toString());
        try{
            requestService.saveFacts(message.key(), message.value());
        }catch(Exception e){
            logger.error("Error wile save ksa  Facts "+ e.getMessage() +": \n "+ message );
        }
    }

    @Transactional
    @Incoming("price-engine-failure-responses-ksa")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveFailureResponseToKafkaKSA(Record<UUID, String> message) {
        logger.info("Received failure  ksa response from kafka ");
        try{
            ResponseMetaData responseMetaData = objectMapper.readValue(message.value(), ResponseMetaData.class);
            requestService.saveResponse(responseMetaData);
        }catch(Exception e){
            logger.error("Error wile save  ksa Response "+ e.getMessage() +": \n "+ message );
//            logRepository.save(new PriceLog(e.getMessage(), Timestamp.from(Instant.now()), message.key()));
        }
    }

    @Transactional
    @Incoming("price-engine-logs-ksa")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveLogToKafkaKSA(Record<UUID, String> message) {
        logger.info("Received  ksa log from kafka ");
        try{
            PriceLog priceLog = objectMapper.readValue(message.value(), PriceLog.class);
            var isSaved = logRepository.save(priceLog);
            if (isSaved){
                logger.info("price ksa  log saved for request " + message.key());
            }
            else{
                logger.error("price ksa  log not saved for request " + message.key());
            }
        }catch(Exception e){
            logger.error("Error wile save ksa  Log "+ e.getMessage() +": \n "+ message );
        }
    }

}