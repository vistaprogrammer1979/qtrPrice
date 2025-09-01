package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.RCMFacilityCodeMapping;
import com.santechture.service.qatar.mssql.DBClientBase;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Singleton
public class RCMFacilityDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RCMFacilityDBClient.class);
    @Inject
    Logger logger;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT Facility_license, HIS_CODE, ACTIVITY_CODE, ACTIVITY_TYPE, ISNULL(PRICE, 0) AS PRICE FROM  RCM_FACILITY_CODES_MAPPING";
    private static final List<String> tables = List.of("RCM_FACILITY_CODES_MAPPING");
    private static final String model = "RCM_FACILITY_CODES_MAPPING";

    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String provider = resultSet.getString("Facility_license");
                        String providerActivityCode = resultSet.getString("HIS_CODE");
                        String activityCode = resultSet.getString("ACTIVITY_CODE");
                        int type = resultSet.getInt("ACTIVITY_TYPE");
                        double price = resultSet.getDouble("PRICE");
                        RCMFacilityCodeMapping mapping = new RCMFacilityCodeMapping( activityCode,   price,   providerActivityCode,   type, provider );
                        result.add(mapping);
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return result;
    }
    @Override
    public String getKey(Map<String, Object> map) {
        return String.format("%s:%s:%s",model,map.get("facilityLisence"), map.get("activityCode"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }

    @Override
    public double calculateSize() {
        return 0;
    }

    @Override
    public Object getObject(JsonNode json) {
        return null;
    }
}
