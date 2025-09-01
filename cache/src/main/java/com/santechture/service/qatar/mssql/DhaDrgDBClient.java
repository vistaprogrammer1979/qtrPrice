package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.DHA_DRG;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Singleton
public class DhaDrgDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DhaDrgDBClient.class);
    @Inject
    Logger logger;

    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT distinct  DRG_Code , Admission_Type  , Relative_Weight  , HCPCS_Portion,   " +
            "                  DRUG_Portion , Surgery_Portion , ALOS   FROM   STT_DHA_DRG_CODES  ";
    private static final List<String> tables = List.of("STT_DHA_DRG_CODES");
    private static final String model = "DHA_DRG";

    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String addmissionType = resultSet.getString("Admission_Type");
                        String code = resultSet.getString("DRG_Code");
                        // String description = rs.getString("DRG_Description");
                        Double relativeWeight = resultSet.getDouble("Relative_Weight");
                        Double hcpcsPortion = resultSet.getDouble("HCPCS_Portion");
                        Double drugPortion = resultSet.getDouble("DRUG_Portion");
                        Double surgeryPortion = resultSet.getDouble("Surgery_Portion");
                        Double aLos = resultSet.getDouble("ALOS");
                        DHA_DRG drg = new DHA_DRG(code, addmissionType, relativeWeight, hcpcsPortion, drugPortion, surgeryPortion, aLos );
                        result.add(drg);
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
        return String.format("%s:%s",model,map.get("code"));
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
