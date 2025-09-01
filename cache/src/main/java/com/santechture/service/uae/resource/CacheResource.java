package com.santechture.service.uae.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.service.KafkaMessagingService;
import com.santechture.service.uae.mssql.*;
import com.santechture.service.uae.redis.CacheData;
import io.quarkus.redis.client.RedisClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("api/v1/cache")
public class CacheResource {
    final static Logger logger = LoggerFactory.getLogger(CacheResource.class);
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
    @Inject
    ObjectMapper objectMapper;
    @Inject
    RedisClient redisClient;
    @Inject
    SPCContractDBClient spcContractClient;
    @Inject
    FacilityDBClient facilityDBClient;
    @Inject
    ContractsDBClient contractsDBClient;
    @Inject
    CusPriceListItemDBClient cusPriceListItemDBClient;
    @Inject
    DhaDrgCostPreActivityDBClient dhaDrgCostPreActivityDBClient;
    @Inject
    DhaDrgDBClient dhaDrgDBClient;
    @Inject
    DhaDrgExcludedCptDBClient dhaDrgExcludedCptDBClient;
    @Inject
    DhaDrgHighCostDBClient dhaDrgHighCostDBClient;
    @Inject
    DrugPricesDBClient drugPricesDBClient;
    @Inject
    FacilityCliniciansDBClient facilityCliniciansDBClient;
    @Inject
    SpcCodeFactorsDBClient spcCodeFactorsDBClient;
    @Inject
    SpcGroupFactorsDBClient spcGroupFactorsDBClient;
    @Inject
    SPCMasterListsDBClient masterListsDBClient;
    @Inject
    SPCMasterListsItemsDBClient masterListsItemsDBClient;
    @Inject
    CodeGroupsDBClient codeGroupsDBClient;
    @Inject
    CacheData cacheData;
    @Inject
    KafkaMessagingService kafkaMessagingService;

    @GET
    public Response startCache(){
        cacheData.saveContractsToRedis();
        kafkaMessagingService.sendUpdateModelKeyToKafka("all");
        return Response.ok().build();
    }
    final static  Logger log = LoggerFactory.getLogger(CacheResource.class);
    private List<DBClientBase> dbClients;
    @GET
    @Path("update/{model}")
    public Response cache(@PathParam("model") String model) {
        log.info("received update caching request");
        cacheData.saveContractsToRedis(model);
        kafkaMessagingService.sendUpdateModelKeyToKafka(model);
        return Response.ok().build();
    }

    @PostConstruct
    private void init() {
        log.info("Starting Redis contract cache");
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        dbClients = List.of(
                spcContractClient
                ,facilityDBClient
                ,contractsDBClient
                ,cusPriceListItemDBClient
                ,dhaDrgCostPreActivityDBClient
                ,dhaDrgDBClient
                ,dhaDrgExcludedCptDBClient
                ,dhaDrgHighCostDBClient
                ,drugPricesDBClient
                ,facilityCliniciansDBClient
                ,spcCodeFactorsDBClient
                ,spcGroupFactorsDBClient
                ,masterListsDBClient
                ,masterListsItemsDBClient
                ,codeGroupsDBClient
        );
    }


    @GET
    @Path("info")
    public Response getCacheStatus(){
       var cacheInfo = redisClient.hmget(List.of(allInfoKey, lastUpdateField, statusField, progressStatusField));
        if (cacheInfo == null){
           return Response.serverError().build();
        }
        Map<String, String> resultMap = new HashMap<>();
        Map<String, Map> result = new HashMap<>();
        List<String> fields = List.of(lastUpdateField, statusField, progressStatusField);

        for (int i = 0; i < cacheInfo.size(); i++) {
            resultMap.put(fields.get(i), cacheInfo.get(i).toString());
        }
        result.put("summary", resultMap);
        for (DBClientBase dbClient : dbClients) {
            cacheInfo = redisClient.hmget(
                    List.of(generalInfoKey+":"+dbClient.getModel(),
                            lastUpdateField,
                            statusField,
                            progressStatusField));
            result.put(dbClient.getModel(), Map.of(
                    fields.get(0), cacheInfo.get(0).toString(),
                    fields.get(1), cacheInfo.get(1).toString(),
                    fields.get(2), cacheInfo.get(2).toString()
            ));
        }
        return Response.accepted(result).build();
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



}
