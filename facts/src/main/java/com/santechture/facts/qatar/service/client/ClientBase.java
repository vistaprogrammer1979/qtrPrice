package com.santechture.facts.qatar.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.facts.qatar.service.cache.RedisService;
import com.santechture.request.Data;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

public abstract class ClientBase {
    @Inject
    Logger logger;
    @Inject
    RedisService redisService;
    @Inject
    ObjectMapper objectMapper;


    public abstract List<String> getKeys(Data data);
//    public abstract List<Map<String,String>> fetchData(Data data);
    public abstract Uni<Void> addFacts(Data data);

//    public void addFacts(Data data) {
//        var fetchedData = this.fetchData(data);
//        this.convertValues(data, fetchedData);
//    }

}