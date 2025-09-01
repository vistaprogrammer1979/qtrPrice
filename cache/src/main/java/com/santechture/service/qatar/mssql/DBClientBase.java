package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DBClientBase {
    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    final static Logger log = LoggerFactory.getLogger(DBClientBase.class);

    public abstract String getKey(Map<String, Object> map);

    public String getKey2(Map<String, Object> map) {
        return null;
    }

    public abstract List<Object> getData();
    public abstract String getModel();
    public abstract List<String> getTables();
    public  Map<String, String> calCheckSum() {
//        log.info("Start calCheckSum");
        Map<String, String> checkSum = new HashMap<>();
        StringBuilder sQuery = new StringBuilder();
        for (String t : getTables()) {
            sQuery.append("SELECT '").append(t).append("', CHECKSUM_AGG(BINARY_CHECKSUM(*)) FROM dbo.").append(t).append(" WITH (NOLOCK) union ");
        }
        //remove last union
        sQuery = new StringBuilder(sQuery.substring(0, sQuery.length() - 7));
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sQuery.toString())) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        checkSum.put(
                                resultSet.getString(1),
                                resultSet.getString(2)
                        );
                    }
                }
            }
            if (checkSum.isEmpty()) {
                throw new SQLException("No checksum found");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            for (var stack : e.getStackTrace()) {
                log.error(stack.toString());
            }
            return Map.of("", "");
        }
        return Map.of(
                String.join("|", checkSum.keySet()),
                String.join("|", checkSum.values())
        );
    }
    public abstract double calculateSize();
    public abstract Object getObject(JsonNode json);
}
