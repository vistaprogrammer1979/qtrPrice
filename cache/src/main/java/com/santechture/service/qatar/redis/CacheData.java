package com.santechture.service.qatar.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.santechture.CodeGroup;
import com.santechture.pojo.CacheProgressStatusEnum;
import com.santechture.pojo.CacheStatusEnum;
import com.santechture.request.CodeType;
import com.santechture.service.qatar.QatarInitializing;
import com.santechture.service.qatar.mssql.CodeGroupsDBClient;
import com.santechture.service.qatar.mssql.DBClientBase;
import com.santechture.service.qatar.mssql.trigger.pojo.AuditEntry;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.RedisClientName;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.ConnectionPoolTooBusyException;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Singleton
//@Startup
public class CacheData {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
    @Inject
    Logger log;
    @Inject
    @RedisClientName("qatar")
    RedisClient redisClient;
    @Inject
    @RedisClientName("qatar")
    ReactiveRedisClient reactiveRedisClient;
    @Inject
    @RedisClientName("qatar")
    ReactiveRedisDataSource RedisDS;
    @Inject
    ObjectMapper objectMapper;


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
    @Inject
    private QatarInitializing qatarInitializing;
    private ReactiveHashCommands<String, String, String> reactiveHashCommands;

//    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @PostConstruct
    private void init() {
        log.info("Starting Redis contract cache");
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        dbClients = qatarInitializing.getDbClients();
        reactiveHashCommands = RedisDS.hash(String.class);
    }

    //    @Scheduled(every="30m")
    public void saveContractsToRedis() {
        log.info("******************************************* Starting Redis cache - Qatar ********************************************************");
        addToRedis();
        log.info("******************************************* Finish Redis cache - Qatar ********************************************************");
    }

