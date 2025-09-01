package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.CusDentistryASDSGPriceListItem;
import com.santechture.request.CodeType;
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
public class CusPriceListItem_Dentistry_DBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusPriceListItem_Dentistry_DBClient.class);
    private static final String model = "CusDentistryASDSGPriceListItem";
    @Inject
    ObjectMapper objectMapper;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql ="SELECT pncp.[ID]\n" +
            "               , pncp.[PriceList_id]\n" +
            "                , pncp.code   AS [Code]\n" +
            "                , pncp.[price]\n" +
            "                , pncp.[price_2]\n" +
            "                , pncp.PriceList_Id\n" +
            "           FROM [PL_CUS_PriceList] pcp\n" +
            "                     inner join [PL_NEW_CUS_PriceListItem] pncp on pcp.ID = pncp.PriceList_Id\n" +
            "            where pcp.deleted = 0 and pncp.deleted = 0\n" +
            "              and pcp.priceListType = 'Dentistry_ASDSG' ";
    private static final String sql_size = "\n" +
            "SELECT sum(COALESCE(DATALENGTH(pncp.[ID]), 0)+\n" +
            "     COALESCE(DATALENGTH(pncp.[PriceList_id]), 0)+\n" +
            "     COALESCE(DATALENGTH(pncp.code ), 0)+\n" +
            "     COALESCE(DATALENGTH(pncp.[price]), 0)+\n" +
            "     COALESCE(DATALENGTH(pncp.[price_2]), 0)+\n" +
            "     COALESCE(DATALENGTH(pncp.PriceList_Id), 0)) / (1024.0) as total_size\n" +
            "FROM [PL_CUS_PriceList] pcp\n" +
            "         inner join [PL_NEW_CUS_PriceListItem] pncp on pcp.ID = pncp.PriceList_Id\n" +
            "where pcp.deleted = 0\n" +
            "  and pncp.deleted = 0\n" +
            "  and pcp.priceListType = 'Dentistry_ASDSG';";
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
                        Double price2 = resultSet.getDouble("price_2");
                        Long cusPricListId = resultSet.getLong("PriceList_Id");
                        CusDentistryASDSGPriceListItem item = new CusDentistryASDSGPriceListItem(ID, cusPricListId, code, price, price2);
                        item.setId(ID);
                        item.setCode(code);
                        item.setGeneralDentistPrice(price);
                        item.setSpecializedDentistPrice(price2);
                        item.setPricListId(cusPricListId);
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
        Double price2 = resultSet.get("Price_2") != null ? resultSet.get("Price_2").asDouble() : null;
        Long cusPricListId = resultSet.get("PriceList_Id") != null ? resultSet.get("PriceList_Id").asLong() : null;
        CusDentistryASDSGPriceListItem item = new CusDentistryASDSGPriceListItem(ID, cusPricListId, code, price, price2);
        item.setId(ID);
        item.setCode(code);
        item.setGeneralDentistPrice(price);
        item.setSpecializedDentistPrice(price2);
        item.setPricListId(cusPricListId);
        return item;
    }

}
