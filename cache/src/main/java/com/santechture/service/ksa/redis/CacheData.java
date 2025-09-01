package com.santechture.service.ksa.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CodeGroup;
import com.santechture.KSAInitializing;
import com.santechture.pojo.CacheProgressStatusEnum;
import com.santechture.pojo.CacheStatusEnum;
import com.santechture.request.CodeType;
import com.santechture.service.ksa.mssql.*;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.RedisClientName;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
//@Startup
public class CacheData {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
    @Inject
    Logger log;
    @Inject
    @RedisClientName("ksa")
    RedisClient redisClient;
    @Inject
    ReactiveRedisClient reactiveRedisClient;
    @Inject
    ObjectMapper objectMapper;
   @Inject
    KSAInitializing ksaInitializing;

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
    private List<DBClientBase> dbClients;

//    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @PostConstruct
    private void init() {
        log.info("Starting Redis contract cache");
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        dbClients = ksaInitializing.getDbClients();
    }

    //    @Scheduled(every="30m")
    public void saveContractsToRedis() {
        log.info("******************************************* Starting Redis cache - KSA ********************************************************");
        addToRedis();
        log.info("******************************************* Finish Redis cache - KSA ********************************************************");
    }

    public void saveContractsToRedis(String model) {
        this.updateStatus(allInfoKey, CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(),"0","0");
        this.insertIntoRedis(model);
        this.updateStatus(allInfoKey, CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), "0", "0");

    }

    private void addToRedis() {
//        var status = this.isCached();
//        if (status == 0){
        this.updateStatus(allInfoKey, CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), "0", "0");
        this.insertIntoRedis();
        this.updateStatus(allInfoKey, CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(),"0", "0");
