package com.santechture;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObseleteEncounter {
    @JsonProperty(value ="AccumedID")
    protected long accumedID;
    @JsonProperty(value ="FacilityID", required = true)
    protected String facilityID;
    @JsonProperty(value ="Type", required = true)
    protected BigInteger type;
    @JsonProperty(value ="PatientID", required = true)
    protected String patientID;
    @JsonProperty(value ="Start", required = false)
    protected Date start;
    @JsonProperty(value ="End", required = false)
    protected Date end;
    @JsonProperty(value ="StartType")
    protected BigInteger startType;
    @JsonProperty(value ="EndType")
    protected BigInteger endType;
    @JsonProperty(value ="TransferSource")
    protected String transferSource;
    @JsonProperty(value ="TransferDestination")
    protected String transferDestination;
    public long getAccumedID() {
        return accumedID;
    }
    public void setAccumedID(long value) {
        this.accumedID = value;
    }
    public String getFacilityID() {
        return facilityID;
    }
    public void setFacilityID(String value) {
        this.facilityID = value;
    }
    public BigInteger getType() {
        return type;
    }
    public void setType(BigInteger value) {
        this.type = value;
    }
    public String getPatientID() {
        return patientID;
    }
    public void setPatientID(String value) {
        this.patientID = value;
    }
    public Date getStart() {
        return start;
    }
    public void setStart(Date value) {
        this.start = value;
    }
    public Date getEnd() {
        return end;
    }
    public void setEnd(Date value) {
        this.end = value;
    }
    public BigInteger getStartType() {
        return startType;
    }
    public void setStartType(BigInteger value) {
        this.startType = value;
    }
    public BigInteger getEndType() {
        return endType;
    }
    public void setEndType(BigInteger value) {
        this.endType = value;
    }
    public String getTransferSource() {
        return transferSource;
    }
    public void setTransferSource(String value) {
        this.transferSource = value;
    }
    public String getTransferDestination() {
        return transferDestination;
    }
    public void setTransferDestination(String value) {
        this.transferDestination = value;
    }
}
