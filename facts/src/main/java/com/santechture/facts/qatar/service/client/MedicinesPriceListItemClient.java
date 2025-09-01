package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CusRadiology_ACHIPriceListItem;
import com.santechture.MedicinesPriceListItem;
import com.santechture.facts.qatar.service.cache.RedisService;
import com.santechture.request.Activity;
import com.santechture.request.CodeType;
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
public class MedicinesPriceListItemClient extends ClientBase {
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
            activities = activities.stream().filter(a -> CodeType.Medicine.name().equals(a.getType().name())).toList();
            for (var activity : activities) {
                lst.add(String.format("%s:%s", "MedicinesPriceListItem", activity.getCode()));
            }
        }
        return lst;
    }


    @Override
    @WithSpan("Add Facts For MedicinesPriceListItem")
    public Uni<Void> addFacts(Data data) {
        data.setMedicinesPriceListItems(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if(!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, MedicinesPriceListItem.class);
                    data.getMedicinesPriceListItems().add(obj);
                }
            }
        }));
    }

}
