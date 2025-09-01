package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.MasterPriceListItem;
import com.santechture.request.CodeType;
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
public class SPCMasterListsItemsDBClient extends DBClientBase {
    public static final String model = "MasterPriceListItem";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SPCMasterListsItemsDBClient.class);
    private static final String sql = "SELECT [ID], "
            + " type,\n "
            + " code,\n "
            + " [price] ,[startDate] ,[endDate] , PriceList_Id "
            + " From [PL_NEW_CUS_PriceListItem] "
            + " WHERE code IS NOT NULL ";

    private static final String sql_size = "SELECT\n" +
            "    sum(\n" +
            "    COALESCE(DATALENGTH([ID]),0)+\n" +
            "    COALESCE(DATALENGTH(type),0)+\n" +
            "    COALESCE(DATALENGTH(code),0)+\n" +
            "    COALESCE(DATALENGTH([price]),0)+\n" +
            "    COALESCE(DATALENGTH([startDate]),0)+\n" +
            "    COALESCE(DATALENGTH([endDate]),0)+\n" +
            "    COALESCE(DATALENGTH(PriceList_Id),0)) / (1024.0)  as total_size\n" +
            "From\n" +
            "    [PL_NEW_CUS_PriceListItem]\n" +
            "WHERE code IS NOT NULL";

    private static final List<String> tables = List.of("PL_NEW_CUS_PriceListItem");
    @Inject
    Logger logger;
    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new MasterPriceListItem(
                                        resultSet.getLong("ID"),
                                        resultSet.getLong("PriceList_Id"),
                                        resultSet.getString("code"),
                                        CodeType.from(resultSet.getInt("type")),
                                        resultSet.getDouble("price"),
                                        resultSet.getTimestamp("startDate") != null ? Date.from(resultSet.getTimestamp("startDate").toInstant()) : null,
                                        resultSet.getTimestamp("endDate") != null ? Date.from(resultSet.getTimestamp("endDate").toInstant()) : null,
                                        null)
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
        return String.format("%s:%s:%s", model, map.get("masterListId"), map.get("code"));
    }

    public String getModel() {
        return model;
    }

    public List<String> getTables() {
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
