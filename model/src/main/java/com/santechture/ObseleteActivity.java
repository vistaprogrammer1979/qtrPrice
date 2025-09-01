package com.santechture;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObseleteActivity {
    @JsonProperty(value ="AccumedID")
    protected long accumedID;
    @JsonProperty(value ="ID")
    protected String id;
    @JsonProperty(value ="Start", required = true)
    protected Date start;
    @JsonProperty(value ="Type", required = true)
    protected BigInteger type;
    @JsonProperty(value ="Code", required = true)
    protected String code;
    @JsonProperty(value ="Quantity")
    protected float quantity;
    @JsonProperty(value ="Net")
    protected float net;
    @JsonProperty(value ="ProviderNet")
    protected float providerNet;
    @JsonProperty(value ="Clinician", required = true)
    protected String clinician;
    @JsonProperty(value ="PriorAuthorizationID")
    protected String priorAuthorizationID;
    @JsonProperty(value ="List")
    protected Float list;
    @JsonProperty(value ="Gross")
    protected Float gross;
    @JsonProperty(value ="PatientShare")
    protected Float patientShare;
    @JsonProperty(value ="PaymentAmount")
    protected float paymentAmount;
    @JsonProperty(value ="DenialCode")
    protected String denialCode;
    @JsonProperty(value ="Observation")
    public long getAccumedID() {
        return accumedID;
    }
    public void setAccumedID(long value) {
        this.accumedID = value;
    }
    public String getID() {
        return id;
    }
    public void setID(String value) {
        this.id = value;
    }
    public Date getStart() {
        return start;
    }
    public void setStart(Date value) {
        this.start = value;
    }
    public BigInteger getType() {
        return type;
    }
    public void setType(BigInteger value) {
        this.type = value;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String value) {
        this.code = value;
    }
    public float getQuantity() {
        return quantity;
    }
    public void setQuantity(float value) {
        this.quantity = value;
    }
    public float getNet() {
        return net;
    }
    public void setNet(float value) {
        this.net = value;
    }
    public float getProviderNet() {
        return providerNet;
    }
    public void setProviderNet(float value) {
        this.providerNet = value;
    }
    public String getClinician() {
        return clinician;
    }
    public void setClinician(String value) {
        this.clinician = value;
    }
    public String getPriorAuthorizationID() {
        return priorAuthorizationID;
    }
    public void setPriorAuthorizationID(String value) {
        this.priorAuthorizationID = value;
    }
    public Float getList() {
        return list;
    }
    public void setList(Float value) {
        this.list = value;
    }
    public Float getGross() {
        return gross;
    }
    public void setGross(Float value) {
        this.gross = value;
    }
    public Float getPatientShare() {
        return patientShare;
    }
    public void setPatientShare(Float value) {
        this.patientShare = value;
    }
    public float getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(float value) {
        this.paymentAmount = value;
    }
    public String getDenialCode() {
        return denialCode;
    }
    public void setDenialCode(String value) {
        this.denialCode = value;
    }
    public void setId(String id) {
        this.id = id;
    }
}
