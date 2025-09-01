package com.santechture.service.qatar.mssql.trigger;

import com.santechture.service.KafkaMessagingService;
import com.santechture.service.qatar.QatarInitializing;
import com.santechture.service.qatar.mssql.trigger.pojo.AuditEntry;
import com.santechture.service.qatar.redis.CacheData;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;


@ApplicationScoped
@Slf4j
public class AuditLogPoller {

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;

    @Inject
    QatarInitializing qatarInitializing;
    @Inject
    CacheData cacheData;
    @Inject
    KafkaMessagingService kafkaMessagingService;



    // Run every 5 seconds (adjust as needed)
    @Scheduled(every = "5s")
    void pollAuditLog() {
//        log.info("Start Check for changes in DB");
        String sql = """
            SELECT id, table_name, operation, record_id, old_data, new_data, changed_at, changed_by, processed,
               COALESCE(DATALENGTH(old_data),0) as old_data_size,
               COALESCE(DATALENGTH(new_data),0) as new_data_size 
            FROM [PRICE_CACHE_LOG]
            WHERE processed = 0
            ORDER BY changed_at desc
            """;

        List<AuditEntry> newEntries = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {



            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    AuditEntry entry = new AuditEntry(
                            rs.getLong("id"),
                            rs.getString("table_name"),
                            rs.getString("operation"),
                            rs.getLong("record_id"),
                            rs.getString("old_data"),
                            rs.getString("new_data"),
                            rs.getObject("changed_at", OffsetDateTime.class),
                            rs.getString("changed_by"),
                            rs.getInt("old_data_size"),
                            rs.getInt("new_data_size")
                    );
                    newEntries.add(entry);
                }
            }

            if (!newEntries.isEmpty()) {
                log.info("Start caching data bases on the trigger");
                processAuditEntries(newEntries);
                log.info("Processed {} new audit entries", newEntries.size());
            }
        } catch (SQLException e) {
            log.error("Failed to poll audit log", e);
        }
    }

    public static  List<AuditEntry> getLatestEntries(List<AuditEntry> auditEntries) {
        return auditEntries.stream()
                .collect(Collectors.groupingBy(
                        entry -> new AbstractMap.SimpleEntry<>(entry.getTableName(), entry.getRecordId()),
                        Collectors.maxBy(Comparator.comparing(AuditEntry::getChangedAt))
                ))
                .values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    private void processAuditEntries(List<AuditEntry> entries) {
        entries = getLatestEntries(entries);

        Map<Long,String> updatedEntriesIds = cacheData.updateEntries(entries);
        List<Long> actualUpdatedEntriesIds = entries.stream().filter(e -> updatedEntriesIds.containsKey(e.getRecordId())).map(AuditEntry::getId).toList();
        Set<String> updatedModels = new HashSet<>(updatedEntriesIds.values());
        markRecordsAsProcessed(actualUpdatedEntriesIds);
        for(String updatedModel : updatedModels) {
            kafkaMessagingService.sendUpdateModelKeyToKafkaQatar(updatedModel);
        }

//        var updatedTables = entries.stream().map(AuditEntry::getTableName).collect(Collectors.toSet());
//        for (var dbClient : qatarInitializing.getDbClients()) {
//            if (dbClient.getTables().stream().anyMatch(updatedTables::contains)) {
//                log.info("received update caching request");
//                cacheData.saveContractsToRedis(dbClient.getModel());
//                kafkaMessagingService.sendUpdateModelKeyToKafkaQatar(dbClient.getModel());
//            }
//            markRecordsAsProcessed(entries.stream().map(AuditEntry::getId).toList());
//        }

    }


    public int markRecordsAsProcessed(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        // Create a parameter placeholders string (?, ?, ?)
        String placeholders = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = String.format(
                "UPDATE [PRICE_CACHE_LOG] SET processed = 1 WHERE id IN (%s)",
                placeholders);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set each parameter
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }

            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to mark records as processed", e);
            throw new RuntimeException("Database operation failed", e);
        }
    }
}
