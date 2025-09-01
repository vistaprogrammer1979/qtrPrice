package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.CusIP_SUBPriceListItem;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
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
public class CusIP_SUBPriceListItem_DBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusIP_SUBPriceListItem_DBClient.class);
    private static final String model = "CusIP_SUBPriceListItem";
    @Inject
    ObjectMapper objectMapper;
    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql ="SELECT pncp.[ID]\n" +
            "     , pncp.[PriceList_id]\n" +
            "     , pncp.code   AS [Code]\n" +
            "     , pncp.[price]\n" +
            "FROM [PL_CUS_PriceList] pcp\n" +
            "         inner join [PL_NEW_CUS_PriceListItem] pncp on pcp.ID = pncp.PriceList_Id\n" +
            "where pcp.deleted = 0 and pncp.deleted = 0 \n" +
            "  and pcp.priceListType = 'IP_SUB' ";

    private static final String sql_size = "SELECT sum(COALESCE(DATALENGTH(pncp.[ID]), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.[PriceList_id]), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.code), 0) +\n" +
            "           COALESCE(DATALENGTH(pncp.[price]), 0)) / (1024.0) as total_size\n" +
            "FROM [PL_CUS_PriceList] pcp\n" +
            "         inner join [PL_NEW_CUS_PriceListItem] pncp on pcp.ID = pncp.PriceList_Id\n" +
            "where pcp.deleted = 0\n" +
            "  and pncp.deleted = 0\n" +
            "  and pcp.priceListType = 'IP_SUB'";


    private static final List<String> tables = List.of("PL_CUS_PriceList", "PL_NEW_CUS_PriceListItem");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        String code = resultSet.getString("Code");
                        Double price = resultSet.getDouble("price");
                        Long cusPricListId = resultSet.getLong("PriceList_Id");
                        CusIP_SUBPriceListItem item = new CusIP_SUBPriceListItem(ID, cusPricListId, code, price);
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


        return 0D;
    }

    @Override
    public Object getObject(JsonNode resultSet) {
        Long ID = resultSet.get("ID") != null ? resultSet.get("ID").asLong() : null;
        String code = resultSet.get("code") != null ? resultSet.get("code").asText() : null;
        Double price = resultSet.get("price") != null ? resultSet.get("price").asDouble() : null;
        Long cusPricListId = resultSet.get("PriceList_Id") != null ? resultSet.get("PriceList_Id").asLong() : null;
        return new CusIP_SUBPriceListItem(ID, cusPricListId, code, price);
    }

}
