package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CusContract;
import com.santechture.CusPriceList;
import com.santechture.facts.qatar.service.cache.RedisService;
import com.santechture.request.Data;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class CusPriceListClient extends ClientBase {
    @Inject
    Logger logger;
    @Inject
    RedisService redisService;
    @Inject
    ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public List<String> getKeys(Data data) {
        var result = new ArrayList<String>();
        if (data.getCusContractList() != null) {
            for (CusContract contract : data.getCusContractList()) {
                if (contract.getPriceListId() != null) {
                    result.add(String.format("%s:%s", "CusPriceList", contract.getPriceListId()));
                }
                if (contract.getInpatientArDrgPriceListId() != null) {
                    result.add(String.format("%s:%s", "CusPriceList", contract.getInpatientArDrgPriceListId()));
                }
                if (contract.getOutpatientQocsPriceListId() != null) {
                    result.add(String.format("%s:%s", "CusPriceList", contract.getOutpatientQocsPriceListId()));
                }
                if (contract.getDentistryAsdsgPriceListId() != null) {
                    result.add(String.format("%s:%s", "CusPriceList", contract.getDentistryAsdsgPriceListId()));
                }
                if (contract.getRadiologyAchiPriceListId() != null) {
                    result.add(String.format("%s:%s", "CusPriceList", contract.getRadiologyAchiPriceListId()));
                }
                if (contract.getEmergencyUrgPriceListId() != null) {
                    result.add(String.format("%s:%s", "CusPriceList", contract.getEmergencyUrgPriceListId()));
                }
                if (contract.getInpatientSubacutePriceListId() != null) {
                    result.add(String.format("%s:%s", "CusPriceList", contract.getInpatientSubacutePriceListId()));
                }
            }
        }
        return result;
    }


    @WithSpan("Add Facts For CusPriceList")
    @Override
    public Uni<Void> addFacts(Data data) {
        data.setCusPriceList(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if(!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, CusPriceList.class);
                    data.getCusPriceList().add(obj);
                }

            }
        }));
    }

}
