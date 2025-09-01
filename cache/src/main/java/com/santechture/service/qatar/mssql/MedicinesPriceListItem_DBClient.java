package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.CusRadiology_ACHIPriceListItem;
import com.santechture.MedicinesPriceListItem;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Data;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Singleton
public class MedicinesPriceListItem_DBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MedicinesPriceListItem_DBClient.class);
    private static final String model = "MedicinesPriceListItem";
    @Inject
    ObjectMapper objectMapper;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql ="select ID, TRADE_CODE, TRADE_NAME, INGREDIENT_STRENGTH, DOSAGE_FORM, PACKAGE_PRICE_TO_PUBLIC,\n" +
            "                               MANUFACTURER, REGISTERED_OWNER, unit_price, expiry_date, effective_date, GRANULAR_UNIT,\n" +
            "                               Source, SCIENTIFIC_CODE, SCIENTIFIC_NAME, ROUTE_OF_ADMIN, Status,\n" +
            "                               creation_date, IsDeleted\n" +
            "from RCM_QTR_MEDICINES\n" +
            "where IsDeleted != 1 or IsDeleted is null";
    private static final String sql_size = "SELECT sum(" +
            "           COALESCE(DATALENGTH([ID]), 0) +\n" +
            "           COALESCE(DATALENGTH([TRADE_CODE]), 0) +\n" +
            "           COALESCE(DATALENGTH([TRADE_NAME]), 0) +\n" +
            "           COALESCE(DATALENGTH([INGREDIENT_STRENGTH]), 0) +\n" +
            "           COALESCE(DATALENGTH([DOSAGE_FORM]), 0) +\n" +
            "           COALESCE(DATALENGTH([PACKAGE_PRICE_TO_PUBLIC]), 0) +\n" +
            "           COALESCE(DATALENGTH([MANUFACTURER]), 0) +\n" +
            "           COALESCE(DATALENGTH([REGISTERED_OWNER]), 0) +\n" +
            "           COALESCE(DATALENGTH([unit_price]), 0) +\n" +
            "           COALESCE(DATALENGTH([expiry_date]), 0) +\n" +
            "           COALESCE(DATALENGTH([effective_date]), 0) +\n" +
            "           COALESCE(DATALENGTH([GRANULAR_UNIT]), 0) +\n" +
            "           COALESCE(DATALENGTH([Source]), 0) +\n" +
            "           COALESCE(DATALENGTH([SCIENTIFIC_CODE]), 0) +\n" +
            "           COALESCE(DATALENGTH([SCIENTIFIC_NAME]), 0) +\n" +
            "           COALESCE(DATALENGTH([ROUTE_OF_ADMIN]), 0) +\n" +
            "           COALESCE(DATALENGTH([Status]), 0) +\n" +
            "           COALESCE(DATALENGTH([creation_date]), 0) +\n" +
            "           COALESCE(DATALENGTH([IsDeleted]), 0) \n" +
            ") / (1024.0) as total_size\n" +
            "FROM [RCM_QTR_MEDICINES] pcp\n" +
            "where (IsDeleted != 1 or IsDeleted is null)";

    private static final List<String> tables = List.of("RCM_QTR_MEDICINES");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        String TRADE_CODE = resultSet.getString("TRADE_CODE");
                        String TRADE_NAME = resultSet.getString("TRADE_NAME");
                        String INGREDIENT_STRENGTH = resultSet.getString("INGREDIENT_STRENGTH");
                        String DOSAGE_FORM = resultSet.getString("DOSAGE_FORM");
                        Double PACKAGE_PRICE_TO_PUBLIC = resultSet.getDouble("PACKAGE_PRICE_TO_PUBLIC");
                        String MANUFACTURER = resultSet.getString("MANUFACTURER");
                        String REGISTERED_OWNER = resultSet.getString("REGISTERED_OWNER");
                        Double unit_price = resultSet.getDouble("unit_price");
                        java.sql.Date expiry_date = resultSet.getDate("expiry_date");
                        java.sql.Date effective_date = resultSet.getDate("effective_date");
                        Long GRANULAR_UNIT = resultSet.getLong("GRANULAR_UNIT");
                        String Source = resultSet.getString("Source");
                        String SCIENTIFIC_CODE = resultSet.getString("SCIENTIFIC_CODE");
                        String SCIENTIFIC_NAME = resultSet.getString("SCIENTIFIC_NAME");
                        String ROUTE_OF_ADMIN = resultSet.getString("ROUTE_OF_ADMIN");
                        String Status = resultSet.getString("Status");
                        Date creation_date = resultSet.getDate("creation_date");
                        Boolean IsDeleted = resultSet.getBoolean("IsDeleted");

                        MedicinesPriceListItem item = new MedicinesPriceListItem();
                            item.setId(ID);
                            item.setTradeCode(TRADE_CODE);
                            item.setTradeName(TRADE_NAME);
                            item.setIngredientStrength(INGREDIENT_STRENGTH);
                            item.setDosageForm(DOSAGE_FORM);
                            item.setPackagePriceToPublic(PACKAGE_PRICE_TO_PUBLIC);
                            item.setManufacturer(MANUFACTURER);
                            item.setRegisteredOwner(REGISTERED_OWNER);
                            item.setUnitPrice(unit_price);
                            item.setExpiryDate(expiry_date);
                            item.setEffectiveDate(effective_date);
                            item.setGranularUnit(GRANULAR_UNIT);
                            item.setSource(Source);
                            item.setScientificCode(SCIENTIFIC_CODE);
                            item.setScientificName(SCIENTIFIC_NAME);
                            item.setRouteOfAdmin(ROUTE_OF_ADMIN);
                            item.setStatus(Status);
                            item.setCreationDate(creation_date);
                            item.setIsDeleted(IsDeleted);

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
        return String.format("%s:%s", model, map.get("tradeCode"));
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
        String TRADE_CODE = resultSet.get("TRADE_CODE") != null ? resultSet.get("TRADE_CODE").asText() : null;
        String TRADE_NAME = resultSet.get("TRADE_NAME") != null ? resultSet.get("TRADE_NAME").asText() : null;
        String INGREDIENT_STRENGTH = resultSet.get("INGREDIENT_STRENGTH") != null ? resultSet.get("INGREDIENT_STRENGTH").asText() : null;
        String DOSAGE_FORM = resultSet.get("DOSAGE_FORM") != null ? resultSet.get("DOSAGE_FORM").asText() : null;
        Double PACKAGE_PRICE_TO_PUBLIC = resultSet.get("PACKAGE_PRICE_TO_PUBLIC") != null ? resultSet.get("PACKAGE_PRICE_TO_PUBLIC").asDouble() : null;
        String MANUFACTURER = resultSet.get("MANUFACTURER") != null ? resultSet.get("MANUFACTURER").asText() : null;
        String REGISTERED_OWNER = resultSet.get("REGISTERED_OWNER") != null ? resultSet.get("REGISTERED_OWNER").asText() : null;
        Double unit_price = resultSet.get("unit_price") != null ? resultSet.get("unit_price").asDouble() : null;
        java.sql.Date expiry_date = resultSet.get("expiry_date") != null ? objectMapper.convertValue(resultSet.get("expiry_date").asText(), Date.class) : null;
        java.sql.Date effective_date = resultSet.get("effective_date") != null ? objectMapper.convertValue(resultSet.get("effective_date").asText(), Date.class) : null;
        Long GRANULAR_UNIT = resultSet.get("GRANULAR_UNIT") != null ? resultSet.get("GRANULAR_UNIT").asLong() : null;
        String Source = resultSet.get("Source") != null ? resultSet.get("Source").asText() : null;
        String SCIENTIFIC_CODE = resultSet.get("SCIENTIFIC_CODE") != null ? resultSet.get("SCIENTIFIC_CODE").asText() : null;
        String SCIENTIFIC_NAME = resultSet.get("SCIENTIFIC_NAME") != null ? resultSet.get("SCIENTIFIC_NAME").asText() : null;
        String ROUTE_OF_ADMIN = resultSet.get("ROUTE_OF_ADMIN") != null ? resultSet.get("ROUTE_OF_ADMIN").asText() : null;
        String Status = resultSet.get("Status") != null ? resultSet.get("Status").asText() : null;
        Date creation_date = resultSet.get("creation_date") != null ? objectMapper.convertValue(resultSet.get("creation_date").asText(), Date.class) : null;
        Boolean IsDeleted = resultSet.get("IsDeleted") != null ? resultSet.get("IsDeleted").asBoolean() : null;

        MedicinesPriceListItem item = new MedicinesPriceListItem( );
                item.setId(ID);
                item.setTradeCode(TRADE_CODE);
                item.setTradeName(TRADE_NAME);
                item.setIngredientStrength(INGREDIENT_STRENGTH);
                item.setDosageForm(DOSAGE_FORM);
                item.setPackagePriceToPublic(PACKAGE_PRICE_TO_PUBLIC);
                item.setManufacturer(MANUFACTURER);
                item.setRegisteredOwner(REGISTERED_OWNER);
                item.setUnitPrice(unit_price);
                item.setExpiryDate(expiry_date);
                item.setEffectiveDate(effective_date);
                item.setGranularUnit(GRANULAR_UNIT);
                item.setSource(Source);
                item.setScientificCode(SCIENTIFIC_CODE);
                item.setScientificName(SCIENTIFIC_NAME);
                item.setRouteOfAdmin(ROUTE_OF_ADMIN);
                item.setStatus(Status);
                item.setCreationDate(creation_date);
                item.setIsDeleted(IsDeleted);

        return item;
    }

}
