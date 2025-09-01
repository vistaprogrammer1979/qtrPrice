package com.santechture.service.qatar.mssql;

import com.fasterxml.jackson.databind.JsonNode;
import com.santechture.Clinician;
import com.santechture.service.qatar.mssql.DBClientBase;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Singleton
public class FacilityCliniciansDBClient extends DBClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FacilityCliniciansDBClient.class);
    private static final String model = "Clinician";
    @Inject
    @DataSource("qatar")
    AgroalDataSource dataSource;
    private static final String sql = "SELECT c.Clinician_License\n"
            + ", c.Profession "
            + ", c.Category "
            + ", c.Facility_License "
            + ", c.Facility_Type "
            + ", c.Status "
            + ", CASE WHEN TRY_CONVERT(datetime, c.Valid_From, 103) IS NOT NULL THEN TRY_CONVERT(datetime, c.Valid_From, 103) ELSE NULL  END AS Valid_From"
            + ", CASE WHEN TRY_CONVERT(datetime, c.Valid_To, 103)  IS  NOT NULL THEN TRY_CONVERT(datetime, c.Valid_To, 103)  ELSE NULL END As  Valid_To "
            + ", c.regulator "
            + "FROM "
            + "ACCUMED_CLINICIANS c";
    private static final List<String> tables = List.of("ACCUMED_CLINICIANS");
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    public List<Object> getData() {
        List<Object> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String license = resultSet.getString("Clinician_License");
                        String facility_license = resultSet.getString("Facility_License");
                        Date validFrom = resultSet.getTimestamp("Valid_From");
                        Date validTo = resultSet.getTimestamp("Valid_To");
                        String status = resultSet.getString("Status");
                        String profession = resultSet.getString("Profession");
                        String category = resultSet.getString("Category");
                        String facility_type = resultSet.getString("Facility_Type");
                        Integer regulator = resultSet.getInt("regulator");
                        result.add(new Clinician(license, facility_license, validFrom,
                                validTo, status, profession, category, facility_type,
                                regulator));
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
    private Date toDate(String dateString) {
        log.info(dateString);
//        try {
//            return FORMATTER.parse(dateString);
//        } catch (ParseException e) {
//            log.error(dateString);
//            log.error(e.getMessage());
            return new Date();
//        }
    }

    @Override
    public String getKey(Map<String, Object> map) {
//        log.info(String.format("%s:%s:%s", model ,map.get("facility_license"),map.get("license")));
        return String.format("%s:%s:%s", model ,map.get("facility_license"),map.get("license"));
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

    @Override
    public Object getObject(JsonNode json) {
        return null;
    }
}
