package com.santechture.service.uae.mssql;

import com.santechture.MasterPriceListItem;
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
public class SPCMasterListsItemsDBClient extends DBClientBase {
    public static final String model = "MasterPriceListItem";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SPCMasterListsItemsDBClient.class);
    private static final String sql = "SELECT [ID], "
            + "CASE	WHEN CPT_code IS NOT NULL THEN  3  "
            + "		WHEN HCPCS_code IS NOT NULL THEN 4  "
            + "		WHEN TradeDrug_code IS NOT NULL THEN 5  "
            + "		WHEN Dental_code IS NOT NULL THEN 6  "
            + "		WHEN Service_code IS NOT NULL THEN 8  "
            + "		WHEN IRDrug_code IS NOT NULL THEN 9  "
            + "		WHEN GenericDrug_code IS NOT NULL THEN 10  "
            + "END AS [Type], "
            + "CASE	WHEN CPT_code IS NOT NULL THEN  CPT_code  "
            + "		WHEN HCPCS_code IS NOT NULL THEN HCPCS_code  "
            + "		WHEN TradeDrug_code IS NOT NULL THEN TradeDrug_code  "
            + "		WHEN Dental_code IS NOT NULL THEN Dental_code "
            + "		WHEN Service_code IS NOT NULL THEN Service_code "
            + "		WHEN IRDrug_code IS NOT NULL THEN IRDrug_code "
            + "		WHEN GenericDrug_code IS NOT NULL THEN GenericDrug_code "
            + "END AS [Code] "
            + " ,[price] ,[startDate] ,[endDate] ,[anaesthesiaBaseUnits], MasterPriceList_id "
            + "From [PL_SPC_MasterPriceListItem] ";
    private static final List<String> tables = List.of("PL_SPC_MasterPriceListItem");
    @Inject
    Logger logger;
    @Inject
    AgroalDataSource dataSource;

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(
                                new MasterPriceListItem(
                                        resultSet.getLong("ID"),
                                        resultSet.getLong("MasterPriceList_id"),
                                        resultSet.getString("Code"),
                                        CodeType.from(resultSet.getInt("type")),
                                        resultSet.getDouble("price"),
                                        resultSet.getTimestamp("startDate") != null ? Date.from(resultSet.getTimestamp("startDate").toInstant()) : null,
                                        resultSet.getTimestamp("endDate") != null ? Date.from(resultSet.getTimestamp("endDate").toInstant()) : null,
                                        (double) resultSet.getInt("anaesthesiaBaseUnits"))
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
        return String.format("%s:%s:%s", model, map.get("masterListId"), map.get("code"));
    }

    public String getModel() {
        return model;
    }

    public List<String> getTables() {
        return tables;
    }


}
