package com.santechture.service.ksa.mssql;

import com.santechture.CusContractConfigurations;
import com.santechture.PLCUSContractCodesConfigurations;
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
public class CusContractCodesConfigurationDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusContractCodesConfigurationDBClient.class);
    final static String model= "CusContractCodesConfigurations";
    @Inject
    Logger logger;

    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;
    private static final String sql = " select contract_id,id, PL_CUS_Contract_Codes_Configurations.code ,PL_CUS_Contract_Codes_Configurations.type ,PL_CUS_Contract_Codes_Configurations.covered,\n"
            + "PL_CUS_Contract_Codes_Configurations.need_Approval from PL_CUS_Contract_Codes_Configurations\n";
    private static final List<String> tables = List.of("PL_CUS_Contract_Codes_Configurations");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int ID = resultSet.getInt("ID");
                        Long contractId = resultSet.getLong("contract_id");
                        int groupType = resultSet.getInt("type");
                        String code = resultSet.getString("code");
                        boolean covered = resultSet.getInt("covered") == 1;
                        boolean need_Approval = resultSet.getInt("need_Approval") == 1;
                        PLCUSContractCodesConfigurations configuration = new PLCUSContractCodesConfigurations(ID, contractId, groupType, code, covered, need_Approval);
                        configuration.setId(ID);
                        result.add(configuration);
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

        return String.format("%s:%s:%s:%s",model, map.get("contractId"), map.get("type"), map.get("code"));
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
