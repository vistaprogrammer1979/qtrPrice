package com.santechture.facts.qatar.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.SPCContract;
import com.santechture.request.Claim;
import com.santechture.request.Data;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.RedisClientName;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.http.ConnectionPoolTooBusyException;
import io.vertx.redis.client.Response;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Singleton
public class RedisService {
    @Inject
    @RedisClientName("qatar")
    RedisClient redisClient;
    @Inject
    @RedisClientName("qatar")
    ReactiveRedisDataSource redisDS;
    @Inject
    Logger log;

    @Inject
    ObjectMapper objectMapper;

    private ReactiveValueCommands<String, String> redis;
    @PostConstruct
    private void init() {
        log.info("RedisService PostConstruct");
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.redis = redisDS.value(String.class);
    }
    public ArrayList<String> getGroupsNames(String type){
        try{
            return new ArrayList<String>(redisClient.smembers(type + "_" + "Groups").stream().map(Response::toString).toList());
        } catch (Exception e){
            return new ArrayList<>();
        }

    }

    public List<String> getGroups(String code){
        List<String> groupsKeys = new ArrayList<>();
        var response = redisClient.smembers("Groups");
        if (response!= null && response.size() > 0) {
            response.stream().forEach(r -> groupsKeys.add(r.toString()));
            return groupsKeys.stream().map(g -> "CodeGroup:"+g+":"+code).toList();
        }
        return groupsKeys;

    }
    public List<String> getKeys(String pattern){
        var keys = redisClient.keys(pattern);
            if (keys!= null) {
//                keys.forEach(key -> log.info("Key:"+key));
                return keys.stream().map(Response::toString).toList();
            }
            return new ArrayList<>();
    }

    public List<Map<String, String>> getDataOld(List<String> keys) {
        List<Map<String,String>> list = new ArrayList<>();
        for (String key : keys) {
            var response = redisClient.hgetall(key);
            if (response != null && response.size() > 0) {
                Map<String, String> map = new HashMap<>();
                for (String k: response.getKeys()){
                    map.put(k, response.get(k).toString());
                }
                list.add(map);
            }
        }
        return list;
    }
    public Uni<List<Map<String, String>>> getData(List<String> keys) {
        // Handle empty keys list
        if (keys.isEmpty()) {
            return Uni.createFrom().item(Collections.emptyList());
        }

        // Get the ReactiveHashCommands instance
        ReactiveHashCommands<String, String, String> hashCommands = redisDS.hash(String.class);

        // Create a list of Uni<Map<String, String>> for each key
        List<Uni<Map<String, String>>> uniList = keys.stream()
                .map(key -> {
                    // The base operation
                    Uni<Map<String, String>> operation = hashCommands.hgetall(key)
                            .onItem().ifNotNull().transform(map -> map)
                            .onItem().ifNull().continueWith(Map.of());

                    // Special handling for ConnectionPoolTooBusyException
                    Uni<Map<String, String>> persistentOperation = operation
                            .onFailure(ConnectionPoolTooBusyException.class)
                            .retry()
                            .withBackOff(Duration.ofMillis(100), Duration.ofSeconds(1))
//                            .withBackOff(Duration.ofMillis(200), Duration.ofSeconds(30))
                            .indefinitely();

                    // Handle all other exceptions
                    return persistentOperation
                            .onFailure(f -> !(f instanceof ConnectionPoolTooBusyException))
                            .recoverWithUni(failure -> {
                                // For non-pool-busy exceptions, fail immediately
                                return Uni.createFrom().failure(failure);
                            });
                })
                .collect(Collectors.toList());

        // Combine all results
        return Uni.combine().all().unis(uniList)
                .with(list -> list.stream()
                        .map(item -> (Map<String, String>) item)
                        .collect(Collectors.toList()));
    }


    private boolean isPoolExhaustion(Throwable throwable) {
        return throwable.getMessage() != null &&
                throwable.getMessage().contains("Connection pool reached max wait queue size");
    }
    private boolean isConnectionPoolException(Throwable throwable) {
        // Check for various pool-related exception messages
        return throwable.getMessage() != null && (
                throwable.getMessage().contains("Connection pool reached max wait queue size") ||
                        throwable.getMessage().contains("Pool exhausted") ||
                        throwable.getMessage().contains("Unable to acquire connection from pool") ||
                        throwable.getMessage().contains("Connection pool timeout"));
    }

    public SPCContract addSpcContract(Claim claim){
        String packageName;
        if (claim.getPatient() != null && claim.getPatient().getPatientInsurance()!= null  && claim.getPatient().getPatientInsurance().getPackageName() != null){
            packageName = claim.getPatient().getPatientInsurance().getPackageName();
        }
        else {
            packageName = "_";
        }
        var response = redisClient.hgetall(String.format("SPCContract:%s:%s:%s",
                claim.getProviderID()!=null?claim.getProviderID():"_",
                claim.getReceiverID()!=null?claim.getReceiverID():"_",
                packageName));
        if (response == null || response.size() == 0) {
            return null; // or handle empty response as needed
        }        SPCContract spcContract = null;
        try{
            Map<String, String> map = new HashMap<>();

//            response.getKeys().stream().map(k -> map.put(k, response.get(k).toString()));
            for (String k: response.getKeys()){
//                log.info(String.format("%s:%s",k, response.get(k)));
                map.put(k, response.get(k).toString());
            }
//            log.info(map.toString());


            spcContract = objectMapper.convertValue(map, SPCContract.class);
//            log.info(String.format("spcContract: %s", spcContract));
//            log.info(String.format("map: %s", map));
        }catch (Exception e){
            log.error(e);
        }

        return spcContract;
//        return mapper.convertValue(result, SPCContract.class);
//        result.forEach(r -> log.debugf("Add SPC contract to Redis: %s", r));
//        return null;
    }
    @WithSpan("Save full request into redis")
//    public void saveRequest(String id, Data data){
//        try {
//            redisClient.set(List.of(String.format("PriceCloud:FullRequest:%s",id), objectMapper.writeValueAsString(data), "EX", "3600"));
//        } catch (JsonProcessingException e) {
//            log.warn("Error saving full request in redis ", e);
//        }
//    }
    public void saveRequest(String id, Data data) {
        try {
            String key = String.format("PriceCloud:FullRequest:%s", id);
            String value = objectMapper.writeValueAsString(data);

            redis.setex(key, 3600, value)
                    .onFailure(ConnectionPoolTooBusyException.class)
                    .retry()
                    .withBackOff(Duration.ofMillis(10), Duration.ofMillis(100))
                    .indefinitely()
                    .onFailure().invoke(e -> log.error("Unhandled exception saving to Redis", e))

                    .subscribe().with(
                            success -> {},
                            failure -> log.warn("Final failure despite retries", failure)
                    );

        } catch (JsonProcessingException e) {
            log.warn("JSON serialization error", e);
        }
    }

    public boolean isValid() {
        var status = redisClient.hmget(List.of("price-cache-info:all", "status"));
        if (status == null || status.get(0) == null ) {
            return false;
        }
        return status.get(0).toString().equals("valid");
    }


}
