package com.santechture.facts.qatar.service;

import com.santechture.facts.qatar.service.InitializeService;
import com.santechture.facts.entity.PriceLog;
import com.santechture.request.Facts;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class KafkaMessagingService {
    final static Logger log  = LoggerFactory.getLogger(KafkaMessagingService.class);

    @Channel("price-engine-fact-qatar")
    Emitter<Record<UUID, String>> priceResponseEmitter;
    @Channel("price-engine-logs-qatar")
    Emitter<Record<UUID, String>> priceLogEmitter;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    ContainerRequestContext requestContext;
    @Inject
    InitializeService initializeService;

    public void sendFactToKafka(UUID uuid, Facts facts) {
        log.info("Send facts to kafka " + uuid);

        try{
            priceResponseEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(facts)));

        }catch (Exception e){
            log.error("Couldn't send facts to kafka ", e);
        }
    }

    public void sendLogToKafka(UUID uuid, PriceLog priceLog) {

        try{
            var isSaved = Boolean.getBoolean(String.valueOf(requestContext.getHeaders().getFirst("X-Is-Request-Saved")));
            if (isSaved) {
                priceLogEmitter.send(Record.of(uuid, objectMapper.writeValueAsString(priceLog)));
            }else{
                log.info("Request {} didn't saved into DB " , uuid);
            }
        }catch (Exception e){
            log.error(String.format("Couldn't send log with request id %s to kafka. ",uuid), e);
        }
    }

//    public void sendFactInstanceToKafka(UUID uuid, String instance) {
//        try{
//            var isSaved = Boolean.getBoolean(String.valueOf(requestContext.getHeaders().getFirst("X-Is-Request-Saved")));
//            if (isSaved) {
//                priceInstanceEmitter.send(Record.of(uuid, instance));
//            }else{
//                log.info("Couldn't save Fact instance because request {} didn't saved into DB " , uuid);
//            }
//        }catch (Exception e){
//            log.error(String.format("Couldn't send fact instance  with request id %s to kafka. ",uuid), e);
//        }
//    }
//    public void sendFailureResponseToKafka(UUID uuid, String response) {
//        try{
//
//            priceFailureResponseEmitter.send(Record.of(uuid, response));
//        }catch (Exception e){
//            log.error("Couldn't send failure response to kafka ", e);
//        }
//    }
    @Incoming("price-cache-management-qatar")
    public void receiveUpdateEventFromKafka(Record<String, String> message) {
        log.info("Received facts for request {} - Qatar", message.value());
        try{
            switch (message.value()){
                case "CodeGroup" -> {
                    initializeService.updateClinical();
                    initializeService.updateGeneral();
                    initializeService.updateInvestigation();
                    initializeService.updateLabSupplies();
                    initializeService.updateRoomAndBed();
                    initializeService.updateOther();
                    initializeService.updatePharmaceticalsAndSupplies();
                    initializeService.updateOnc();
                    initializeService.updateStaffHealthInsuranceChargeType();
                    initializeService.updatePackagedServices();

                }
//                case "SPCContract" -> initializeService.updateSPCContract();
//                case "SPCGroupFactor" -> initializeService.updateSPCGroupFactor();
                case "MasterPriceList" -> initializeService.updateMasterPriceList();
                case "CusContract" -> initializeService.updateCusContract();
//                case "SPCCodeFactor" -> initializeService.updateSPCCodeFactor();
                default -> initializeService.updateRedisKeys();
            }
        }catch(Exception e){
            log.error("Error wile update redis keys"+ e.getMessage() +": \n "+ message );
        }
    }
}