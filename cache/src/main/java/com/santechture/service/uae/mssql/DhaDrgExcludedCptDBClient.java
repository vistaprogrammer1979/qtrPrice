package com.santechture.service.uae.mssql;

import com.santechture.DRGExcludedCpts;
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
public class DhaDrgExcludedCptDBClient extends DBClientBase{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DhaDrgExcludedCptDBClient.class);
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql = "SELECT   CODE   FROM  STT_DRG_EXCLUDED_CPTS  " ;
    private static final List<String> tables = List.of("STT_DRG_EXCLUDED_CPTS");
    private static final String model = "DRGExcludedCPTs";
    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String code = resultSet.getString("CODE");

                        DRGExcludedCpts cpt = new DRGExcludedCpts(code, CodeType.from(3));
                        result.add(cpt);
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
        return String.format("%s:%s", model,map.get("code"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }
}
