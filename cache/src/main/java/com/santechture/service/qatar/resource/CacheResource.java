package com.santechture.service.qatar.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.pojo.CacheProgressStatusEnum;
import com.santechture.pojo.CacheStatusEnum;
import com.santechture.service.KafkaMessagingService;
import com.santechture.service.qatar.QatarInitializing;
import com.santechture.service.qatar.mssql.*;
import com.santechture.service.qatar.redis.CacheData;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.RedisClientName;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ApplicationScoped
@Path("api/v1/qatar/cache/")
public class CacheResource {
    final static Logger logger = LoggerFactory.getLogger(CacheResource.class);
    final static Logger log = LoggerFactory.getLogger(CacheResource.class);
    @ConfigProperty(name = "price.cache.redis.all.info.key")
    String allInfoKey;
    @ConfigProperty(name = "price.cache.redis.last.update.field")
    String lastUpdateField;
    @ConfigProperty(name = "price.cache.redis.status.field")
    String statusField;
    @ConfigProperty(name = "price.cache.redis.progress.status.field")
    String progressStatusField;
    @ConfigProperty(name = "price.cache.redis.general.info.key")
    String generalInfoKey;
    @ConfigProperty(name = "price.cache.redis.progress.size.field")
    String sizeKey;
    @ConfigProperty(name = "price.cache.redis.progress.records.field")
    String recordAmountKey;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    @RedisClientName("qatar")
    RedisClient redisClient;

    @Inject
    CacheData cacheData;
    @Inject
    KafkaMessagingService kafkaMessagingService;
    private List<DBClientBase> dbClients;
    @Inject
    private QatarInitializing qatarInitializing;

    @POST
    public Response startCache() {
        cacheData.saveContractsToRedis();
        kafkaMessagingService.sendUpdateModelKeyToKafkaQatar("all");
        return Response.ok().build();
    }

    @POST
    @Path("update/{model}")
    public Response cache(@PathParam("model") String model) {
        log.info("received update caching request");
        cacheData.saveContractsToRedis(model);
        kafkaMessagingService.sendUpdateModelKeyToKafkaQatar(model);
        return Response.ok().build();
    }

    @PostConstruct
    private void init() {
        log.info("Starting Redis contract cache");
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        dbClients = qatarInitializing.getDbClients();

    }


    @GET
    @Path("info")
    public Response getCacheStatus() {
        var cacheInfo = redisClient.hmget(List.of(allInfoKey, lastUpdateField, statusField, progressStatusField));
        if (cacheInfo == null) {
            var response = new GeneralResponse(404, "No cache data", 0, List.of(), UUID.randomUUID().toString());
            return Response.accepted(response).build();
        }
        Map<String, String> resultMap = new HashMap<>();
        List result = new ArrayList();
        List<String> fields = List.of(lastUpdateField, statusField, progressStatusField, sizeKey, recordAmountKey);
        List<String> defaultValues = List.of("", CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.Failed.name(), "0", "0");
        resultMap.put("title", "All");
        for (int i = 0; i < cacheInfo.size(); i++) {
            resultMap.put(fields.get(i), cacheInfo.get(i) != null ? cacheInfo.get(i).toString() : defaultValues.get(i));
        }
//        result.add( resultMap);
        for (DBClientBase dbClient : dbClients) {
            cacheInfo = redisClient.hmget(List.of(generalInfoKey + ":" + dbClient.getModel(), lastUpdateField, statusField, progressStatusField, sizeKey, recordAmountKey));
            var map = new HashMap<String, String>();
            map.put("title", dbClient.getModel());
            map.put(fields.get(0), cacheInfo.get(0) != null ? cacheInfo.get(0).toString() : defaultValues.get(0));
            map.put(fields.get(1), cacheInfo.get(1) != null ? cacheInfo.get(1).toString() : defaultValues.get(1));
            map.put(fields.get(2), cacheInfo.get(2) != null ? cacheInfo.get(2).toString() : defaultValues.get(2));
            map.put(fields.get(3), cacheInfo.get(3) != null ? cacheInfo.get(3).toString() : defaultValues.get(3));
            map.put(fields.get(4), cacheInfo.get(4) != null ? cacheInfo.get(4).toString() : defaultValues.get(4));
            result.add(map);
        }
        var response = new GeneralResponse(200, "", result.size(), result, UUID.randomUUID().toString());
        return Response.ok(response).build();
//
//        if (status.get(0).toString().equals("in-progress")){
//            return 1;
//        }
//        if (status.get(0).toString().equals("finished")){
//            var lastUpdate = redisClient.hmget(List.of(String.format("cached-price-facts:%s",tableName), "lastUpdate")).get(0);
//            if (lastUpdate != null && lastUpdate.get(0).toString().equals("in-progress")){}
//            return 1;
//        }
//        return 0;
//        return Response.ok().build();
    }

    @GET
    @Path("/calculate-size")
    public Response getSize() {
        var mapSizes = new HashMap<>();
        for (DBClientBase dbClient : dbClients) {
            log.info(dbClient.getModel());
            mapSizes.put(dbClient.getModel(), dbClient.calculateSize());
        }
        return Response.ok(mapSizes).build();
    }


}
