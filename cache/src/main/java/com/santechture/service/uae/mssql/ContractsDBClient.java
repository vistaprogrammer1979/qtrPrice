package com.santechture.service.uae.mssql;

import com.santechture.CusContract;
import com.santechture.SPCContract;
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
public class ContractsDBClient extends DBClientBase{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ContractsDBClient.class);
    private static final String model = "CusContract";
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql = "SELECT [ID],[priceListId],[insurer_license],[facility_license],[package_name],[clinician_license],[startDate] "
            + " ,[endDate],[approved],[deleted],[regulator_id],[PHARM_DISCOUNT],[IP_DISCOUNT],[OP_DISCOUNT],[BASE_RATE] "
            //Change back vpande
            + " ,[GAP],[MARGINAL],[NEGOTIATION_DRG_FACTOR] as IP_DRG_Factor,  "
            + " IsNull((Select 1 from PL_CUS_PriceList where PL_CUS_PriceList.ID=priceListId and PL_CUS_PriceList.priceListType='Dental'), 0) As isDental"
            + "  , multipleProc, primaryProc, secondaryProc, thirdProc, forthProc "
            + " FROM [PL_CUS_Contract] "
            + " where [approved] =1 ";
    private static final List<String> tables = List.of("PL_CUS_Contract");

    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Long priceListId = resultSet.getLong("priceListId");
                        String insurerLicense = resultSet.getString("insurer_license");
                        String facilityLicense = resultSet.getString("facility_license");
                        String packageName = resultSet.getString("package_name");
                        String clinicianLicense = resultSet.getString("clinician_license");
                        Date startDate = resultSet.getTimestamp("startDate");
                        Date endDate = resultSet.getTimestamp("endDate");
                        Boolean approved = resultSet.getBoolean("approved");
                        Boolean deleted = resultSet.getBoolean("deleted");
                        Integer regulator = resultSet.getInt("regulator_id");
                        Double PHARM_DISCOUNT = resultSet.getDouble("PHARM_DISCOUNT");
                        Double IP_DISCOUNT = resultSet.getDouble("IP_DISCOUNT");
                        Double OP_DISCOUNT = resultSet.getDouble("OP_DISCOUNT");
                        Double BASE_RATE = resultSet.getDouble("BASE_RATE");
                        Double GAP = resultSet.getDouble("GAP");
                        Double MARGINAL = resultSet.getDouble("MARGINAL");
                        Integer isDental = resultSet.getInt("isDental");

                        Integer multipleProc = resultSet.getInt("multipleProc");
                        Double primaryProc = resultSet.getDouble("primaryProc");
                        Double secondaryProc = resultSet.getDouble("secondaryProc");
                        Double thirdProc = resultSet.getDouble("thirdProc");
                        Double forthProc = resultSet.getDouble("forthProc");
                        Double ip_DRGFactor = resultSet.getDouble("IP_DRG_Factor");
                        CusContract cusContract = new CusContract(ID, priceListId,
                                insurerLicense, facilityLicense, packageName,
                                clinicianLicense, startDate, endDate,
                                approved, deleted, PHARM_DISCOUNT,
                                IP_DISCOUNT, OP_DISCOUNT, BASE_RATE, regulator,
                                GAP, MARGINAL, isDental,
                                multipleProc, primaryProc,
                                secondaryProc, thirdProc, forthProc, ip_DRGFactor);
                        result.add(cusContract);
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

}
