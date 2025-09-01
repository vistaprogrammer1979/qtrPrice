package com.santechture.service.uae.mssql;

import
        com.santechture.DHA_DRG_HighCost;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Singleton
public class DhaDrgHighCostDBClient extends DBClientBase{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DhaDrgHighCostDBClient.class);
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql = "SELECT distinct Activity_Code, Activity_Type FROM STT_DHA_DRG_HIGHCOST" ;
    private static final List<String> tables = List.of("STT_DHA_DRG_HIGHCOST");
    public static final String model = "DHA_DRG_HighCost";

    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer type = resultSet.getInt("Activity_Type");
                        String code = resultSet.getString("Activity_Code");
                        // String description = rs.getString("Description");

                            DHA_DRG_HighCost drg = new DHA_DRG_HighCost(code, CodeType.from(type));
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
//        log.info(String.format("%s:%s", model ,map.get("code")));
        return String.format("%s:%s", model ,map.get("code"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }
}
