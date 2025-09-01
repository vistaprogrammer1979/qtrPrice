package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.CusPriceList;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Singleton
public class CusPriceListDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusPriceListDBClient.class);
    private static final String model = "CusPriceList";
    @Inject
    Logger logger;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql =
            "SELECT [ID]," +
                    " startDate ," +
                    " [endDate]  ," +
                    " [approved] ,  " +
                    "[priceListType] ," +
                    " [regulator_id]  ," +
                    " [FACILITY_ID]  ," +
                    " [FACILITY_GROUP_ID] ," +
                    "[regulator_id], " +
                    "[deleted]"
            + " FROM "
            + " [PL_CUS_PriceList] "
            + " where deleted = 0 ";

    private static final String sql_size = "SELECT sum(COALESCE(DATALENGTH([ID]), 0)+\n" +
            "       COALESCE(DATALENGTH(startDate), 0)+\n" +
            "       COALESCE(DATALENGTH([endDate]), 0)+\n" +
            "       COALESCE(DATALENGTH([approved]), 0)+\n" +
            "       COALESCE(DATALENGTH([priceListType]), 0)+\n" +
            "       COALESCE(DATALENGTH([regulator_id]), 0)+\n" +
            "       COALESCE(DATALENGTH([FACILITY_ID]), 0)+\n" +
            "       COALESCE(DATALENGTH([FACILITY_GROUP_ID]), 0)+\n" +
            "       COALESCE(DATALENGTH([regulator_id]), 0)+\n" +
            "       COALESCE(DATALENGTH([deleted]), 0)) / (1024.0) as total_size\n" +
            "FROM [PL_CUS_PriceList]\n" +
            "where deleted = 0";

    private static final List<String> tables = List.of("PL_CUS_PriceList");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Date startDate = resultSet.getTimestamp("startDate");
                        Date endDate = resultSet.getTimestamp("endDate");
                        String priceListType = resultSet.getString("priceListType");
                        Boolean approved = resultSet.getBoolean("approved");
                        Boolean deleted = resultSet.getBoolean("deleted");
                        Integer regulator_id = resultSet.getInt("regulator_id");
                        Integer facilityId = resultSet.getInt("FACILITY_ID");
                        Integer facilityGroupId = resultSet.getInt("FACILITY_GROUP_ID");
                        CusPriceList item = new CusPriceList();
                        item.setId(ID);
                        item.setApproved(approved);
                        item.setStartDate(startDate);
                        item.setEndDate(endDate);
                        item.setDeleted(deleted);
                        item.setRegulatorId(regulator_id);
                        item.setFacilityId(facilityId);
                        item.setFacilityGroupId(facilityGroupId);
                        item.setPriceListType(priceListType);
                        result.add(item);
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
        return String.format("%s:%s", model, map.get("id"));
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

        return 0;
    }

    @Override
    public Object getObject(JsonNode resultSet) {
        Long ID = resultSet.get("ID").asLong();
        Date startDate = resultSet.get("startDate") != null ? objectMapper.convertValue(resultSet.get("startDate").asText(), Date.class) : null;
        Date endDate = resultSet.get("endDate") != null ? objectMapper.convertValue(resultSet.get("endDate").asText(), Date.class) : null;
        String priceListType = resultSet.get("priceListType") != null ? resultSet.get("priceListType").asText() : null;
        Boolean approved = resultSet.get("approved") != null ? resultSet.get("approved").asBoolean() : null;
        Boolean deleted = resultSet.get("deleted") != null ? resultSet.get("deleted").asBoolean() : null;
        Integer regulator_id = resultSet.get("regulator_id") != null ? resultSet.get("regulator_id").asInt() : null;
        Integer facilityId = resultSet.get("FACILITY_ID") != null ? resultSet.get("FACILITY_ID").asInt() : null;
        Integer facilityGroupId = resultSet.get("FACILITY_GROUP_ID") != null ? resultSet.get("FACILITY_GROUP_ID").asInt() : null;
        CusPriceList item = new CusPriceList();
        item.setId(ID);
        item.setApproved(approved);
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        item.setDeleted(deleted);
        item.setRegulatorId(regulator_id);
        item.setFacilityId(facilityId);
        item.setFacilityGroupId(facilityGroupId);
        item.setPriceListType(priceListType);
        return item;
    }

}