    public void saveContractsToRedis(String model) {
        this.updateStatus(allInfoKey, CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), "0", "0");
        this.insertIntoRedis(model);
        this.updateStatus(allInfoKey, CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), "0", "0");

    }

    private void addToRedis() {
//        var status = this.isCached();
//        if (status == 0){
        this.updateStatus(allInfoKey, CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), "0", "0");
        this.insertIntoRedis();
        this.updateStatus(allInfoKey, CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), "0", "0");
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


        redisClient.hmget(List.of(infoKey, lastUpdateField, statusField, progressStatusField//,
//                        checksum.keySet().stream().toList().get(0),
//                        checksum.values().stream().toList().get(0)
        ));

        return true;
    }

    private void updateStatus(String infoKey, String status, String progressStatus, String recordAmount, String size) {
        try {

            var oldValue = redisClient.keys(infoKey);
            if (oldValue != null) {
                redisClient.hset(List.of(infoKey, lastUpdateField, FORMATTER.format(Timestamp.from(Instant.now())), statusField, status, progressStatusField, progressStatus, recordAmountKey, recordAmount, sizeKey, size));
            } else {
                redisClient.hmset(List.of(infoKey, lastUpdateField, FORMATTER.format(Timestamp.from(Instant.now())), statusField, status, progressStatusField, progressStatus, recordAmountKey, recordAmount, sizeKey, size));
            }
        } catch (Exception e) {
            log.error(String.format("Error updating redis status for model %s", infoKey), e);
            Arrays.stream(e.getStackTrace()).forEach(s -> log.error(s.toString()));
        }


    }

    private Map<String, String> getStatus(String infoKey) {
        try {
            var response = redisClient.hgetall(infoKey);
            if (response != null && response.size() > 0) {
                Map<String, String> map = new HashMap<>();
                for (String k : response.getKeys()) {
                    map.put(k, response.get(k).toString());
                }
                return map;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private void insertIntoRedis() {
        for (DBClientBase dbClient : dbClients) {
            var model = dbClient.getModel();
            log.info(model);
            var checksum = dbClient.calCheckSum();
            try {
                updateStatus(String.format(generalInfoKey + ":" + model), CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), "0", "0");
                var data = dbClient.getData();
                var size = dbClient.calculateSize();
//                insertAllInParallel(dbClient, data);
                insertAllInParallel(dbClient, data, reactiveHashCommands);

//                for (Object object : data) {
//                    this.delObject(dbClient, object);
//                    var lst = this.toList(dbClient, object);
//                    if (lst.size() > 1) {
//                        try {
//
//                             redisClient.hmset(lst);
////                            reactiveRedisClient.hmset(lst);
////                            log.info(lst.toString());
//                        } catch (Exception e) {
//                            log.info(lst);
//                            log.error(e);
//                        }
//                    }
//
//
//                }
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
                        } catch (Exception e) {
                            log.error("Error inserting cpt groups into redis", e);
                        }
                    }
                    if (general.size() > 1) {
                        try {
                            redisClient.sadd(general);
                            log.info("Added general groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting hcpc groups into redis", e);
                        }
                    }
                    if (INVESTIGATION.size() > 1) {
                        try {
                            redisClient.sadd(INVESTIGATION);
                            log.info("Added INVESTIGATION groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting ICD groups into redis", e);
                        }
                    }
                    if (labSupplies.size() > 1) {
                        try {
                            redisClient.sadd(labSupplies);
                            log.info("Added labSupplies groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting labSupplies groups into redis", e);
                        }
                    }
                    if (roomAndBed.size() > 1) {
                        try {
                            redisClient.sadd(roomAndBed);
                            log.info("Added roomAndBed groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting roomAndBed groups into redis", e);
                        }
                    }
                    if (other.size() > 1) {
                        try {
                            redisClient.sadd(other);
                            log.info("Added other groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting other groups into redis", e);
                        }
                    }
                    if (pharmaceticalsAndSupplies.size() > 1) {
                        try {
                            redisClient.sadd(pharmaceticalsAndSupplies);
                            log.info("Added pharmaceticalsAndSupplies groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting pharmaceticalsAndSupplies groups into redis", e);
                        }
                    }
                    if (onc.size() > 1) {
                        try {
                            redisClient.sadd(onc);
                            log.info("Added onc groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting onc groups into redis", e);
                        }
                    }
                    if (staff.size() > 1) {
                        try {
                            redisClient.sadd(staff);
                            log.info("Added Staff Health Insurance ChargeType groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting Staff Health Insurance ChargeType groups into redis", e);
                        }
                    }
                    if (packaged.size() > 1) {
                        try {
                            redisClient.sadd(packaged);
                            log.info("Added packaged Services groups to cache");
                        } catch (Exception e) {
                            log.error("Error inserting Packaged Services groups into redis", e);
                        }
                    }

                }
                checksum = dbClient.calCheckSum();
                updateStatus(String.format(generalInfoKey + ":" + model), CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), Integer.toString(data.size()), String.valueOf(size));
            } catch (Exception e) {
                log.error(String.format("Couldn't insert the model %s into redis", model), e);
                try {
                    updateStatus(String.format(generalInfoKey + ":" + model), CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.Failed.name(), "0", "0");
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
                    updateStatus(String.format(generalInfoKey + ":" + model), CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), "0", "0");
                    var data = dbClient.getData();
                    for (Object object : data) {
                        this.delObject(dbClient, object);
                        var lst = this.toList(dbClient, object);
                        if (lst.size() > 1) {
                            try {
//                                log.info(lst);
                                redisClient.hmset(lst);
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
                        icd.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.ICD9) || g.getType().equals(CodeType.ICD10)).map(CodeGroup::getName).collect(Collectors.toSet()));
                        List<String> dental = new ArrayList<>();
                        dental.add("DENTAL_Groups");
                        dental.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.DENTAL)).map(CodeGroup::getName).collect(Collectors.toSet()));
                        List<String> service = new ArrayList<>();
                        service.add("SERVICE_Groups");
                        service.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.SERVICE)).map(CodeGroup::getName).toList());
                        List<String> PharmaceticalsSupplies = new ArrayList<>();
                        PharmaceticalsSupplies.add("Pharmaceticals_And_Supplies_Groups");
                        PharmaceticalsSupplies.addAll(codeGroups.stream().filter(g -> g.getType().equals(CodeType.PharmaceticalsAndSupplies)).map(CodeGroup::getName).toList());
                        if (cpt.size() > 1) {
                            try {
                                redisClient.sadd(cpt);
                                log.info("Added CPT groups to cache");
                            } catch (Exception e) {
                                log.error("Error inserting cpt groups into redis", e);
                            }
                        }
                        if (hcpcs.size() > 1) {
                            try {
                                redisClient.sadd(hcpcs);
                                log.info("Added HCPC groups to cache");
                            } catch (Exception e) {
                                log.error("Error inserting hcpc groups into redis", e);
                            }
                        }
                        if (icd.size() > 1) {
                            try {
                                redisClient.sadd(icd);
                                log.info("Added ICD groups to cache");
                            } catch (Exception e) {
                                log.error("Error inserting ICD groups into redis", e);
                            }
                        }
                        if (dental.size() > 1) {
                            try {
                                redisClient.sadd(dental);
                                log.info("Added DENTAL groups to cache");
                            } catch (Exception e) {
                                log.error("Error inserting dental groups into redis", e);
                            }
                        }
                        if (service.size() > 1) {
                            try {
                                redisClient.sadd(service);
                                log.info("Added SERVICE groups to cache");
                            } catch (Exception e) {
                                log.error("Error inserting SERVICE groups into redis", e);
                            }
                        }
                        if (PharmaceticalsSupplies.size() > 1) {
                            try {
                                redisClient.sadd(PharmaceticalsSupplies);
                                log.info("Added Pharmaceticals And Supplies groups to cache");
                            } catch (Exception e) {
                                log.error("Error inserting Pharmaceticals And Supplies groups into redis", e);
                            }
                        }


                    }
                    checksum = dbClient.calCheckSum();
                    updateStatus(String.format(generalInfoKey + ":" + model), CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), Integer.toString(data.size()), Double.toString(dbClient.calculateSize()));
                } catch (Exception e) {
                    log.error(String.format("Couldn't insert the model %s into redis", model), e);
                    try {
                        updateStatus(String.format(generalInfoKey + ":" + model), CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.Failed.name(), "0", "0");
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

    private String getKey(DBClientBase dbClient, Object object) {
        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        return dbClient.getKey(map);
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

    private DBClientBase getDBClientBase(AuditEntry entry) {
        DBClientBase dbClientBase = null;
        if ("PL_NEW_CUS_PriceListItem".equals(entry.getTableName())) {
            JsonNode resultSet = null;
            try {
                if ("INSERT".equals(entry.getOperation())) {
                    resultSet = objectMapper.readTree(entry.getNewData()).get(0);
                } else {
                    resultSet = objectMapper.readTree(entry.getOldData()).get(0);
                }
                if (resultSet != null) {
                    var priceListId = resultSet.get("PriceList_Id") != null ? resultSet.get("PriceList_Id").asInt() : null;
                    if (priceListId != null) {
                        var response = redisClient.hgetall(String.format("%s:%s", qatarInitializing.getCusPriceListDBClient().getModel(), priceListId));
                        if (response != null && response.size() > 0) {
                            Map<String, String> map = new HashMap<>();
                            var priceListTypeValue = response.get("priceListType");
                            if (priceListTypeValue != null) {
                                var priceListType = priceListTypeValue.toString();
                                switch (priceListType) {
                                    case "IR_DRG" ->
                                            dbClientBase = qatarInitializing.getCusPriceListItemIpArdrgDbClient();
                                    case "OP_QOCS" ->
                                            dbClientBase = qatarInitializing.getCusPriceListItemOpQocsDbClient();
                                    case "ER_URG" ->
                                            dbClientBase = qatarInitializing.getCusPriceListItemEmergencyDbClient();
                                    case "Dentistry_ASDSG" ->
                                            dbClientBase = qatarInitializing.getCusPriceListItemDentistryDbClient();
                                    case "Radiology_ACHI" ->
                                            dbClientBase = qatarInitializing.getCusPriceListItemRadiologyAchiDbClient();
                                    case "IP_SUB" ->
                                            dbClientBase = qatarInitializing.getCusIPSubPriceListItemDbClient();
                                    default -> dbClientBase = qatarInitializing.getCusPriceListItemDBClient();
                                }
                            }
                        }
                    }
                }
            } catch (JsonProcessingException e) {
                dbClientBase = qatarInitializing.getCusPriceListItemDBClient();
            }
            if (dbClientBase == null) {
                dbClientBase = qatarInitializing.getCusPriceListItemDBClient();
            }
        } else {
            dbClientBase = qatarInitializing.getDbClients().stream().filter(dbClientBase1 -> dbClientBase1.getTables().contains(entry.getTableName())).toList().get(0);
        }
        return dbClientBase;
    }

    public Map<Long, String> updateEntries(List<AuditEntry> entries) {
        Map<Long, String> res = new HashMap<>();
        var entriesToInsert = entries.stream().filter(e -> "INSERT".equals(e.getOperation())).toList();
        var entriesToUpdate = entries.stream().filter(e -> "UPDATE".equals(e.getOperation())).toList();
        var entriesToDelete = entries.stream().filter(e -> "DELETE".equals(e.getOperation())).toList();
        for (var entry : entriesToInsert) {
            var dbClientBase = getDBClientBase(entry);
            var oldStatus = getStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()));
            if (oldStatus != null) {
                updateStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()), CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), oldStatus.get(recordAmountKey), oldStatus.get(sizeKey));
            }
            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(entry.getNewData()).get(0);
            } catch (JsonProcessingException e) {
                log.warn("Couldn't parse JSON object", e);
                jsonNode = null;
                continue;
            }
            Object object = dbClientBase.getObject(jsonNode);
            if (object != null) {
                this.delObject(dbClientBase, object);
                boolean deleted = jsonNode.get("deleted") != null && jsonNode.get("deleted").asBoolean();
                var lst = toList(dbClientBase, object);
                if (lst.size() > 1) {
                    try {
                        if (!deleted) {
                            redisClient.hmset(lst);
                        }

                        updateStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()), CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), Long.toString(Long.parseLong(oldStatus.get(recordAmountKey)) + 1), Double.toString(Double.parseDouble(oldStatus.get(sizeKey)) + entry.getNewDataSize() / 1024.0));
                        res.put(entry.getRecordId(), dbClientBase.getModel());
                    } catch (Exception e) {
                        log.info(lst);
                        log.error(e);
                    }
                }
            }
        }

        for (var entry : entriesToDelete) {
            DBClientBase dbClientBase = getDBClientBase(entry);
            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(entry.getOldData()).get(0);
            } catch (JsonProcessingException e) {
                log.warn("Couldn't parse JSON object", e);
                jsonNode = null;
                continue;
            }
            Object object = dbClientBase.getObject(jsonNode);
            var oldStatus = getStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()));
            if (object != null) {
                updateStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()), CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), oldStatus.get(recordAmountKey), oldStatus.get(sizeKey));
                var key = getKey(dbClientBase, object);
                redisClient.del(List.of(key));
                updateStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()), CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), Long.toString(Long.parseLong(oldStatus.get(recordAmountKey)) - 1), Double.toString(Double.parseDouble(oldStatus.get(sizeKey)) - entry.getOldDataSize() / 1024.0));
                res.put(entry.getRecordId(), dbClientBase.getModel());
            }
        }

        for (var entry : entriesToUpdate) {
            DBClientBase dbClientBase = getDBClientBase(entry);
            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(entry.getNewData()).get(0);
            } catch (JsonProcessingException e) {
                log.warn("Couldn't parse JSON object", e);
                jsonNode = null;
                continue;
            }
            Object object = dbClientBase.getObject(jsonNode);
            boolean deleted = jsonNode.get("deleted") != null && jsonNode.get("deleted").asBoolean();
            var oldStatus = getStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()));
            if (object != null) {
                updateStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()), CacheStatusEnum.notValid.name(), CacheProgressStatusEnum.InProgress.name(), oldStatus.get(recordAmountKey), oldStatus.get(sizeKey));
                this.delObject(dbClientBase, object);
                var lst = toList(dbClientBase, object);
                if (lst.size() > 1) {
                    try {
                        if (!deleted) {
                            redisClient.hmset(lst);
                        }
                        res.put(entry.getRecordId(), dbClientBase.getModel());
                    } catch (Exception e) {
                        log.info(lst);
                        log.error(e);
                    }
                    updateStatus(String.format(generalInfoKey + ":" + dbClientBase.getModel()), CacheStatusEnum.valid.name(), CacheProgressStatusEnum.Finished.name(), Long.toString(Long.parseLong(oldStatus.get(recordAmountKey)) - 1), Double.toString(Double.parseDouble(oldStatus.get(sizeKey)) - entry.getOldDataSize() / 1024.0 + entry.getNewDataSize() / 1024.0));
                }
            }
            res.put(entry.getRecordId(), dbClientBase.getModel());
        }
        return res;
    }

    private void delObject(DBClientBase dbClientBase, Object object) {
        try {
            String key = this.getKey(dbClientBase, object);
            reactiveRedisClient.del(List.of(key));
        } catch (Exception exception) {
            log.warn("Couldn't del object: " + object, exception);
        }
    }

    private Map.Entry<String, Map<String, String>> toMap(DBClientBase dbClient, Object object) {
        Objects.requireNonNull(dbClient, "DBClient cannot be null");
        Objects.requireNonNull(object, "Object to convert cannot be null");

        try {
            Map<String, Object> sourceMap = objectMapper.convertValue(object, new TypeReference<>() {
            });

            String key = dbClient.getKey(sourceMap);
            if (key == null || key.isBlank()) {
                throw new IllegalStateException("DBClient key cannot be null or blank");
            }

            Map<String, String> values = sourceMap.entrySet().stream().filter(entry -> entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString(), (existing, replacement) -> existing, LinkedHashMap::new));

            return Map.entry(key, Collections.unmodifiableMap(values));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert object to map", e);
        }
    }

    public void insertAllInParallelBlocking(
            DBClientBase dbClient,
            List<Object> data

    ) {
        Multi.createFrom().iterable(data)
                .onItem().transformToUniAndMerge(object -> {
                    Map.Entry<String, Map<String, String>> entry = toMap(dbClient, object);
                    this.delObject(dbClient, object);
                    return reactiveHashCommands.hset(entry.getKey(), entry.getValue());
                })
                .collect().asList()
                .await().indefinitely(); // BLOCKING! (Avoid in reactive apps)
    }


