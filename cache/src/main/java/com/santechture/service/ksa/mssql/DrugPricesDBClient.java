package com.santechture.service.ksa.mssql;

import com.santechture.DrugPrice;
import com.santechture.Regulator;
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
public class DrugPricesDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DrugPricesDBClient.class);
    private static final String model = "DrugPrice";
    @Inject
    Logger logger;

    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;
    private static final String sql = "select  PackageSize as packageSize,RegisterNumber as code , [Public price] as price  from ACCUMED_SFDA_DRUG\n"
            + "where PackageSize>0 ";
    private static final List<String> tables = List.of("ACCUMED_SFDA_DRUG");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String code = resultSet.getString("code");
                        Double package_Price_to_Public = resultSet.getObject("price") != null && !resultSet.getString("price").trim().isEmpty() ? Double.parseDouble(resultSet.getString("price")) : 0;
                        Float packageSize = resultSet.getObject("packageSize") != null ? resultSet.getFloat("packageSize") : 1;
                        DrugPrice drugPrice = new DrugPrice(code, package_Price_to_Public, packageSize);
                        result.add(drugPrice);
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
//        log.info(String.format("%s:%s:%s", model ,map.get("regulator"), map.get("code")));
        return String.format("%s:%s", model , map.get("code"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }

    @Override
    public double calculateSize() {
        return 0;
    }

}
