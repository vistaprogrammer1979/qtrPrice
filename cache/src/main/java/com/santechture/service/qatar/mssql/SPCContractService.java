package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.SPCContract;
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
import java.util.*;

@Singleton
public class SPCContractService extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SPCContractService.class);
    @Inject
    Logger logger;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT [ID] ,[type] ,[insurer_license] ,[facility_license] ,[package_name] ,[clinician_license] ,[startDate] "
            + "      ,[endDate] ,[factor] ,[approved] ,[deleted] ,[parentId] ,[PHARM_DISCOUNT] ,[IP_DISCOUNT],[OP_DISCOUNT] "
            + "      ,[BASE_RATE] ,[regulator_id] ,[GAP] ,[MARGINAL]  FROM [PL_SPC_PriceList]"
            + " Where deleted = 0 AND approved = 1";
    public static final List<String> tables = List.of("PL_SPC_PriceList");
    public static final String model = "SPCContract";

    public static final List<String> keys = List.of("facilityLicense", "insurerLicense", "packageName");
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new SPCContract(
                                        resultSet.getLong("ID"),
                                        resultSet.getInt("type"),
                                        resultSet.getString("insurer_license"),
                                        resultSet.getString("facility_license"),
                                        resultSet.getString("package_name"),
                                        resultSet.getString("clinician_license"),
                                        Date.from(resultSet.getTimestamp("startDate").toInstant()),
                                        Date.from(resultSet.getTimestamp("endDate").toInstant()),
                                        resultSet.getDouble("factor"),
                                        resultSet.getBoolean("approved"),
                                        resultSet.getBoolean("deleted"),
                                        resultSet.getInt("parentId"),
                                        resultSet.getDouble("PHARM_DISCOUNT"),
                                        resultSet.getDouble("IP_DISCOUNT"),
                                        resultSet.getDouble("OP_DISCOUNT"),
                                        resultSet.getDouble("BASE_RATE"),
                                        resultSet.getInt("regulator_id"),
                                        resultSet.getDouble("GAP"),
                                        resultSet.getDouble("MARGINAL"))
                                );
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
//        log.info(String.format("%s:%s:%s:%s", model, map.get("insurerLicense"), map.get("facilityLicense"), map.get("id")));
        return String.format("%s:%s:%s:%s", model, map.get("insurerLicense"), map.get("facilityLicense"), map.get("id"));
    }
//    public String getKey2(Map<String, Object> map) {
//        log.info(String.format("%s:%s:%s",model,map.get("facilityLicense"),map.get("insurerLicense")));
//        return String.format("%s:%s:%s",model,map.get("facilityLicense"),map.get("insurerLicense"));
//    }
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
