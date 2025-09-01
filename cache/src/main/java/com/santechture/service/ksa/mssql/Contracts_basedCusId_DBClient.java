package com.santechture.service.ksa.mssql;

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
public class Contracts_basedCusId_DBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Contracts_basedCusId_DBClient.class);
    private static final String model = "CusContract";
    @Inject
    Logger logger;

    @Inject
    @DataSource("ksa")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT CONTRACT.[ID],CONTRACT.[priceListId],CONTRACT.[insurer_license],CONTRACT.[facility_license],CONTRACT.[package_name] "
            + " ,CONTRACT.[clinician_license],CONTRACT.[startDate] "
            + " ,CONTRACT.[endDate],CONTRACT.[approved],CONTRACT.[deleted],CONTRACT.[regulator_id],CONTRACT.[PHARM_DISCOUNT],CONTRACT.[IP_DISCOUNT] "
            + " ,CONTRACT.[OP_DISCOUNT],CONTRACT.[BASE_RATE] "
            + " ,CONTRACT.[GAP],CONTRACT.[MARGINAL], "
            + " IsNull((Select 1 from PL_CUS_PriceList where PL_CUS_PriceList.ID=CONTRACT.priceListId and PL_CUS_PriceList.priceListType='Dental'), 0) As isDental "
            + "  ,CONTRACT. multipleProc,CONTRACT. primaryProc, CONTRACT.secondaryProc, CONTRACT.thirdProc,CONTRACT. forthProc ,CONTRACT.[IP_PHARM_DISCOUNT],CONTRACT.[OP_PHARM_DISCOUNT] ,CONTRACT.SAUDI_VAT,CONTRACT.NONE_SAUDI_VAT "
            + " ,ltrim(rtrim(CONTRACT.policy_name )) as policy_name,ltrim(rtrim(CONTRACT.class_name)) as class_name,CONTRACT.ip_Copayment,CONTRACT.op_Copayment,CONTRACT.IP_MAX_PATIENT_SHARE,CONTRACT.OP_MAX_PATIENT_SHARE  , CONTRACT.ROOM_LIMIT, CONTRACT.PRIOR_APPROVAL_LIMIT,CONTRACT.ROOM_TYPE ,CONTRACT.COMPANY_CODE, ISNULL( INS.IP_SUSPENSION,CONTRACT.IP_SUSPENSION) AS IP_SUSPENSION,ISNULL( INS.OP_SUSPENSION,CONTRACT.OP_SUSPENSION) AS OP_SUSPENSION "
            + " FROM [PL_CUS_Contract] AS CONTRACT LEFT JOIN ACCUMED_INSURERS AS INS ON CONTRACT.insurer_license= INS.AUTH_NO "
            + " where CONTRACT.[approved] =1   and CONTRACT.deleted = 0 ";
    private static final List<String> tables = List.of("PL_CUS_Contract","ACCUMED_INSURERS","");

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
                        String policyName = resultSet.getString("policy_name");
                        String className = resultSet.getString("class_name");
                        String clinicianLicense = resultSet.getString("clinician_license");
                        Date startDate = resultSet.getTimestamp("startDate");
                        Date endDate = resultSet.getTimestamp("endDate");
                        Boolean approved = resultSet.getBoolean("approved");
                        Boolean deleted = resultSet.getBoolean("deleted");
                        Boolean ipSuspension = resultSet.getBoolean("IP_SUSPENSION");
                        Boolean opSuspension = resultSet.getBoolean("OP_SUSPENSION");
                        Integer regulator = resultSet.getInt("regulator_id");
                        Double PHARM_DISCOUNT = resultSet.getObject("PHARM_DISCOUNT") == null ? null : resultSet.getDouble("PHARM_DISCOUNT");
                        Double IP_PHARM_DISCOUNT = resultSet.getObject("IP_PHARM_DISCOUNT") == null ? null : resultSet.getDouble("IP_PHARM_DISCOUNT");
                        Double OP_PHARM_DISCOUNT = resultSet.getObject("OP_PHARM_DISCOUNT") == null ? null : resultSet.getDouble("OP_PHARM_DISCOUNT");
                        Double IP_DISCOUNT = resultSet.getObject("IP_DISCOUNT") == null ? null : resultSet.getDouble("IP_DISCOUNT");
                        Double OP_DISCOUNT = resultSet.getObject("OP_DISCOUNT") == null ? null : resultSet.getDouble("OP_DISCOUNT");
                        Double BASE_RATE = resultSet.getObject("BASE_RATE") == null ? null : resultSet.getDouble("BASE_RATE");
                        Double GAP = resultSet.getObject("GAP") == null ? null : resultSet.getDouble("GAP");
                        Double MARGINAL = resultSet.getObject("MARGINAL") == null ? null : resultSet.getDouble("MARGINAL");
                        Integer isDental = resultSet.getObject("isDental") == null ? null : resultSet.getInt("isDental");
                        Double saudiVat = resultSet.getObject("SAUDI_VAT") != null ? resultSet.getDouble("SAUDI_VAT") : null;
                        Double noneSaudiVat = resultSet.getObject("NONE_SAUDI_VAT") != null ? resultSet.getDouble("NONE_SAUDI_VAT") : null;
                        Integer multipleProc = resultSet.getObject("multipleProc") == null ? null : resultSet.getInt("multipleProc");
                        Double primaryProc = resultSet.getObject("primaryProc") == null ? null : resultSet.getDouble("primaryProc");
                        Double secondaryProc = resultSet.getObject("secondaryProc") == null ? null : resultSet.getDouble("secondaryProc");
                        Double thirdProc = resultSet.getObject("thirdProc") == null ? null : resultSet.getDouble("thirdProc");
                        Double forthProc = resultSet.getObject("forthProc") == null ? null : resultSet.getDouble("forthProc");
                        Double ipCopayment = resultSet.getObject("ip_Copayment") == null ? null : resultSet.getDouble("ip_Copayment");
                        Double opCopayment = resultSet.getObject("op_Copayment") == null ? null : resultSet.getDouble("op_Copayment");
                        Double ipMaxPatientShare = resultSet.getObject("IP_MAX_PATIENT_SHARE") == null ? null : resultSet.getDouble("IP_MAX_PATIENT_SHARE");
                        Double opMaxPatientShare = resultSet.getObject("OP_MAX_PATIENT_SHARE") == null ? null : resultSet.getDouble("OP_MAX_PATIENT_SHARE");
                        Double priorLimit = resultSet.getObject("ROOM_LIMIT") == null ? null : resultSet.getDouble("ROOM_LIMIT");
                        Double priorApprovalLimit = resultSet.getObject("PRIOR_APPROVAL_LIMIT") != null ? resultSet.getDouble("PRIOR_APPROVAL_LIMIT") : null;
                        String roomType = resultSet.getString("ROOM_TYPE");
                        String companyCode = resultSet.getString("COMPANY_CODE");
                        CusContract cusContract = new CusContract(ID, priceListId,
                                insurerLicense, facilityLicense, packageName,
                                clinicianLicense, startDate, endDate,
                                approved, deleted, PHARM_DISCOUNT,
                                IP_DISCOUNT, OP_DISCOUNT, BASE_RATE, regulator,
                                GAP, MARGINAL, isDental,
                                multipleProc, primaryProc,
                                secondaryProc, thirdProc, forthProc, IP_PHARM_DISCOUNT, OP_PHARM_DISCOUNT);
                        cusContract.setSaudiVat(saudiVat);
                        cusContract.setNoneSaudiVat(noneSaudiVat);
                        cusContract.setPolicyName(policyName);
                        cusContract.setClassName(className);
                        cusContract.setOpCopayment(opCopayment);
                        cusContract.setOpMaxPatientShare(opMaxPatientShare);
                        cusContract.setIpCopayment(ipCopayment);
                        cusContract.setIpMaxPatientShare(ipMaxPatientShare);
                        cusContract.setRoomType(roomType);
                        cusContract.setPriorApprovalLimit(priorApprovalLimit);
                        cusContract.setCompanyCode(companyCode);
                        cusContract.setRoomLimit(priorLimit);
                        cusContract.setIpSuspension(ipSuspension);
                        cusContract.setOpSuspension(opSuspension);
                        cusContract.setStatus(com.santechture.request.Status.VALID);
//                        cusContract.setOriginalContract(cusContract);
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
        return String.format("%s:ID:%s",
                model, map.get("ID"));
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
