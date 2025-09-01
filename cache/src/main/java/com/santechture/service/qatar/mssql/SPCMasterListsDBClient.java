package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.MasterPriceList;
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
public class SPCMasterListsDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SPCMasterListsDBClient.class);
    public static final String model = "MasterPriceList";
    @Inject
    Logger logger;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT [ID], [name], [startDate], [endDate], [regulator_id] "
            + "  FROM [PL_CUS_PriceList]";
    private static final String sql_size = "SELECT\n" +
            "    sum(\n" +
            "    COALESCE(DATALENGTH([ID]),0) +\n" +
            "    COALESCE(DATALENGTH([name]),0) +\n" +
            "    COALESCE(DATALENGTH([startDate]),0) +\n" +
            "    COALESCE(DATALENGTH([endDate]),0) +\n" +
            "    COALESCE(DATALENGTH([regulator_id]),0)) / (1024.0) as total_size\n" +
            "FROM [PL_CUS_PriceList]";
    private static final List<String> tables = List.of("PL_CUS_PriceList");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new MasterPriceList(
                                        resultSet.getLong("ID"),
                                        resultSet.getString("name"),
                                        resultSet.getTimestamp("startDate"),
                                        resultSet.getTimestamp("endDate"),
                                        resultSet.getInt("regulator_id"))

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
        return String.format("%s:%s",model,map.get("id"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }

    @Override
    public double calculateSize() {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql_size)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        var total_size  = resultSet.getDouble("total_size");
                        if (total_size > 0d ){
                            total_size = Math.round(total_size);
                            if (total_size > 0d) {
                                return total_size;
                            }else {
                                return 1;
                            }
                        }
                    }
                }
            }
        }catch (SQLException e) {
            log.error(e.getMessage());
            for (var stack : e.getStackTrace()){
                log.error(stack.toString());
            }
        }
        return 0D;
    }

    @Override
    public Object getObject(JsonNode json) {
        return null;
    }
}
