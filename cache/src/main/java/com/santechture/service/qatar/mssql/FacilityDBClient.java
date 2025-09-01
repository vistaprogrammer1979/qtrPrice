package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.Facility;
import com.santechture.FacilityType;
import com.santechture.Regulator;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.json.Json;
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
public class FacilityDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FacilityDBClient.class);
        public static final String model = "Facility";
    @Inject
    Logger logger;
    @Inject
    ObjectMapper objectMapper;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql = "Select F.FACILITY_LICENSE, F.REGULATOR, \n"
            + "F.IS_ACTIVE, F.FACILITY_TYPE_ID, \n"
            + "F.FACILITY_STATUS_ID\n"
            + "from ACCUMED_FACILITY F\n";

    private static final String sql_size = "Select\n" +
            "    sum(\n" +
            "    COALESCE(DATALENGTH(F.FACILITY_LICENSE),0)+\n" +
            "    COALESCE(DATALENGTH(F.REGULATOR),0)+\n" +
            "    COALESCE(DATALENGTH(F.IS_ACTIVE),0)+\n" +
            "    COALESCE(DATALENGTH(F.FACILITY_TYPE_ID),0)+\n" +
            "    COALESCE(DATALENGTH(F.FACILITY_STATUS_ID),0)) / (1024.0) as total_size\n" +
            "from ACCUMED_FACILITY F";

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
    public Object getObject(JsonNode obj) {
    return new Facility(
                obj.get("FACILITY_LICENSE").asText(),
                FacilityType.from(obj.get("FACILITY_TYPE_ID").asInt()),
                Regulator.from(obj.get("REGULATOR").asInt()),
                obj.get("IS_ACTIVE").asInt()!=0);
    }
}
