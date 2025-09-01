package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.CusPriceListItem;
import com.santechture.Facility;
import com.santechture.FacilityType;
import com.santechture.Regulator;
import com.santechture.request.CodeType;
import com.santechture.service.qatar.mssql.DBClientBase;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.json.Json;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Singleton
public class CusPriceListItemDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusPriceListItemDBClient.class);
    private static final String model = "CusPriceListItem";
    @Inject
    Logger logger;
    @Inject
    ObjectMapper objectMapper;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql ="\n" +
            "SELECT pncp.[ID]\n" +
            "               , pncp.[PriceList_id]\n" +
            "                , pncp.code   AS [Code]\n" +
            "                , pncp.[type] AS [Type]\n" +
            "                , pncp.[price]\n" +
            "                , pncp.[discount]\n" +
            "                , pncp.startDate\n" +
            "                , pncp.endDate\n" +
            "           FROM [PL_CUS_PriceList] pcp\n" +
            "                     inner join [PL_NEW_CUS_PriceListItem] pncp on pcp.ID = pncp.PriceList_Id\n" +
            "            where pcp.deleted = 0 and pncp.deleted = 0\n" +
            "              and pcp.priceListType = 'General' ";

    private static final String sql_size = "\n" +
            "SELECT sum(COALESCE(DATALENGTH(pncp.[ID]), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.[PriceList_id]), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.code ), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.[type] ), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.[price]), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.[discount]), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.startDate), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.endDate), 0)) / (1024.0) as total_size\n" +
            "FROM [PL_CUS_PriceList] pcp\n" +
            "         inner join [PL_NEW_CUS_PriceListItem] pncp on pcp.ID = pncp.PriceList_Id\n" +
            "where pcp.deleted = 0\n" +
            "  and pncp.deleted = 0\n" +
            "  and pcp.priceListType = 'General';";
    private static final List<String> tables = List.of("PL_CUS_PriceList", "PL_NEW_CUS_PriceListItem");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Long pricListId = resultSet.getLong("PriceList_id");
                        Integer type = resultSet.getInt("Type");
                        String code = resultSet.getString("Code");
                        Double price = resultSet.getDouble("price");
                        Double discount = null;
                        if (resultSet.getObject("discount") != null) {
                            discount = resultSet.getDouble("discount");
                        }

                        /*if(rs.wasNull()){
                         discount = null;
                         }*/
                        Date startDate = resultSet.getTimestamp("startDate");
                        Date endDate = resultSet.getTimestamp("endDate");
                        if (code == null) {
                            continue;
                        }
                        /*if (code.equalsIgnoreCase("92222")) {
                         Logger.getLogger(RepoUtils.class.getName()).log(Level.INFO, "test " + discount + " " + master.getID());
                         }
                         if ("17-03".equals(code)) {
                         Logger.getLogger(RepoUtils.class.getName()).log(Level.INFO, " priceListID=" + master.getID() + " code=" + code);
                         }*/
                        CusPriceListItem item = new CusPriceListItem(ID, pricListId, code,
                                CodeType.from(type),
                                price, discount, startDate, endDate);
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
        return String.format("%s:%s:%s", model, map.get("pricListId"), map.get("code"));
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

        Long ID = resultSet.get("ID") != null ? resultSet.get("ID").asLong() : null;
        Long pricListId = resultSet.get("PriceList_Id") != null ? resultSet.get("PriceList_Id").asLong() : null;
        Integer type = resultSet.get("type") != null ? resultSet.get("type").asInt() : null;
        String code = resultSet.get("code") != null ? resultSet.get("code").asText() : null;
        Double price = resultSet.get("price") != null ? resultSet.get("price").asDouble() : null;
        Double discount = null;
        if (resultSet.get("discount") != null) {
            discount = resultSet.get("discount").asDouble();
        }
        Date startDate = resultSet.get("startDate") != null ? objectMapper.convertValue(resultSet.get("startDate").asText(), Date.class) : null;
        Date endDate = resultSet.get("endDate") != null ? objectMapper.convertValue(resultSet.get("endDate").asText(), Date.class) : null;

        CusPriceListItem item = new CusPriceListItem(ID, pricListId, code,
                type != null ? CodeType.from(type): null,
                price, discount, startDate, endDate);
        return item;


    }

}
