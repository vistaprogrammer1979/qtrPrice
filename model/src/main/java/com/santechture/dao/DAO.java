/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.santechture.dao;
import com.santechture.request.CoPayment;
import com.santechture.request.Deductible;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author smutlak
 */
public class DAO {

    private Connection dbCon;

    public DAO(Connection dbCon) {
        this.dbCon = dbCon;
    }

    public List<Deductible> getDeductables(Long patientInsuranceID) {
        List<Deductible> ret = new ArrayList<Deductible>();
        try {
            String Sql = "SELECT ACCUMED_PATIENT_DEDUCTABLE_ID,	ACCUMED_PATIENT_INSURANCE_ID, "
                    + " l.[LOOKUP_MEANING] AS [TYPE], DEDUCTABLE_VALUE,	[CEILING], OUT_OF_NETWORK "
                    + " from  [ACCUMED_PATIENT_DEDUCTABLE] d INNER JOIN NEO_LOOKUP_VALUES l on "
                    + " l.LOOKUP_VALUE = d.LOOKUP_VALUE_ID and "
                    + " l.LOOKUP_TYPE_ID = 22  "
                    + "  WHERE [ACCUMED_PATIENT_INSURANCE_ID] " + patientInsuranceID;

            PreparedStatement ps = dbCon.prepareStatement(Sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Long ACCUMED_PATIENT_DEDUCTABLE_ID = rs.getLong("ACCUMED_PATIENT_DEDUCTABLE_ID");
                    Long ACCUMED_PATIENT_INSURANCE_ID = rs.getLong("ACCUMED_PATIENT_INSURANCE_ID");
                    String TYPE = rs.getString("TYPE");
                    Double DEDUCTABLE_VALUE = rs.getDouble("DEDUCTABLE_VALUE");
                    Double CEILING = rs.getDouble("CEILING");
                    Integer OUT_OF_NETWORK = rs.getInt("OUT_OF_NETWORK");
                    

                    Deductible deductable = new Deductible(ACCUMED_PATIENT_DEDUCTABLE_ID, TYPE,
                            OUT_OF_NETWORK, DEDUCTABLE_VALUE, CEILING, null);
                    ret.add(deductable);
                }
                rs.close();
                rs = null;
            }
            ps.close();
            ps = null;

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public List<CoPayment> getCoPayments(Long patientInsuranceID) {
        List<CoPayment> ret = new ArrayList<CoPayment>();
        try {
            String Sql = "SELECT ACCUMED_PATIENT_CO_PAYMENT_ID,	ACCUMED_PATIENT_INSURANCE_ID, "
                    + " l.[LOOKUP_MEANING] AS [TYPE], CO_PAYMENT_VALUE,	[CEILING], OUT_OF_NETWORK "
                    + " from  [ACCUMED_PATIENT_CO_PAYMENT] d INNER JOIN NEO_LOOKUP_VALUES l on "
                    + " l.LOOKUP_VALUE = d.LOOKUP_VALUE_ID and "
                    + " l.LOOKUP_TYPE_ID = 22  "
                    + "  WHERE [ACCUMED_PATIENT_INSURANCE_ID] " + patientInsuranceID;

            PreparedStatement ps = dbCon.prepareStatement(Sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Long ACCUMED_PATIENT_CO_PAYMENT_ID = rs.getLong("ACCUMED_PATIENT_CO_PAYMENT_ID");
                    Long ACCUMED_PATIENT_INSURANCE_ID = rs.getLong("ACCUMED_PATIENT_INSURANCE_ID");
                    String TYPE = rs.getString("TYPE");
                    Double CO_PAYMENT_VALUE = rs.getDouble("CO_PAYMENT_VALUE");
                    Double CEILING = rs.getDouble("CEILING");
                    Integer OUT_OF_NETWORK = rs.getInt("OUT_OF_NETWORK");

                    CoPayment coPayment = new CoPayment(ACCUMED_PATIENT_CO_PAYMENT_ID, TYPE,
                            OUT_OF_NETWORK, CO_PAYMENT_VALUE, CEILING, null);
                    ret.add(coPayment);
                }
                rs.close();
                rs = null;

            }
            ps.close();
            ps = null;

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
}
