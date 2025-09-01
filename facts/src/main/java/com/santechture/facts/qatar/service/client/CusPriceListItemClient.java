package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CusOP_QOCSPriceListItem;
import com.santechture.facts.qatar.service.cache.RedisService;
import com.santechture.request.Data;
import com.santechture.CusPriceListItem;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.*;

@Singleton
public class CusPriceListItemClient extends ClientBase {
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
        if (activities != null){
            for (var activity : activities) {
                for (var cusContract : cusContracts) {
                    lst.add(
                            String.format("%s:%s:%s", "CusPriceListItem", cusContract.getPriceListId(), activity.getCode())
                    );
                }
            }
        }
        return lst;
    }


    @Override
    @WithSpan("Add Facts For CusPriceListItem")
    public Uni<Void> addFacts(Data data) {
        data.setCusPriceListItemsList(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if(!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, CusPriceListItem.class);
                    data.getCusPriceListItemsList().add(obj);              }
            }
        }));
    }

}
