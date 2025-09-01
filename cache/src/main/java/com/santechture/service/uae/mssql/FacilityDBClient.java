package com.santechture.service.uae.mssql;

import com.santechture.Facility;
import com.santechture.FacilityType;
import com.santechture.Regulator;
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
public class FacilityDBClient extends DBClientBase{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FacilityDBClient.class);
    public static final String model = "Facility";
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql = "Select F.FACILITY_LICENSE, F.REGULATOR, \n"
            + "F.IS_ACTIVE, F.FACILITY_TYPE_ID, \n"
            + "F.FACILITY_STATUS_ID\n"
            + "from ACCUMED_FACILITY F\n";
    private static final List<String> tables = List.of("ACCUMED_FACILITY");
    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new Facility(
                                        resultSet.getString("FACILITY_LICENSE"),
                                        FacilityType.from(resultSet.getInt("FACILITY_TYPE_ID")),
                                        Regulator.from(resultSet.getInt("REGULATOR")),
                                        resultSet.getInt("IS_ACTIVE")!=0));
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

//        log.info(String.format("%s:%s", model, map.get("license")));
        return String.format("%s:%s", model, map.get("license"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }

}
