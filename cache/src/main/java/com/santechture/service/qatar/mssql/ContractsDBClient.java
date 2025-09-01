package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santechture.CusContract;
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
public class ContractsDBClient extends DBClientBase {
    @Inject
    ObjectMapper objectMapper;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ContractsDBClient.class);
    private static final String model = "CusContract";
    private static final String sql = "SELECT [ID],\n" +
            "       [priceListId],\n" +
            "       [IR_DRG_priceListId],\n" +
            "       [OP_QOCS_priceListId],\n" +
            "       [IP_SUB_priceListId],\n" +
            "       [Dentistry_ASDSG_priceListId],\n" +
            "       [Radiology_ACHI_priceListId],\n" +
            "       [ER_URG_priceListId],\n" +
            "       [insurer_license],\n" +
            "       [facility_license],\n" +
            "       [package_name],\n" +
            "       [clinician_license],\n" +
            "       [startDate],\n" +
            "       [endDate],\n" +
            "       [approved],\n" +
            "       [deleted],\n" +
            "       [regulator_id],\n" +
            "       [PHARM_DISCOUNT],\n" +
            "       [IP_DISCOUNT],\n" +
            "       [OP_DISCOUNT],\n" +
            "       [BASE_RATE],\n" +
            "       [GAP],\n" +
            "       [MARGINAL],\n" +
            "       [NEGOTIATION_DRG_FACTOR]                                     as IP_DRG_Factor,\n" +
            "       IsNull((Select 1\n" +
            "               from PL_CUS_PriceList\n" +
            "               where PL_CUS_PriceList.ID = priceListId\n" +
            "                 and PL_CUS_PriceList.priceListType = 'Dental'), 0) As isDental,\n" +
            "       multipleProc,\n" +
            "       primaryProc,\n" +
            "       secondaryProc,\n" +
            "       thirdProc,\n" +
            "       forthProc\n" +
            " FROM [PL_CUS_Contract]\n " +
            " where [approved] = 1  and [deleted] = 0";
        private static final List<String> tables = List.of("PL_CUS_Contract");
        private static final String sql_size = " SELECT sum(COALESCE(DATALENGTH([ID]), 0) +\n" +
                "                   COALESCE(DATALENGTH([priceListId]), 0) +\n" +
                "                   COALESCE(DATALENGTH([IR_DRG_priceListId]), 0) +\n" +
                "                   COALESCE(DATALENGTH([OP_QOCS_priceListId]), 0) +\n" +
                "                   COALESCE(DATALENGTH([IP_SUB_priceListId]), 0) +\n" +
                "                   COALESCE(DATALENGTH([Dentistry_ASDSG_priceListId]), 0) +\n" +
                "                   COALESCE(DATALENGTH([Radiology_ACHI_priceListId]), 0) +\n" +
                "                   COALESCE(DATALENGTH([ER_URG_priceListId]), 0) +\n" +
                "                   COALESCE(DATALENGTH([insurer_license]), 0) +\n" +
                "                   COALESCE(DATALENGTH([facility_license]), 0) +\n" +
                "                   COALESCE(DATALENGTH([package_name]), 0) +\n" +
                "                   COALESCE(DATALENGTH([clinician_license]), 0) +\n" +
                "                   COALESCE(DATALENGTH([startDate]), 0) +\n" +
                "                   COALESCE(DATALENGTH([endDate]), 0) +\n" +
                "                   COALESCE(DATALENGTH([approved]), 0) +\n" +
                "                   COALESCE(DATALENGTH([deleted]), 0) +\n" +
                "                   COALESCE(DATALENGTH([regulator_id]), 0) +\n" +
                "                   COALESCE(DATALENGTH([PHARM_DISCOUNT]), 0) +\n" +
                "                   COALESCE(DATALENGTH([IP_DISCOUNT]), 0) +\n" +
                "                   COALESCE(DATALENGTH([OP_DISCOUNT]), 0) +\n" +
                "                   COALESCE(DATALENGTH([BASE_RATE]), 0) +\n" +
                "                   COALESCE(DATALENGTH([GAP]), 0) +\n" +
                "                   COALESCE(DATALENGTH([MARGINAL]), 0) +\n" +
                "                   COALESCE(DATALENGTH([NEGOTIATION_DRG_FACTOR]), 0) +\n" +
                "\n" +
                "                   COALESCE(DATALENGTH(multipleProc), 0) +\n" +
                "                   COALESCE(DATALENGTH(primaryProc), 0) +\n" +
                "                   COALESCE(DATALENGTH(secondaryProc), 0) +\n" +
                "                   COALESCE(DATALENGTH(thirdProc), 0) +\n" +
                "                   COALESCE(DATALENGTH(forthProc), 0) ) / (1024.0) as total_size\n" +
                "             FROM [PL_CUS_Contract]  \n" +
                "             where [approved] = 1 ";
    @Inject
    Logger logger;
    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;

    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ID = resultSet.getLong("ID");
                        Long priceListId = resultSet.getLong("priceListId");
                        Long AR_DRG_priceListId = resultSet.getLong("IR_DRG_priceListId");
                        Long OP_QOCS_priceListId = resultSet.getLong("OP_QOCS_priceListId");
                        Long IP_SUB_priceListId = resultSet.getLong("IP_SUB_priceListId");
                        Long Dentistry_ASDSG_priceListId = resultSet.getLong("Dentistry_ASDSG_priceListId");
                        Long Radiology_ACHI_priceListId = resultSet.getLong("Radiology_ACHI_priceListId");
                        Long ER_URG_priceListId = resultSet.getLong("ER_URG_priceListId");
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
                        CusContract cusContract = new CusContract(ID, priceListId, insurerLicense, facilityLicense, packageName, clinicianLicense, startDate, endDate, approved, deleted, PHARM_DISCOUNT, IP_DISCOUNT, OP_DISCOUNT, BASE_RATE, regulator, GAP, MARGINAL, isDental, multipleProc, primaryProc, secondaryProc, thirdProc, forthProc, ip_DRGFactor);
                        cusContract.setInpatientArDrgPriceListId(AR_DRG_priceListId);
                        cusContract.setOutpatientQocsPriceListId(OP_QOCS_priceListId);
                        cusContract.setInpatientSubacutePriceListId(IP_SUB_priceListId);
                        cusContract.setDentistryAsdsgPriceListId(Dentistry_ASDSG_priceListId);
                        cusContract.setRadiologyAchiPriceListId(Radiology_ACHI_priceListId);
                        cusContract.setEmergencyUrgPriceListId(ER_URG_priceListId);
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
        return String.format("%s:%s:%s:%s", model, map.get("insurerLicense"), map.get("facilityLicense"), map.get("ID"));
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
                        var total_size  = resultSet.getDouble("total_size");
                        if (total_size > 0d ){
                            total_size = Math.round(total_size);
                            if (total_size > 0d) {
                                return total_size;
                            }else {
                                return 1;
                            }
                        }
                    }
                }
            }
        }catch (SQLException e) {
            log.error(e.getMessage());
            for (var stack : e.getStackTrace()){
                log.error(stack.toString());
            }
        }
        return 0;
    }

    @Override
    public Object getObject(JsonNode resultSet) {

        Long ID = resultSet.get("ID") != null ? resultSet.get("ID").asLong() : null;
        Long priceListId = resultSet.get("priceListId") != null ? resultSet.get("priceListId").asLong() : null;
        Long AR_DRG_priceListId = resultSet.get("IR_DRG_priceListId") != null ? resultSet.get("IR_DRG_priceListId").asLong() : null;
        Long OP_QOCS_priceListId = resultSet.get("OP_QOCS_priceListId") != null ? resultSet.get("OP_QOCS_priceListId").asLong() : null;
        Long IP_SUB_priceListId = resultSet.get("IP_SUB_priceListId") != null ? resultSet.get("IP_SUB_priceListId").asLong() : null;
        Long Dentistry_ASDSG_priceListId = resultSet.get("Dentistry_ASDSG_priceListId") != null ? resultSet.get("Dentistry_ASDSG_priceListId").asLong() : null;
        Long Radiology_ACHI_priceListId = resultSet.get("Radiology_ACHI_priceListId") != null ? resultSet.get("Radiology_ACHI_priceListId").asLong() : null;
        Long ER_URG_priceListId = resultSet.get("ER_URG_priceListId") != null ? resultSet.get("ER_URG_priceListId").asLong() : null;
        String insurerLicense = resultSet.get("insurer_license") != null ? resultSet.get("insurer_license").asText() : null;
        String facilityLicense = resultSet.get("facility_license") != null ? resultSet.get("facility_license").asText() : null;
        String packageName = resultSet.get("package_name") != null ? resultSet.get("package_name").asText() : null;
        String clinicianLicense = resultSet.get("clinician_license") != null ? resultSet.get("clinician_license").asText() : null;
        Date startDate = resultSet.get("startDate") != null ? objectMapper.convertValue(resultSet.get("startDate").asText(), Date.class) : null;
        Date endDate = resultSet.get("endDate") != null ? objectMapper.convertValue(resultSet.get("endDate").asText(), Date.class) : null;
        Boolean approved = resultSet.get("approved") !=null ? resultSet.get("approved").asBoolean() : null;
        Boolean deleted = resultSet.get("deleted") !=null ? resultSet.get("deleted").asBoolean() : null;
        Integer regulator = resultSet.get("regulator_id") !=null ? resultSet.get("regulator_id").asInt() : null;
        Double PHARM_DISCOUNT = resultSet.get("PHARM_DISCOUNT") !=null ? resultSet.get("PHARM_DISCOUNT").asDouble() : null;
        Double IP_DISCOUNT = resultSet.get("IP_DISCOUNT") !=null ? resultSet.get("IP_DISCOUNT").asDouble() : null;
        Double OP_DISCOUNT = resultSet.get("OP_DISCOUNT") !=null ? resultSet.get("OP_DISCOUNT").asDouble() : null;
        Double BASE_RATE = resultSet.get("BASE_RATE") !=null ? resultSet.get("BASE_RATE").asDouble() : null;
        Double GAP = resultSet.get("GAP") !=null ? resultSet.get("GAP").asDouble() : null;
        Double MARGINAL = resultSet.get("MARGINAL") !=null ? resultSet.get("MARGINAL").asDouble() : null;
        Integer isDental = resultSet.get("isDental") !=null ? resultSet.get("isDental").asInt() : 0;
        Integer multipleProc = resultSet.get("multipleProc") !=null ? resultSet.get("multipleProc").asInt() : null;
        Double primaryProc = resultSet.get("primaryProc") !=null ? resultSet.get("primaryProc").asDouble() : null;
        Double secondaryProc = resultSet.get("secondaryProc") !=null ? resultSet.get("secondaryProc").asDouble() : null;
        Double thirdProc = resultSet.get("thirdProc") !=null ? resultSet.get("thirdProc").asDouble() : null;
        Double forthProc = resultSet.get("forthProc") !=null ? resultSet.get("forthProc").asDouble() : null;
        Double ip_DRGFactor = resultSet.get("IP_DRG_Factor") !=null ? resultSet.get("IP_DRG_Factor").asDouble() : null;
        CusContract cusContract = new CusContract(ID, priceListId, insurerLicense, facilityLicense, packageName, clinicianLicense, startDate, endDate, approved, deleted, PHARM_DISCOUNT, IP_DISCOUNT, OP_DISCOUNT, BASE_RATE, regulator, GAP, MARGINAL, isDental, multipleProc, primaryProc, secondaryProc, thirdProc, forthProc, ip_DRGFactor);
        cusContract.setInpatientArDrgPriceListId(AR_DRG_priceListId);
        cusContract.setOutpatientQocsPriceListId(OP_QOCS_priceListId);
        cusContract.setInpatientSubacutePriceListId(IP_SUB_priceListId);
        cusContract.setDentistryAsdsgPriceListId(Dentistry_ASDSG_priceListId);
        cusContract.setRadiologyAchiPriceListId(Radiology_ACHI_priceListId);
        cusContract.setEmergencyUrgPriceListId(ER_URG_priceListId);
        return cusContract;
    }
}
