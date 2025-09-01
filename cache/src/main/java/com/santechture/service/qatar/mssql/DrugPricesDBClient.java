package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.DrugPrice;
import com.santechture.Regulator;
import com.santechture.service.qatar.mssql.DBClientBase;
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
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT [CODE], [Package_Price_to_Public], [Unit_Price_to_Public], [DATE_FROM]\n"
            + " ,[DATE_TO], [regulator], [Dosage_Form] FROM [DRUG_PRICES_VIEW] order by regulator,code";
    private static final List<String> tables = List.of("DRUG_PRICES_VIEW");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String code = resultSet.getString("CODE");
                        Double package_Price_to_Public = resultSet.getDouble("Package_Price_to_Public");
                        Double unit_Price_to_Public = resultSet.getDouble("Unit_Price_to_Public");
                        Date startDate = resultSet.getTimestamp("DATE_FROM");
                        Date endDate = resultSet.getTimestamp("DATE_TO");
                        Integer regulator = resultSet.getInt("regulator");
                        String dosage_Form = resultSet.getString("Dosage_Form");
//                        System.out.println("getDrugPrices:"+regulator+":code:"+code);
                        DrugPrice drugPrice = new DrugPrice(code, package_Price_to_Public,
                                unit_Price_to_Public, startDate, endDate, regulator, dosage_Form);
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
        return String.format("%s:%s:%s", model , Regulator.from((Integer) map.get("regulator")).name(), map.get("code"));
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

    @Override
    public Object getObject(JsonNode json) {
        return null;
    }

}
