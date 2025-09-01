package com.santechture.service.ksa.mssql;

import com.santechture.MasterPriceListItem;
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
public class SPCMasterListsItemsDBClient extends DBClientBase {
    public static final String model = "MasterPriceListItem";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SPCMasterListsItemsDBClient.class);
    private static final String sql ="SELECT [ID], "
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
            + " ,[price] ,[startDate] ,[endDate] ,[anaesthesiaBaseUnits] "
            + "From [PL_SPC_MasterPriceListItem] ";

    private static final String sql_size = "SELECT\n" +
            "    sum(\n" +
            "    COALESCE(DATALENGTH([ID]),0)+\n" +
            "    COALESCE(DATALENGTH(type),0)+\n" +
            "    COALESCE(DATALENGTH(code),0)+\n" +
            "    COALESCE(DATALENGTH([price]),0)+\n" +
            "    COALESCE(DATALENGTH([startDate]),0)+\n" +
            "    COALESCE(DATALENGTH([endDate]),0)+\n" +
            "    COALESCE(DATALENGTH(PriceList_Id),0)) / (1024.0 * 1024.0)\n" +
            "From\n" +
            "    [PL_NEW_CUS_PriceListItem]\n" +
            "WHERE code IS NOT NULL";

    private static final List<String> tables = List.of("PL_SPC_MasterPriceListItem");
    @Inject
    Logger logger;
    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;

    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Long MasterPriceList = resultSet.getLong("MasterPriceList_id");
                        int type = resultSet.getInt("Type");
                        String code = resultSet.getString("Code");
                        Double price = resultSet.getDouble("price");
                        Date startDate = resultSet.getTimestamp("startDate");
                        Date endDate = resultSet.getTimestamp("endDate");
                        Double anaesthesiaBaseUnits = resultSet.getDouble("anaesthesiaBaseUnits");

                        MasterPriceListItem item = new MasterPriceListItem(ID, MasterPriceList, code,
                                CodeType.fromKSACodeType(com.santechture.request.ksa.CodeType.from(type)),
                                price, startDate, endDate, anaesthesiaBaseUnits);
                        result.add(item);
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

    @Override
    public double calculateSize() {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql_size)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("total_size");
                    }
                }
            }
        }catch (SQLException e) {
            log.error(e.getMessage());
            for (var stack : e.getStackTrace()){
                log.error(stack.toString());
            }
        }
        return 0D;
    }


}
