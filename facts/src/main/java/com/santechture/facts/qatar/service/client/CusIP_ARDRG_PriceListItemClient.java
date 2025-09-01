package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CusIP_ARDRGPriceListItem;
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
public class CusIP_ARDRG_PriceListItemClient extends ClientBase {
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
                    if (cusContract.getInpatientArDrgPriceListId() != null) {
                        lst.add(String.format("%s:%s:%s", "CusIP_ARDRGPriceListItem", cusContract.getInpatientArDrgPriceListId(), activity.getCode()));
                    }
                }
            }
        }
        return lst;
    }


    @Override
    @WithSpan("Add Facts For CusPriceListItemIPARDRG")
    public Uni<Void> addFacts(Data data) {
        data.setCusIP_ARDRGPriceListItems(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if(!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, CusIP_ARDRGPriceListItem.class);
                    data.getCusIP_ARDRGPriceListItems().add(obj);
                }

            }
        }));

    }

}
