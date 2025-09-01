package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.CodeGroup;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class CodeGroupsDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CodeGroupsDBClient.class);
    private static final String model = "CodeGroup";
    private static final String sql = "SELECT c.ID as id ,\n" + "       c.NAME as name,\n" + "       c.TYPE as type ,\n" + "       [VERSION] ,\n" + "       g.[FROM] as f ,\n" + "       g.[TO] as t\n" + " ,c.PARENT_ID as parent_id \n"+  "FROM\n" + "    ACCUMED_GROUP_CODES g\n" + "        right JOIN ACCUMED_CODE_GROUPS c ON g.GROUP_ID=c.ID\n" + "ORDER BY c.ID;";
    private static final String size_sql = "SELECT\n" + "    sum ( COALESCE(DATALENGTH(c.ID),0) +\n" + "    COALESCE(DATALENGTH(c.NAME),0) +\n" + "    COALESCE(DATALENGTH(c.TYPE),0) +\n" + "    COALESCE(DATALENGTH([VERSION]),0) +\n" + "    COALESCE(DATALENGTH(g.[FROM]),0) +\n" + "    COALESCE(DATALENGTH(g.[TO]),0))/(1024.0) as total_size\n" + "\n" + "FROM\n" + "    ACCUMED_GROUP_CODES g\n" + "right JOIN\n" + "        ACCUMED_CODE_GROUPS c ON g.GROUP_ID=c.ID";
    private static final List<String> tables = List.of("ACCUMED_CODE_GROUPS", "ACCUMED_GROUP_CODES");
    @Inject
    Logger logger;
    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;

    @Override
    public String getKey(Map<String, Object> map) {
//        log.info(String.format("%s:%s:%s", model,map.get("name"), map.get("code")));
        return String.format("%s:%s:%s:%s", model,map.get("type"), map.get("name"), map.get("code"));
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
                        Integer parentId = (Integer) resultSet.getObject("parent_id") ;
                        boolean isParent = ((Integer) resultSet.getObject("parent_id")) != null;
                        CodeType ttype = null;
                        if ("Staff Health Insurance Charge Type".equalsIgnoreCase(type)) {
                            ttype = CodeType.StaffHealthInsuranceCharge;
                        }
                        if ("Clinical Procedure".equalsIgnoreCase(type)) {
                            ttype = CodeType.Clinical;
                        }
                        if ("General".equalsIgnoreCase(type)) {
                            ttype = CodeType.General;
                        }
                        if ("Lab Supplies".equalsIgnoreCase(type)) {
                            ttype = CodeType.LabSupplies;
                        }
                        if ("Room & Bed".equalsIgnoreCase(type)) {
                            ttype = CodeType.RoomAndBed;
                        }
                        if ("INVESTIGATION".equalsIgnoreCase(type)) {
                            ttype = CodeType.INVESTIGATION;
                        }
                        if ("Other".equalsIgnoreCase(type)) {
                            ttype = CodeType.Other;
                        }
                        if ("Pharmaceticals and Supplies".equalsIgnoreCase(type)) {
                            ttype = CodeType.PharmaceticalsAndSupplies;
                        }
                        if ("onc".equalsIgnoreCase(type)) {
                            ttype = CodeType.onc;
                        }
                        if ("Packaged Services".equalsIgnoreCase(type)) {
                            ttype = CodeType.PackagedServices;
                        }
                        if ("Staff Health Insurance Charge Type".equalsIgnoreCase(type)) {
                            ttype = CodeType.StaffHealthInsuranceCharge;
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
                        if (code != null && name != null) {
                            CodeGroup codeGroup = new CodeGroup(ttype, id, name.trim(), code.trim());
                            codeGroup.setIsParent(isParent);
                            list.add(codeGroup);
                        }

                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            for (var stack : e.getStackTrace()) {
                log.error(stack.toString());
            }
//            log.error(Arrays.toString(e.getStackTrace()));
            return null;
        }

        return list;
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
            try (PreparedStatement statement = connection.prepareStatement(size_sql)) {
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
        } catch (SQLException e) {
            log.error(e.getMessage());
            for (var stack : e.getStackTrace()) {
                log.error(stack.toString());
            }
        }
        return 0D;
    }

    @Override
    public Object getObject(JsonNode json) {
        return null;
    }

}
