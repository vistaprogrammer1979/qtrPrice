package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.CusIP_ARDRGPriceListItem;
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
public class CusPriceListItem_IPARDRG_DBClient extends DBClientBase {
    @Inject
    ObjectMapper objectMapper;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusPriceListItem_IPARDRG_DBClient.class);
    private static final String model = "CusIP_ARDRGPriceListItem";
    private static final String sql = "SELECT [ID],\n" +
            "       [PriceList_Id],\n" +
            "       [DRG_Code],\n" +
//            "       [Length_Of_Stay_roles],\n" +
            "       [Normal_Stay_Price],\n" +
            "       [SSO_base_price],\n" +
            "       [SSO_per_diem_price],\n" +
            "       [Same_day_price],\n" +
            "       [Short_Stay_Price],\n" +
            "       [Long_Stay_Price],\n" +
            "       [Long_Stay_Threshold_Price],\n" +
            "       [Short_Stay_LessThan], \n" +
            "       [Long_Stay_MoreThan], \n" +
            "       [Long_Stay_Threshold] \n"+
            "from PL_NEW_CUS_PriceListItem_IRDRG\n" +
            "where deleted = 0 ";
    private static final List<String> tables = List.of("PL_NEW_CUS_PriceListItem_IRDRG");
    private static final String sql_size = "SELECT sum(COALESCE(DATALENGTH([ID]), 0)+\n" +
            "           COALESCE(DATALENGTH([PriceList_Id]), 0)+\n" +
            "           COALESCE(DATALENGTH([DRG_Code]), 0)+\n" +
//            "           COALESCE(DATALENGTH([Length_Of_Stay_roles]), 0)+\n" +
            "           COALESCE(DATALENGTH([Normal_Stay_Price]), 0)+\n" +
            "           COALESCE(DATALENGTH([SSO_base_price]), 0)+\n" +
            "           COALESCE(DATALENGTH([SSO_per_diem_price]), 0)+\n" +
            "           COALESCE(DATALENGTH([Same_day_price]), 0)+\n" +
            "           COALESCE(DATALENGTH([Short_Stay_Price]), 0)+\n" +
            "           COALESCE(DATALENGTH([Long_Stay_Price]), 0)+\n" +
            "           COALESCE(DATALENGTH([Long_Stay_Threshold_Price]), 0)+\n" +
            "           COALESCE(DATALENGTH([Short_Stay_LessThan]), 0)+\n" +
            "           COALESCE(DATALENGTH([Long_Stay_MoreThan]), 0)+\n" +
            "           COALESCE(DATALENGTH([Long_Stay_Threshold]), 0)\n" +
            "           ) / (1024.0) as total_size\n" +
            "\n" +
            "from PL_NEW_CUS_PriceListItem_IRDRG\n" +
            "where deleted = 0;";
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
                        Long id = resultSet.getLong("ID");
                        Long pricListId = resultSet.getLong("PriceList_Id");
                        String code = resultSet.getString("DRG_Code");
//                        String lengthOfStayRoles = resultSet.getString("Length_Of_Stay_roles");
                        Double normalStayPrice = resultSet.getDouble("Normal_Stay_Price");
                        Double ssoBasePrice = resultSet.getDouble("SSO_base_price");
                        Double ssoPerDiemPrice = resultSet.getDouble("SSO_per_diem_price");
                        Double sameDayPrice = resultSet.getDouble("Same_day_price");
                        Double shortStayPrice = resultSet.getDouble("Short_Stay_Price");
                        Double longStayPrice = resultSet.getDouble("Long_Stay_Price");
                        Double longStayThresholdPrice = resultSet.getDouble("Long_Stay_Threshold_Price");
                        Integer shortStayLessThan = resultSet.getObject("Short_Stay_LessThan",Integer.class);
                        Integer longStayMoreThan = resultSet.getObject("Long_Stay_MoreThan",Integer.class);
                        Integer longStayThreshold = resultSet.getObject("Long_Stay_Threshold",Integer.class);
                        CusIP_ARDRGPriceListItem item = new CusIP_ARDRGPriceListItem(id, pricListId, code, "", shortStayLessThan,
                                longStayMoreThan, longStayThreshold, normalStayPrice, ssoBasePrice, ssoPerDiemPrice, sameDayPrice, shortStayPrice, longStayPrice, longStayThresholdPrice, false);
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
    public Object getObject(JsonNode resultSet) {
        Long id = resultSet.get("ID") != null ? resultSet.get("ID").asLong() : null;
        Long pricListId = resultSet.get("PriceList_Id") != null ? resultSet.get("PriceList_Id").asLong() : null;
        String code = resultSet.get("DRG_Code") != null ? resultSet.get("DRG_Code").asText() : null;
//        String lengthOfStayRoles = resultSet.get("Length_Of_Stay_roles") != null ? resultSet.get("Length_Of_Stay_roles").asText() : null;
        Double normalStayPrice = resultSet.get("Normal_Stay_Price") != null ? resultSet.get("Normal_Stay_Price").asDouble() : null;
        Double ssoBasePrice = resultSet.get("SSO_base_price") != null ? resultSet.get("SSO_base_price").asDouble() : null;
        Double ssoPerDiemPrice = resultSet.get("SSO_per_diem_price") != null ? resultSet.get("SSO_per_diem_price").asDouble() : null;
        Double sameDayPrice = resultSet.get("Same_day_price") != null ? resultSet.get("Same_day_price").asDouble() : null;
        Double shortStayPrice = resultSet.get("Short_Stay_Price") != null ? resultSet.get("Short_Stay_Price").asDouble() : null;
        Double longStayPrice = resultSet.get("Long_Stay_Price") != null ? resultSet.get("Long_Stay_Price").asDouble() : null;
        Double longStayThresholdPrice = resultSet.get("Long_Stay_Threshold_Price") != null ? resultSet.get("Long_Stay_Threshold_Price").asDouble() : null;
        Integer shortStayLessThan = resultSet.get("Short_Stay_LessThan") != null ? resultSet.get("Short_Stay_LessThan").asInt() : null;
        Integer longStayMoreThan = resultSet.get("Long_Stay_MoreThan") != null ? resultSet.get("Long_Stay_MoreThan").asInt() : null;
        Integer longStayThreshold = resultSet.get("Long_Stay_Threshold") != null ? resultSet.get("Long_Stay_Threshold").asInt() : null;
        return new CusIP_ARDRGPriceListItem(id, pricListId, code, "", shortStayLessThan,
                longStayMoreThan, longStayThreshold, normalStayPrice, ssoBasePrice, ssoPerDiemPrice, sameDayPrice, shortStayPrice, longStayPrice, longStayThresholdPrice, false);
    }
}
