package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientInsuranceOutcome implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String ruleName;
    private String ruleID;
    private String severity;
    private String longMsg;
    private String shortMsg;
    @JoinColumn(name = "patientInsurance", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private PatientInsurance patientInsurance;
    public PatientInsuranceOutcome() {
    }
    public PatientInsuranceOutcome(Integer id) {
        this.id = id;
    }
    public PatientInsuranceOutcome(String severity, String ruleName, String shortMsg, String longMsg) {
        if (shortMsg.equalsIgnoreCase(longMsg)) {
            longMsg = "E";
        }
        this.ruleName = ruleName;
        this.severity = severity;
        this.longMsg = longMsg;
        this.shortMsg = shortMsg;
        ruleID = "";
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getRuleID() {
        return ruleID;
    }
    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }
    public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    public String getLongMsg() {
        return longMsg;
    }
    public void setLongMsg(String longMsg) {
        this.longMsg = longMsg;
    }
    public String getShortMsg() {
        return shortMsg;
    }
    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public PatientInsurance getPatientInsurance() {
        return patientInsurance;
    }
    public void setPatientInsurance(PatientInsurance patientInsurance) {
        this.patientInsurance = patientInsurance;
    }
}
