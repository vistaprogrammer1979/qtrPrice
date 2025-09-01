package com.santechture.service.ksa.mssql;

import com.santechture.CusContractConfigurations;
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
public class CusContractConfigurationDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CusContractConfigurationDBClient.class);
    final static String model= "CusContractConfigurations";
    @Inject
    Logger logger;

    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;
    private static final String sql =  "select contract_id ,  ID,GROUP_TYPE,DISCOUNT,COPAYMENT,DEDUCTIBLE,MAX_PATIENT_SHARE,IP,OP ,SAUDI_VAT,NONE_SAUDI_VAT,PRIOR_APPROVAL_LIMIT  from PL_CUS_Contract_Configurations";
    private static final List<String> tables = List.of("PL_CUS_Contract_Configurations");

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Long contractId = resultSet.getLong("contract_id");
                        int groupType = resultSet.getInt("GROUP_TYPE");
                        Float discount = resultSet.getObject("DISCOUNT") == null ? null : resultSet.getFloat("DISCOUNT");
                        Double copayment = resultSet.getObject("COPAYMENT") == null ? null : resultSet.getDouble("COPAYMENT");

                        Float deductible = resultSet.getObject("DEDUCTIBLE") == null ? null : resultSet.getFloat("DEDUCTIBLE");

                        Double maxPatientShare = resultSet.getObject("MAX_PATIENT_SHARE") == null ? null : resultSet.getDouble("MAX_PATIENT_SHARE");

                        boolean ip = resultSet.getInt("IP") == 1;
                        boolean op = resultSet.getInt("OP") == 1;
                        Double saudiVat = resultSet.getObject("SAUDI_VAT") != null ? resultSet.getDouble("SAUDI_VAT") : null;
                        Double noneSaudiVat = resultSet.getObject("NONE_SAUDI_VAT") != null ? resultSet.getDouble("NONE_SAUDI_VAT") : null;
                        Double priorApprovalLimit = resultSet.getObject("PRIOR_APPROVAL_LIMIT") != null ? resultSet.getDouble("PRIOR_APPROVAL_LIMIT") : null;

                        CusContractConfigurations configuration = new CusContractConfigurations(groupType, discount, copayment, deductible, maxPatientShare, ip, op);
                        configuration.setCusContractId(contractId);
                        configuration.setId(ID);
                        configuration.setSaudiVat(saudiVat);
                        configuration.setNoneSaudiVat(noneSaudiVat);
                        configuration.setPriorApprovalLimit(priorApprovalLimit);
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

        return String.format("%s:%s",model, map.get("cusContractId"));
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
