package com.santechture.request;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoPayment implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String type;
    @JsonProperty(value ="IP", required = false)
    private boolean ip;
    @JsonProperty(value ="OP", required = false)
    private boolean op;
    @JsonProperty(value ="OutNet", required = false)
    private int outNet;
    @JsonProperty(value ="Value", required = false)
    private Double value;
    @JsonProperty(value ="Ceiling", required = false)
    private Double ceiling;
    @JsonProperty(value ="PreAuth", required = false)
    private String preAuth;
    @JsonIgnore
    private Double claimTotal;
    @JsonProperty(value ="GroupType", required = false)
    private int groupType;
    @JoinColumn(name = "patientInsurance", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private PatientInsurance patientInsurance;
    public CoPayment() {
        claimTotal=0.0d;
    }
    public CoPayment(Long id) {
        this.id = id;
    }
    public CoPayment(Long id, String type, int outNet, Double value, Double ceiling, PatientInsurance patientInsurance) {
        this.id = id;
        this.type = type;
        this.outNet = outNet;
        this.value = value;
        this.ceiling = ceiling;
        this.patientInsurance = patientInsurance;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean isOp() {
        return op;
    }
    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public void setOp(boolean op) {
        this.op = op;
    }

    public boolean isIp() {
        return ip;
    }

    public void setIp(boolean ip) {
        this.ip = ip;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getOutNet() {
        return outNet;
    }
    public void setOutNet(int outNet) {
        this.outNet = outNet;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public Double getCeiling() {
        return ceiling;
    }
    public void setCeiling(Double ceiling) {
        this.ceiling = ceiling;
    }
    public PatientInsurance getPatientInsurance() {
        return patientInsurance;
    }
    public void setPatientInsurance(PatientInsurance patientInsurance) {
        this.patientInsurance = patientInsurance;
    }
    public Double getValue(Double gross, Double deductible){
        return (((double)Math.round((((gross -
                ((deductible == null || deductible.isNaN())?0:deductible)
        ) / 100) * this.value)*100))/100);
    }
    public Double getClaimTotal() {
        return claimTotal;
    }
    public void setClaimTotal(Double claimTotal) {
        this.claimTotal = claimTotal;
    }
    public String getPreAuth() {
        return preAuth;
    }
    public void setPreAuth(String preAuth) {
        this.preAuth = preAuth;
    }
}
