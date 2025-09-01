package com.santechture.service.uae.mssql;

import com.santechture.MasterPriceList;
import com.santechture.SPCContract;
import io.agroal.api.AgroalDataSource;
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
public class SPCMasterListsDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SPCMasterListsDBClient.class);
    public static final String model = "MasterPriceList";
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql = "SELECT [ID], [name], [startDate], [endDate], [regulator_id] "
            + "  FROM [PL_SPC_MasterPriceList]";
    private static final List<String> tables = List.of("PL_SPC_MasterPriceList");

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

}
