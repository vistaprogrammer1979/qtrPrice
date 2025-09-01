package com.santechture.service.uae.mssql;

import com.santechture.CodeGroup;
import com.santechture.GroupCodesRange;
import com.santechture.SPCContract;
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
public class CodeGroupsDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CodeGroupsDBClient.class);
    private static final String model = "CodeGroup";
    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;
    private static final String sql ="SELECT c.ID as id ,c.NAME as name,c.TYPE as type ,[VERSION] , g.[FROM] as f ,g.[TO] as t "
            + "FROM"
            + " ACCUMED_GROUP_CODES  g right  JOIN     ACCUMED_CODE_GROUPS  c"
            + " ON g.GROUP_ID=c.ID"
            + " where c.name in (\n"
            + "Select distinct name from \n"
            + "  ACCUMED_CODE_GROUPS where ID in(\n"
            + " Select distinct group_id from PL_SPC_GROUP_FACTOR)\n"
            + " OR NAME in ('Anaesthesia',\n"
            + " 'Evaluation And Management',\n"
            + " 'Physical Medicine & Rehabilitation',\n"
            + " 'Radiology',\n"
            + " 'Pathology & Laboratory',\n"
            + " 'DGTest_CPT_9_Series',\n"
            + " 'Maternity Care & Delivery',\n"
            + " 'Ophthalmology', 'Anaesthesia', 'Antenatal-Screening ICD_10', "
            + "'Antenatal-Screening ICD_9', 'Dental Consultation', 'Orthodontic Procedures', "
            + "'Prosthodontics_removable', 'Prosthodontics_fixed', 'Consultations', 'X-RAY',"
            + "'Anesthesia Discount Exclusion', 'OT Discount Exclusion', 'DSL codes')"
            + ") ORDER   BY   c.ID";
    private static final List<String> tables = List.of(
            "ACCUMED_CODE_GROUPS",
            "ACCUMED_GROUP_CODES",
            "PL_SPC_GROUP_FACTOR"
           );

    @Override
    public String getKey(Map<String, Object> map) {
//        log.info(String.format("%s:%s:%s", model,map.get("name"), map.get("code")));
        return String.format("%s:%s:%s", model,map.get("name"), map.get("code"));
    }

    @Override
    public List<Object> getData() {
        List<Object> list = new ArrayList<>();
        Map<String, CodeGroup> childrenCodes = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String version = resultSet.getString("VERSION");
                        String code = resultSet.getString("f");
                        String to = resultSet.getString("t");
                        CodeType ttype = null;
                        if ("CPT".equalsIgnoreCase(type)) {
                            ttype = CodeType.CPT;
                        }
                        if ("HCPCS".equalsIgnoreCase(type)) {
                            ttype = CodeType.HCPCS;
                        }
                        if ("DRUG".equalsIgnoreCase(type)) {
                            ttype = CodeType.TRADE_DRUG;
                        }
                        if ("DENTAL".equalsIgnoreCase(type)) {
                            ttype = CodeType.DENTAL;
                        }
                        if ("SERVICE".equalsIgnoreCase(type)) {
                            ttype = CodeType.SERVICE;
                        }
                        if ("IR_DRUG".equalsIgnoreCase(type)) {
                            ttype = CodeType.IR_DRG;
                        }
                        if ("GENERIC_DRUG".equalsIgnoreCase(type)) {
                            ttype = CodeType.GENERIC_DRUG;
                        }

                        if ("ICD".equalsIgnoreCase(type)) {
                            if (version != null) {
                                if ("ICD-10".equalsIgnoreCase(version)) {
                                    ttype = CodeType.ICD10;
                                }
                                if ("ICD-9".equalsIgnoreCase(version)) {
                                    ttype = CodeType.ICD9;
                                }
                            }
                        }
                        CodeGroup codeGroup = new CodeGroup(ttype, id, name, code);
                        list.add(codeGroup);
                        if (to != null) {

                            String sql2 = "";
                            if("CPT".equalsIgnoreCase(type)){
                                childrenCodes.put("CPT_"+code+"_"+to+"_"+name, codeGroup);
                            } else if ("DENTAL".equalsIgnoreCase(type)) {
                                childrenCodes.put("DENTAL_"+code+"_"+to+"_"+name, codeGroup);
                            }
                            else if ("HCPCS".equalsIgnoreCase(type)) {
                                log.info(code + "_HCPCS_");
                                childrenCodes.put("HCPCS_"+code+"_"+to+"_"+name, codeGroup);
                            }
                            else if ("SERVICE".equalsIgnoreCase(type)) {
                                log.info(code + "_SERVICE");
                                childrenCodes.put("SERVICE_"+code+"_"+to+"_"+name, codeGroup);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            for (var stack : e.getStackTrace()){
                log.error(stack.toString());
            }
//            log.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
        if (!childrenCodes.isEmpty()){
            for (String chr : childrenCodes.keySet()){
                String[] parts = chr.split("_");
                String code = parts[1];
                String to = parts[2];
                CodeGroup codeGroup = childrenCodes.get(chr);

                String sql2 = "";
                if ("CPT".equals(parts[0])){
                    sql2 = String.format("SELECT code FROM %s where code >= '%s' and code <= '%s'", "ACCUMED_HAAD_CPTS", code, to);
                } else if ("DENTAL".equals(parts[0])) {
                    sql2 = String.format("SELECT code FROM %s where code >= '%s' and code <= '%s'", "ACCUMED_DENTAL_CODES", code, to);
                }
                else if ("HCPCS".equals(parts[0])) {
                    sql2 = String.format("select code  from ACCUMED_HAAD_HCPCS\n" +
                                    " where code >= '%s' and code <= '%s'"+
                            "union \n" +
                            "select HCPC   as code from ACCUMED_DHA_HCPCS\n" +
                            " where HCPC >= '%s' and HCPC <= '%s'", code, to, code, to);
                }
                else if ("SERVICE".equals(parts[0])) {
                    sql2 = String.format("select code from ACCUMED_SERVICE_CODES  where code >= '%s' and  code <= '%s'\n" +
                            "union\n" +
                            "select code  from ACCUMED_DHA_SERVICE_CODE where  code >= '%s' and  code <= '%s' \n" +
                            "union\n" +
                            "select code  from ACCUMED_SERVICE where  code >= '%s' and  code <= '%s'", code, to, code, to, code, to);
                }
                try (Connection connection2 = dataSource.getConnection()) {
                    try (PreparedStatement statement2 = connection2.prepareStatement(sql2)) {
                        try (ResultSet resultSet2 = statement2.executeQuery()) {
                            while (resultSet2.next()) {
                                String childCode = resultSet2.getString("code");
                                list.add(new CodeGroup(
                                        codeGroup.getType(),
                                        codeGroup.getId(),
                                        codeGroup.getName(),
                                        childCode
                                        ));
                            }
                        }
                    }
                }catch (SQLException e){
                    log.error(e.getMessage());
                    for (var stack : e.getStackTrace()){
                        log.error(stack.toString());
                    }
                }
            }
        }
        return list;
    }
    public String getModel(){
            return model;
    }
    public List<String> getTables(){
        return tables;
    }

}
