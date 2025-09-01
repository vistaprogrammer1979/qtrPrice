package com.santechture.service.uae.mssql;

import com.santechture.SPCCodeFactor;
import com.santechture.request.CodeType;
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
public class SpcCodeFactorsDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SpcCodeFactorsDBClient.class);
    final static String model= "SPCCodeFactor";
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql =  "SELECT [ID], [pl_pricelist_id] "
            + " , CASE"
            + " WHEN CPT_code IS NOT NULL THEN 3 "
            + " WHEN HCPCS_code IS NOT NULL THEN 4 "
            + " WHEN TradeDrug_code IS NOT NULL THEN 5 "
            + " WHEN Dental_code IS NOT NULL THEN 6 "
            + " WHEN Service_code IS NOT NULL THEN 8 "
            + " WHEN IRDrug_code IS NOT NULL THEN 9 "
            + " WHEN GenericDrug_code IS NOT NULL THEN 10 "
            + " END AS [Type] "
            + " , CASE "
            + " WHEN CPT_code IS NOT NULL THEN CPT_code "
            + " WHEN HCPCS_code IS NOT NULL THEN HCPCS_code "
            + " WHEN TradeDrug_code IS NOT NULL THEN TradeDrug_code "
            + " WHEN Dental_code IS NOT NULL THEN Dental_code "
            + " WHEN Service_code IS NOT NULL THEN Service_code "
            + " WHEN IRDrug_code IS NOT NULL THEN IRDrug_code "
            + " WHEN GenericDrug_code IS NOT NULL THEN GenericDrug_code "
            + " END AS [Code] "
            + " , [factor], [startDate], [endDate] "
            + "FROM "
            + " [PL_SPC_CODES_FACTOR] "
            + " WHERE "
            + " [approved] = 1 "
            + " AND [deleted] = 0";
    private static final List<String> tables = List.of("PL_SPC_CODES_FACTOR");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new SPCCodeFactor(
                                        resultSet.getLong("ID"),
                                        resultSet.getInt("pl_pricelist_id"),
                                        CodeType.from(resultSet.getInt("Type")),
                                        resultSet.getString("Code"),
                                        resultSet.getDouble("factor"),
                                        Date.from(resultSet.getTimestamp("startDate").toInstant()),
                                        Date.from(resultSet.getTimestamp("endDate").toInstant()))
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
//        log.info(String.format("%s:%s:%s",model,  map.get("type"),map.get("code")));

        return String.format("%s:%s:%s:%s",model, map.get("type"), map.get("code"), map.get("priceListId"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }
}