//    public void insertAllInParallel(
//            DBClientBase dbClient,
//            List<Object> data,
//            ReactiveHashCommands<String, String, String> hashCommands
//    ) {
//        // Batch configuration (adjust based on your Redis setup)
//        final int BATCH_SIZE = 50;
//        final int MAX_CONCURRENT_BATCHES = 5;
//
//        Multi.createFrom().iterable(data)
//                .group().intoLists().of(BATCH_SIZE)
//                .onItem().transformToUniAndConcatenate(batch ->
//                                processBatch(dbClient, batch, hashCommands),
//                        MAX_CONCURRENT_BATCHES
//                )
//                .collect().asList()
//                .subscribe().with(
//                        ignored -> log.debug("Batch insert completed"),
//                        failure -> log.error("Batch insert failed", failure)
//                );
//    }
//
//    private Uni<Void> processBatch(
//            DBClientBase dbClient,
//            List<Object> batch,
//            ReactiveHashCommands<String, String, String> hashCommands
//    ) {
//        List<Uni<Void>> batchOperations = batch.stream()
//                .map(object -> {
//                    Map.Entry<String, Map<String, String>> entry = toMap(dbClient, object);
//                    return hashCommands.hset(entry.getKey(), entry.getValue())
//                            .onFailure().invoke(e ->
//                                    log.errorf("Failed to insert key %s: %s", entry.getKey(), e)
//                            )
//                            .replaceWithVoid();
//                })
//                .collect(Collectors.toList());
//
//        return Uni.join().all(batchOperations).andCollectFailures().replaceWithVoid();
//    }
//public void insertAllInParallel(
//        DBClientBase dbClient,
//        List<Object> data
//) {
//    // Batch configuration
//    final int BATCH_SIZE = 50;
//    final int CONCURRENCY = 5; // Max concurrent batches
//
//    Multi.createFrom().iterable(data)
//            .group().intoLists().of(BATCH_SIZE)
//            .onItem().transformToUni(batch -> processBatch(dbClient, batch))
//            .merge(CONCURRENCY) // Control parallelism here
//            .collect().asList()
//            .subscribe().with(
//                    ignored -> log.debug("Batch insert completed"),
//                    failure -> log.error("Batch insert failed", failure)
//            );
//}
//
//    private Uni<Void> processBatch(
//            DBClientBase dbClient,
//            List<Object> batch
//    ) {
//        List<Uni<Void>> batchOperations = batch.stream()
//                .map(object -> {
//                    Map.Entry<String, Map<String, String>> entry = toMap(dbClient, object);
//                    return reactiveHashCommands.hset(entry.getKey(), entry.getValue())
//                            .onFailure()
//                            .retry()
//                            .withBackOff(Duration.ofMillis(1000), Duration.ofMillis(1000000))
//                            .indefinitely()
//                            .onFailure().invoke(e -> log.errorf("Failed to insert key %s: %s", entry.getKey(), e))
//                            .replaceWithVoid();
//                })
//                .collect(Collectors.toList());
//
//        return Uni.join().all(batchOperations).andCollectFailures().replaceWithVoid();
//    }

    //
