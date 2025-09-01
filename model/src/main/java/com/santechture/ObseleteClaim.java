package com.santechture;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObseleteClaim implements Serializable {
    @JsonProperty(value ="ID", required = true)
    protected Long id;
    @JsonProperty(value ="ClaimLineID")
    protected Long claimLineID;
    @JsonProperty(value ="IDPayer")
    protected String idPayer;
    @JsonProperty(value ="MemberID")
    protected String memberID;
    @JsonProperty(value ="AccumedPatientID")
    protected Long accumedPatientID;
    @JsonProperty(value ="PayerID", required = true)
    protected String payerID;
    @JsonProperty(value ="ProviderID", required = true)
    protected String providerID;
    @JsonProperty(value ="EmiratesIDNumber", required = true)
    protected String emiratesIDNumber;
    @JsonProperty(value ="Gross")
    protected float gross;
    @JsonProperty(value ="PatientShare")
    protected float patientShare;
    @JsonProperty(value ="Net")
    protected float net;
    @JsonProperty(value ="ProviderInvoiceAmount")
    protected float providerInvoiceAmount;
    @JsonProperty(value ="DenialCode")
    protected String denialCode;
    @JsonProperty(value ="PaymentReference")
    protected String paymentReference;
    @JsonProperty(value ="DateSettlement", required = false)
    protected Date dateSettlement;
    @JsonProperty(value ="Encounter", required = true)
    protected List<ObseleteEncounter> encounter;
    @JsonProperty(value ="Diagnosis", required = true)
    protected List<ObseleteDiagnosis> diagnosis;
    @JsonProperty(value ="Activity", required = true)
    protected List<ObseleteActivity> activity;
    @JsonProperty(value ="Resubmission")
    public Long getID() {
        return id;
    }
    public void setID(Long value) {
        this.id = value;
    }
    public Long getClaimLineID() {
        return claimLineID;
    }
    public void setClaimLineID(Long value) {
        this.claimLineID = value;
    }
    public String getIDPayer() {
        return idPayer;
    }
    public void setIDPayer(String value) {
        this.idPayer = value;
    }
    public String getMemberID() {
        return memberID;
    }
    public void setMemberID(String value) {
        this.memberID = value;
    }
    public Long getAccumedPatientID() {
        return accumedPatientID;
    }
    public void setAccumedPatientID(Long value) {
        this.accumedPatientID = value;
    }
    public String getPayerID() {
        return payerID;
    }
    public void setPayerID(String value) {
        this.payerID = value;
    }
    public String getProviderID() {
        return providerID;
    }
    public void setProviderID(String value) {
        this.providerID = value;
    }
    public String getEmiratesIDNumber() {
        return emiratesIDNumber;
    }
    public void setEmiratesIDNumber(String value) {
        this.emiratesIDNumber = value;
    }
    public float getGross() {
        return gross;
    }
    public void setGross(float value) {
        this.gross = value;
    }
    public float getPatientShare() {
        return patientShare;
    }
    public void setPatientShare(float value) {
        this.patientShare = value;
    }
    public float getNet() {
        return net;
    }
    public void setNet(float value) {
        this.net = value;
    }
    public float getProviderInvoiceAmount() {
        return providerInvoiceAmount;
    }
    public void setProviderInvoiceAmount(float value) {
        this.providerInvoiceAmount = value;
    }
    public String getDenialCode() {
        return denialCode;
    }
    public void setDenialCode(String value) {
        this.denialCode = value;
    }
    public String getPaymentReference() {
        return paymentReference;
    }
    public void setPaymentReference(String value) {
        this.paymentReference = value;
    }
    public Date getDateSettlement() {
        return dateSettlement;
    }
    public void setDateSettlement(Date value) {
        this.dateSettlement = value;
    }
    public List<ObseleteEncounter> getEncounter() {
        if (encounter == null) {
            encounter = new ArrayList<ObseleteEncounter>();
        }
        return this.encounter;
    }
    public List<ObseleteDiagnosis> getDiagnosis() {
        if (diagnosis == null) {
            diagnosis = new ArrayList<ObseleteDiagnosis>();
        }
        return this.diagnosis;
    }
    public List<ObseleteActivity> getActivity() {
        if (activity == null) {
            activity = new ArrayList<ObseleteActivity>();
        }
        return this.activity;
    }
    public void setActivity(List<ObseleteActivity> activity) {
        this.activity = activity;
    }
    public void setDiagnosis(List<ObseleteDiagnosis> diagnosis) {
        this.diagnosis = diagnosis;
    }
    public void setEncounter(List<ObseleteEncounter> encounter) {
        this.encounter = encounter;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setIdPayer(String idPayer) {
        this.idPayer = idPayer;
    }
    public Long getId() {
        return id;
    }
    public String getIdPayer() {
        return idPayer;
    }
}
