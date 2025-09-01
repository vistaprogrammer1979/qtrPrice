package com.santechture.service.qatar.mssql.trigger;

import com.santechture.service.qatar.QatarInitializing;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class InitializingAuditPriceTables {

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;

    @Inject
    QatarInitializing qatarInitializing;
    // List of tables you want to audit
    private Set<String> auditTables ;

    @Startup
    public void init() {
        log.info("InitializingAuditPriceTables started");
        auditTables = new HashSet<>();
        qatarInitializing.getDbClients().stream().forEach(
                dbClient -> {
                    if (dbClient.getTables() != null){
                        auditTables.addAll(dbClient.getTables());
                    }
                }
        );
        createAuditTableIfNotExists();
        createTriggersForAuditedTables();
    }


    private void createAuditTableIfNotExists() {
        String createTableSQL = """
            IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'PRICE_CACHE_LOG')
                BEGIN
                    CREATE TABLE PRICE_CACHE_LOG (
                        id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        table_name NVARCHAR(255) NOT NULL,
                        operation NVARCHAR(20) NOT NULL,
                        record_id BIGINT,
                        old_data NVARCHAR(MAX),
                        new_data NVARCHAR(MAX),
                        changed_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
                        changed_by NVARCHAR(255),
                        processed BIT NOT NULL DEFAULT 0
                    );
                    CREATE INDEX idx_audit_log_table_record ON PRICE_CACHE_LOG(table_name, record_id);
                    CREATE INDEX idx_audit_log_changed_at ON PRICE_CACHE_LOG(changed_at);
                END
            """;

        executeSQL(createTableSQL, "Audit table created or already exists");
    }

    private void createTriggersForAuditedTables() {
        String  facilityIdColumn = "[FACILITY_ID]";
        String  idColumn = "[ID]";

        for (String table : auditTables) {
            if (table.equals("ACCUMED_FACILITY")){
                idColumn = facilityIdColumn;
            }
            // First drop existing trigger if it exists
            String dropTriggerSQL = """
                IF EXISTS (SELECT * FROM sys.triggers WHERE name = 'trigger_audit_%s')
                BEGIN
                    DROP TRIGGER trigger_audit_%s
                END
                """.formatted(table, table);

            executeSQL(dropTriggerSQL, "Dropped existing trigger for table: " + table);
            String triggerSQL = """
                CREATE TRIGGER trigger_audit_%s
                ON %s
                AFTER INSERT, UPDATE, DELETE
                AS
                BEGIN
                    SET NOCOUNT ON;
                    -- Handle INSERT operations
                    IF EXISTS (SELECT * FROM inserted) AND NOT EXISTS (SELECT * FROM deleted)
                    BEGIN
                        INSERT INTO PRICE_CACHE_LOG (table_name, operation, record_id, new_data, changed_by)
                        SELECT '%s', 'INSERT', %s,  (SELECT * FROM inserted FOR JSON AUTO), suser_name()
                        FROM inserted;
                    END
                    -- Handle UPDATE operations
                    IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
                    BEGIN
                        INSERT INTO PRICE_CACHE_LOG (table_name, operation, record_id, old_data, new_data, changed_by)
                        SELECT '%s', 'UPDATE', %s,
                               (SELECT * FROM deleted FOR JSON AUTO),
                               (SELECT * FROM inserted FOR JSON AUTO), suser_name()
                        FROM inserted i;
                    END
                    -- Handle DELETE operations
                    IF NOT EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
                    BEGIN
                        INSERT INTO PRICE_CACHE_LOG (table_name, operation, record_id, old_data, changed_by)
                        SELECT '%s', 'DELETE', %s, (SELECT * FROM deleted FOR JSON AUTO), suser_name()
                        FROM deleted;
                    END
                END
                """.formatted(table, table, table, idColumn, table, idColumn, table, idColumn);

            executeSQL(triggerSQL, "Created triggers for table: " + table);
        }
    }

    private void executeSQL(String sql, String successMessage) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
            log.info(successMessage);
        } catch (SQLException e) {
            log.error("Failed to execute SQL: " + e.getMessage());
            throw new RuntimeException("Database initialization price audit failed", e);
        }
    }
}
