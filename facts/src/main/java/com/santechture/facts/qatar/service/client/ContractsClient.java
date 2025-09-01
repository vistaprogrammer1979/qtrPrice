package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CodeGroup;
import com.santechture.CusContract;
import com.santechture.facts.qatar.service.InitializeService;
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
public class ContractsClient extends ClientBase {
    @Inject
    Logger logger;
    @Inject
    RedisService redisService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    InitializeService initializeService;

    @PostConstruct
    private void init() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public List<String> getKeys(Data data) {
        var claim = data.getClaim();
        var keys = new ArrayList<String>();
        if (claim.getContract() == null) {
            return keys;
        }
        var cusContractKeyList = initializeService.getCusContractKeyList();
        return cusContractKeyList.stream().filter(cusContractKey -> cusContractKey.startsWith(String.format("CusContract:%s:%s", claim.getReceiverID(), claim.getProviderID()))).toList();
    }


    @WithSpan("Add Facts For Contracts")
    @Override
    public Uni<Void> addFacts(Data data) {
        data.setCusContractList(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if (!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, CusContract.class);
                    data.getCusContractList().add(obj);
                }
            }
        }));
    }

}
