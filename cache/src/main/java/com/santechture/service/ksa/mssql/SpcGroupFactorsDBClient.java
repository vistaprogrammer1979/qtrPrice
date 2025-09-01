package com.santechture.service.ksa.mssql;

import com.santechture.SPCGroupFactor;
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
public class SpcGroupFactorsDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SpcGroupFactorsDBClient.class);
    public static final String model = "SPCGroupFactor";
    @Inject
    Logger logger;

    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;
    private static final String sql =  "SELECT [ID] ,[pl_pricelist_id] ,[group_id] ,[factor] ,[startDate] ,[endDate] "
            + "FROM PL_SPC_Group_FACTOR "
            + "WHERE [approved] = 1 AND [deleted] = 0";
    private static final List<String> tables = List.of("PL_SPC_Group_FACTOR");

    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new SPCGroupFactor(
                                        resultSet.getLong("ID"),
                                        resultSet.getLong("pl_pricelist_id"),
                                        resultSet.getLong("group_id"),
                                        resultSet.getDouble("factor"),
                                        Date.from(resultSet.getTimestamp("startDate").toInstant()),
                                        Date.from(resultSet.getTimestamp("endDate").toInstant()))
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
//        log.info(String.format("%s:%s:%s",model,map.get("priceListId"), map.get("id")));
        return String.format("%s:%s:%s",model,map.get("priceListId"), map.get("id"));
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

}