//    public void insertAllInParallel(
//            DBClientBase dbClient,
//            List<Object> data,
//            ReactiveHashCommands<String, String, String> hashCommands
//    ) {
//        final int CONCURRENCY = 5;
//
//        Multi.createFrom().iterable(data)
//                .onItem().transformToUni(object -> {
//                    Map.Entry<String, Map<String, String>> entry = toMap(dbClient, object);
//                    return hashCommands.hset(entry.getKey(), entry.getValue())
//                            .onFailure().invoke(e -> log.errorf("Failed to insert key %s: %s", entry.getKey(), e))
//                            .replaceWithVoid();
//                })
//                .merge(CONCURRENCY)
//                .collect().asList()
//                .subscribe().with(
//                        ignored -> log.debug("Insert completed"),
//                        failure -> log.error("Insert failed", failure)
//                );
//    }
    public void insertAllInParallel(
            DBClientBase dbClient,
            List<Object> data,
            ReactiveHashCommands<String, String, String> hashCommands
    ) {
        final int CONCURRENCY = 5;
        final Duration INITIAL_RETRY_DELAY = Duration.ofMillis(500);
        final Duration MAX_RETRY_DELAY = Duration.ofSeconds(5);
        final AtomicInteger totalFailures = new AtomicInteger();

        Multi.createFrom().iterable(data)
                .onItem().transformToUni(object -> {
                    Map.Entry<String, Map<String, String>> entry = toMap(dbClient, object);

                    return Uni.createFrom().item(entry)
                            .onItem().transformToUni(e ->
                                    hashCommands.hset(e.getKey(), e.getValue())
                                            .onFailure().invoke(ex -> {
                                                totalFailures.incrementAndGet();
                                                log.warnf("Retrying key %s (attempt %d): %s",
                                                        e.getKey(),
                                                        totalFailures.get(),
                                                        ex.getMessage());
                                            })
                                            .replaceWithVoid()
                            )
                            .onFailure().retry()
                            .withBackOff(INITIAL_RETRY_DELAY, MAX_RETRY_DELAY)
                            .indefinitely() // Retry forever until success
                            .ifNoItem().after(Duration.ofSeconds(30))
                            .failWith(new RuntimeException("Operation timeout"));
                })
                .merge(CONCURRENCY)
                .collect().asList()
                .subscribe().with(
                        ignored -> log.info("All inserts completed successfully for model " + dbClient.getModel()),
                        failure -> log.error("Critical unrecoverable failure", failure)
                );
    }
}
