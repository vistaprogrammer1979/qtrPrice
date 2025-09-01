package com.santechture.service.ksa.mssql;

import com.santechture.CusContract;
import com.santechture.SPCContract;
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
public class SPCContractsDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SPCContractsDBClient.class);
    private static final String model = "SPCCusContract";
    @Inject
    Logger logger;

    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT [ID] ,[type] ,[insurer_license] ,[facility_license] ,[package_name] ,[clinician_license] ,[startDate] "
            + "      ,[endDate] ,[factor] ,[approved] ,[deleted] ,[parentId] ,[PHARM_DISCOUNT] ,[IP_DISCOUNT],[OP_DISCOUNT] "
            + "      ,[BASE_RATE] ,[regulator_id] ,[GAP] ,[MARGINAL]  FROM [PL_SPC_PriceList]"
            + " Where deleted = 0 AND approved = 1";
    private static final List<String> tables = List.of("PL_SPC_PriceList");

    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Integer type = resultSet.getInt("type");
                        String insurerLicense = resultSet.getString("insurer_license");
                        String facilityLicense = resultSet.getString("facility_license");
                        String packageName = resultSet.getString("package_name");
                        String clinicianLicense = resultSet.getString("clinician_license");
                        Date startDate = resultSet.getTimestamp("startDate");
                        Date endDate = resultSet.getTimestamp("endDate");
                        Double factor = resultSet.getDouble("factor");
                        Boolean approved = resultSet.getBoolean("approved");
                        Boolean deleted = resultSet.getBoolean("deleted");
                        Integer parentId = resultSet.getObject("parentId") == null ? null : resultSet.getInt("parentId");
                        Double PHARM_DISCOUNT = resultSet.getObject("PHARM_DISCOUNT") == null ? null : resultSet.getDouble("PHARM_DISCOUNT");
                        Double IP_DISCOUNT = resultSet.getObject("IP_DISCOUNT") == null ? null : resultSet.getDouble("IP_DISCOUNT");
                        Double OP_DISCOUNT = resultSet.getObject("OP_DISCOUNT") == null ? null : resultSet.getDouble("OP_DISCOUNT");
                        Double BASE_RATE = resultSet.getObject("BASE_RATE") == null ? null : resultSet.getDouble("BASE_RATE");
                        Integer regulator = resultSet.getObject("regulator_id") == null ? null : resultSet.getInt("regulator_id");
                        Double GAP = resultSet.getObject("GAP") == null ? null : resultSet.getDouble("GAP");
                        Double MARGINAL = resultSet.getObject("MARGINAL") == null ? null : resultSet.getDouble("MARGINAL");

                        SPCContract spcContract = new SPCContract(ID, type,
                                insurerLicense, facilityLicense, packageName,
                                clinicianLicense, startDate, endDate, factor,
                                approved, deleted, parentId, PHARM_DISCOUNT,
                                IP_DISCOUNT, OP_DISCOUNT, BASE_RATE, regulator,
                                GAP, MARGINAL);
                        result.add(spcContract);
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
//        log.info(String.format("%s:%s:%s:%s",
//                model,map.get("insurerLicense"), map.get("facilityLicense"), map.get("packageName")));
        return String.format("%s:%s:%s:%s",
                model,map.get("insurerLicense"), map.get("facilityLicense"), map.get("packageName"));
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
