package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CusRadiology_ACHIPriceListItem;
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
public class CusRadiology_ACHI_PriceListItemClient extends ClientBase {
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
    }

    @Override
    public List<String> getKeys(Data data) {
        var claim = data.getClaim();
        var cusContracts = data.getCusContractList();
        var activities = claim.getActivity();
        var lst = new ArrayList<String>();
        if (activities != null) {
            for (var activity : activities) {
                for (var cusContract : cusContracts) {
                    if (cusContract.getRadiologyAchiPriceListId() != null) {
                        lst.add(String.format("%s:%s:%s", "CusRadiology_ACHIPriceListItem", cusContract.getRadiologyAchiPriceListId(), activity.getCode()));
                    }
                }
            }
        }
        return lst;
    }


    @Override
    @WithSpan("Add Facts For CusPriceListItem_Radiology_ACHI")
    public Uni<Void> addFacts(Data data) {
        data.setCusRadiologyAchiPriceListItems(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if(!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, CusRadiology_ACHIPriceListItem.class);
                    data.getCusRadiologyAchiPriceListItems().add(obj);
                }
            }
        }));
    }

}
