package com.santechture.service.uae.mssql;

import com.santechture.Facility;
import com.santechture.FacilityType;
import com.santechture.PackageCode;
import com.santechture.Regulator;
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
public class PackageCodesDBClient extends DBClientBase{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PackageCodesDBClient.class);
    public static final String model = "PackageCode";
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql = "select  packageGroupID,ActivityType,PackageCode,NetAmount,IS_ITEM_LEVEL from RCM_Package_Group";
    private static final List<String> tables = List.of("RCM_Package_Group");
    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new PackageCode(
                                        resultSet.getString("PackageCode"),
                                        resultSet.getDouble("NetAmount"),
                                        resultSet.getInt("packageGroupID"),
                                        resultSet.getBoolean("IS_ITEM_LEVEL")));
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

//        log.info(String.format("%s:%s", model, map.get("code")));
        return String.format("%s:%s", model, map.get("code"));
    }
    public String getModel(){
        return model;
    }
    public List<String> getTables(){
        return tables;
    }

}
