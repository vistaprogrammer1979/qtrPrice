package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CusIP_SUBPriceListItem;
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
public class CusIP_SUB_PriceListItemClient extends ClientBase {
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
        var encounter = claim.getEncounter() != null && !claim.getEncounter().isEmpty() ? claim.getEncounter().get(0) : null;
        var lst = new ArrayList<String>();
        if (encounter != null && encounter.getCareType() != null) {

            for (var cusContract : cusContracts) {
                if (cusContract.getInpatientSubacutePriceListId() != null) {
                    lst.add(String.format("%s:%s:%s", "CusIP_SUBPriceListItem", cusContract.getInpatientSubacutePriceListId(), encounter.getCareType()));
                }
            }

        }
        return lst;
    }


    @Override
    @WithSpan("Add Facts For CusPriceListItem_IP_SUB")
    public Uni<Void> addFacts(Data data) {
        data.setCusIPSubPriceListItems(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if(!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, CusIP_SUBPriceListItem.class);
                    data.getCusIPSubPriceListItems().add(obj);
                }
            }
        }));

    }

}