//            this.updateStatus("finished");
//        }
//        else {
//            log.info("No need to cache SPCContract to redis");
//        }
    }

    private boolean checkStatus(String infoKey) {
        var oldValue = redisClient.keys(infoKey);
        if (oldValue == null) {
            return false;
        }


        redisClient.hmget(
                List.of(
                        infoKey,
                        lastUpdateField,
                        statusField,
                        progressStatusField//,
//                        checksum.keySet().stream().toList().get(0),
//                        checksum.values().stream().toList().get(0)
                )
        );

        return true;
    }

    private void updateStatus(String infoKey, String status, String progressStatus,String recordAmount, String size) {
        try {

            var oldValue = redisClient.keys(infoKey);
            if (oldValue != null) {
                redisClient.hset(List.of(
                                infoKey,
                                lastUpdateField, FORMATTER.format(Timestamp.from(Instant.now())),
                                statusField, status,
                                progressStatusField, progressStatus,
                                recordAmountKey, recordAmount,
                                sizeKey,size
                        )
                );
            } else {
                redisClient.hmset(List.of(
                                infoKey,
                                lastUpdateField, FORMATTER.format(Timestamp.from(Instant.now())),
                                statusField, status,
                                progressStatusField, progressStatus,
                                recordAmountKey, recordAmount,
                                sizeKey,size
                        )
                );
            }
        } catch (Exception e) {
            log.error(String.format("Error updating redis status for model %s", infoKey), e);
            Arrays.stream(e.getStackTrace()).forEach(s -> log.error(s.toString()));
        }


    }

    private void insertIntoRedis() {
        for (DBClientBase dbClient : dbClients) {
            var model = dbClient.getModel();
            log.info(model);
            var checksum = dbClient.calCheckSum();
            try {
                updateStatus(String.format(generalInfoKey + ":" + model),
                        CacheStatusEnum.notValid.name(),
                        CacheProgressStatusEnum.InProgress.name(),
                        "0","0"
                );
                var data = dbClient.getData();
                for (Object object : data) {
                    var lst = this.toList(dbClient, object);
                    if (lst.size() > 1) {
                        try {

                            redisClient.hmset(lst);
//                            log.info(lst.toString());
                        } catch (Exception e) {
                            log.info(lst);
                            log.error(e);
                        }
                    }
                    lst = this.toList2(dbClient, object);
                    if (lst != null && lst.size() > 1) {
                        try {

                            redisClient.hmset(lst);
                        } catch (Exception e) {
                            log.info(lst);
                            log.error(e);
                        }
                    }

                }
                if (dbClient.getClass().getSimpleName().equals(CodeGroupsDBClient.class.getSimpleName())) {
                    var codeGroups = new ArrayList<CodeGroup>(data.stream().map(d -> (CodeGroup) d).toList());
                    List<String> clinical = new ArrayList<>();
                    clinical.add("Clinical_Groups");
                    clinical.addAll(codeGroups.stream().filter(g -> CodeType.Clinical.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> general = new ArrayList<>();
                    general.add("General_Groups");
                    general.addAll(codeGroups.stream().filter(g -> CodeType.General.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> INVESTIGATION = new ArrayList<>();
                    INVESTIGATION.add("Investigation_Groups");
                    INVESTIGATION.addAll(codeGroups.stream().filter(g -> CodeType.INVESTIGATION.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> labSupplies = new ArrayList<>();
                    labSupplies.add("LabSupplies_Groups");
                    labSupplies.addAll(codeGroups.stream().filter(g -> CodeType.LabSupplies.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> roomAndBed = new ArrayList<>();
                    roomAndBed.add("RoomAndBed_Groups");
                    roomAndBed.addAll(codeGroups.stream().filter(g -> CodeType.RoomAndBed.equals(g.getType())).map(CodeGroup::getName).toList());

                    List<String> other = new ArrayList<>();
                    other.add("Other_Groups");
                    other.addAll(codeGroups.stream().filter(g -> CodeType.Other.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> pharmaceticalsAndSupplies = new ArrayList<>();
                    pharmaceticalsAndSupplies.add("PharmaceticalsAndSupplies_Groups");
                    pharmaceticalsAndSupplies.addAll(codeGroups.stream().filter(g -> CodeType.PharmaceticalsAndSupplies.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> onc = new ArrayList<>();
                    onc.add("Onc_Groups");
                    onc.addAll(codeGroups.stream().filter(g -> CodeType.onc.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> staff = new ArrayList<>();
                    staff.add("StaffHealthInsuranceChargeType_Groups");
                    staff.addAll(codeGroups.stream().filter(g -> CodeType.StaffHealthInsuranceCharge.equals(g.getType())).map(CodeGroup::getName).toList());
                    List<String> packaged = new ArrayList<>();
                    packaged.add("PackagedServices_Groups");
                    packaged.addAll(codeGroups.stream().filter(g -> CodeType.PackagedServices.equals(g.getType())).map(CodeGroup::getName).toList());

                    if (clinical.size() > 1) {
                        try {
                            redisClient.sadd(clinical);
                            log.info("Added clinical service groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting cpt groups into redis", e);
                        }
                    }
                    if (general.size() > 1) {
                        try{
                            redisClient.sadd(general);
                            log.info("Added general groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting hcpc groups into redis", e);
                        }
                    }
                    if (INVESTIGATION.size() > 1) {
                        try{
                            redisClient.sadd(INVESTIGATION);
                            log.info("Added INVESTIGATION groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting ICD groups into redis", e);
                        }
                    }
                    if (labSupplies.size() > 1) {
                        try{
                            redisClient.sadd(labSupplies);
                            log.info("Added labSupplies groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting labSupplies groups into redis", e);
                        }
                    }
                    if (roomAndBed.size() > 1) {
                        try{
                            redisClient.sadd(roomAndBed);
                            log.info("Added roomAndBed groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting roomAndBed groups into redis", e);
                        }
                    }
                    if (other.size() > 1) {
                        try{
                            redisClient.sadd(other);
                            log.info("Added other groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting other groups into redis", e);
                        }
                    }
                    if (pharmaceticalsAndSupplies.size() > 1) {
                        try{
                            redisClient.sadd(pharmaceticalsAndSupplies);
                            log.info("Added pharmaceticalsAndSupplies groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting pharmaceticalsAndSupplies groups into redis", e);
                        }
                    }
                    if (onc.size() > 1) {
                        try{
                            redisClient.sadd(onc);
                            log.info("Added onc groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting onc groups into redis", e);
                        }
                    }
                    if (staff.size() > 1) {
                        try{
                            redisClient.sadd(staff);
                            log.info("Added Staff Health Insurance ChargeType groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting Staff Health Insurance ChargeType groups into redis", e);
                        }
                    }
                    if (packaged.size() > 1) {
                        try{
                            redisClient.sadd(packaged);
                            log.info("Added packaged Services groups to cache");
                        }
                        catch (Exception e){
                            log.error("Error inserting Packaged Services groups into redis", e);
                        }
                    }

                }
                checksum = dbClient.calCheckSum();
                updateStatus(String.format(generalInfoKey + ":" + model),
                        CacheStatusEnum.valid.name(),
                        CacheProgressStatusEnum.Finished.name(),
                        Integer.toString(data.size()),Integer.toString(data.size()));
            } catch (Exception e) {
                log.error(String.format("Couldn't insert the model %s into redis", model), e);
                try {
                    updateStatus(String.format(generalInfoKey + ":" + model),
                            CacheStatusEnum.notValid.name(),
                            CacheProgressStatusEnum.Failed.name(),
                            "0","0");
                } finally {
                    log.info("******************************************* Failed model into redis");
                }
            }

        }
    }

    private void insertIntoRedis(String model) {
        for (DBClientBase dbClient : dbClients) {
            if (dbClient.getModel().equals(model)) {
                log.info(model);
                var checksum = dbClient.calCheckSum();
                try {
                    updateStatus(String.format(generalInfoKey + ":" + model),
                            CacheStatusEnum.notValid.name(),
                            CacheProgressStatusEnum.InProgress.name(),
                            "0","0"
                    );
                    var data = dbClient.getData();
                    for (Object object : data) {
                        var lst = this.toList(dbClient, object);
                        if (lst.size() > 1) {
                            try {
                                log.info(lst);

                                log.info(redisClient.hmset(lst));
                            } catch (Exception e) {
                                log.info(lst);
                                log.error(e);
                            }
                        }
//                        lst = this.toList2(dbClient, object);
//                        if (lst != null && lst.size() > 1) {
//                            try {
//
//                                redisClient.hmset(lst);
//                            } catch (Exception e) {
//                                log.info(lst);
//                                log.error(e);
//                            }
//                        }
                    }
                    log.info("Inserted " + model + " into redis");
                    if (dbClient.getClass().getSimpleName().equals(CodeGroupsDBClient.class.getSimpleName())) {
                        var codeGroups = new ArrayList<CodeGroup>(data.stream().map(d -> (CodeGroup) d).toList());
                        List<String> cpt = new ArrayList<>();
                        cpt.add("CPT_Groups");
                        cpt.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.CPT)).map(CodeGroup::getName).collect(Collectors.toSet()));
                        List<String> hcpcs = new ArrayList<>();
                        hcpcs.add("HCPCS_Groups");
                        hcpcs.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.HCPCS)).map(CodeGroup::getName).collect(Collectors.toSet()));
                        List<String> icd = new ArrayList<>();
                        icd.add("ICD_Groups");
                        icd.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.ICD9) || g.getType().equals(CodeType.ICD10))
                                .map(CodeGroup::getName).collect(Collectors.toSet()));
                        List<String> dental = new ArrayList<>();
                        dental.add("DENTAL_Groups");
                        dental.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.DENTAL)).map(CodeGroup::getName).collect(Collectors.toSet()));
                        List<String> service = new ArrayList<>();
                        service.add("SERVICE_Groups");
                        service.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.SERVICE)).map(CodeGroup::getName).toList());

                        if (cpt.size() > 1) {
                            try {
                                redisClient.sadd(cpt);
                                log.info("Added CPT groups to cache");
                            }
                            catch (Exception e){
                                log.error("Error inserting cpt groups into redis", e);
                            }
                        }
                        if (hcpcs.size() > 1) {
                            try{
                                redisClient.sadd(hcpcs);
                                log.info("Added HCPC groups to cache");
                            }
                            catch (Exception e){
                                log.error("Error inserting hcpc groups into redis", e);
                            }
                        }
                        if (icd.size() > 1) {
                            try{
                                redisClient.sadd(icd);
                                log.info("Added ICD groups to cache");
                            }
                            catch (Exception e){
                                log.error("Error inserting ICD groups into redis", e);
                            }
                        }
                        if (dental.size() > 1) {
                            try{
                                redisClient.sadd(dental);
                                log.info("Added DENTAL groups to cache");
                            }
                            catch (Exception e){
                                log.error("Error inserting dental groups into redis", e);
                            }
                        }
                        if (service.size() > 1) {
                            try{
                                redisClient.sadd(service);
                                log.info("Added SERVICE groups to cache");
                            }
                            catch (Exception e){
                                log.error("Error inserting SERVICE groups into redis", e);
                            }
                        }


                    }
//                    checksum = dbClient.calCheckSum();
                    updateStatus(String.format(generalInfoKey + ":" + model),
                            CacheStatusEnum.valid.name(),
                            CacheProgressStatusEnum.Finished.name(),
                            Integer.toString(data.size()), Integer.toString(data.size()));
                } catch (Exception e) {
                    log.error(String.format("Couldn't insert the model %s into redis", model), e);
                    try {
                        updateStatus(String.format(generalInfoKey + ":" + model),
                                CacheStatusEnum.notValid.name(),
                                CacheProgressStatusEnum.Failed.name(),
                                "0", "0");
                    } finally {
                        log.info("******************************************* Failed model into redis");
                    }
                }
            }
        }
    }

    private int isCached(String tableName) {
        var status = redisClient.hmget(List.of(String.format("cached-price-facts:%s", tableName), "status"));
        if (status == null) {
            return 0;
        }
        if (status.get(0).toString().equals("in-progress")) {
            return 1;
        }
        if (status.get(0).toString().equals("finished")) {
            var lastUpdate = redisClient.hmget(List.of(String.format("cached-price-facts:%s", tableName), "lastUpdate")).get(0);
            if (lastUpdate != null && lastUpdate.get(0).toString().equals("in-progress")) {
            }
            return 1;
        }
        return 0;
    }

    private List<String> toList(DBClientBase dbClient, Object object) {
        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        List<String> list = new ArrayList<>();
        list.add(dbClient.getKey(map));
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getValue() != null) {
                list.add(entry.getKey());
                list.add(entry.getValue().toString());
            }
        }
        return list;
    }

    private List<String> toList2(DBClientBase dbClient, Object object) {
        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        List<String> list = new ArrayList<>();
        String key2 = dbClient.getKey2(map);
        if (key2 != null) {
            list.add(key2);
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                if (entry.getValue() != null) {
                    list.add(entry.getKey());
                    list.add(entry.getValue().toString());
                }
            }
            return list;
        }
        return null;
    }

}
