package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santechture.converter.DateDeserializer;
import com.santechture.converter.DateSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Encounter implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="idCaller", required = false)
    private Integer idCaller;
    @JsonProperty(value ="FacilityID", required = true)
    private String facilityID;
    @JsonProperty(value ="Type", required = true)
    private Integer type;
    @JsonProperty(value ="PatientID", required = true)
    private String patientID;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="Start", required = false)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date start;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="End", required = false)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date end;
    @JsonProperty(value ="StartType")
    private Integer startType;
    @JsonProperty(value ="EndType")
    private Integer endType;
    @JsonProperty(value ="TransferSource")
    private String transferSource;
    @JsonProperty(value ="TransferDestination")
    private String transferDestination;
    @JsonProperty(value ="NewPatient")
    private Integer newPatient;
    @JsonProperty(value ="orderingClinician", required = false)
    private String orderingClinician;
    @OneToOne(mappedBy = "encounterID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Authorisation")
    private Authorisation authorisation;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Claim claimID;
    @JsonProperty(value = "inchargeDoctorSpecialty", required = false)
    private String inchargeDoctorSpecialty;
    @JsonProperty(value = "dispenseLocation", required = false)
    private String dispenseLocation;
    //added By Rasha
    @JsonProperty(value = "careType", required = false)
    private String careType;
    @JsonProperty(value = "encounterClinic", required = false)
    private String encounterClinic;
    @JsonProperty(value = "location", required = false)
    private String location;
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<EncounterOutcome> outcome;
    @JsonProperty(value="referralClinic")
    private String referralClinic;
    @JsonProperty(value="lengthOfStay")
    private Double lengthOfStay;


    public String getReferralClinic() {
        return referralClinic;
    }

    public void setReferralClinic(String referralClinic) {
        this.referralClinic = referralClinic;
    }

    public Encounter() {
    }
    public Encounter(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getIdCaller() {
        return idCaller;
    }
    public void setIdCaller(Integer idCaller) {
        this.idCaller = idCaller;
    }
    public String getFacilityID() {
        return facilityID;
    }
    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getPatientID() {
        return patientID;
    }
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }
    public Integer getStartType() {
        return startType;
    }
    public void setStartType(Integer startType) {
        this.startType = startType;
    }
    public Integer getEndType() {
        return endType;
    }
    public void setEndType(Integer endType) {
        this.endType = endType;
    }
    public String getTransferSource() {
        return transferSource;
    }
    public void setTransferSource(String transferSource) {
        this.transferSource = transferSource;
    }
    public String getTransferDestination() {
        return transferDestination;
    }
    public void setTransferDestination(String transferDestination) {
        this.transferDestination = transferDestination;
    }
    public Integer getNewPatient() {
        return newPatient;
    }
    public void setNewPatient(Integer newPatient) {
        this.newPatient = newPatient;
    }
    public Authorisation getAuthorisation() {
        return authorisation;
    }
    public void setAuthorisation(Authorisation authorisation) {
        this.authorisation = authorisation;
    }
    public Claim getClaimID() {
        return claimID;
    }
    public void setClaimID(Claim claimID) {
        this.claimID = claimID;
    }
    public List<EncounterOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<EncounterOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new EncounterOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<EncounterOutcome>();
            this.outcome.add(new EncounterOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
    public String getOrderingClinician() {
        return orderingClinician;
    }
    public void setOrderingClinician(String orderingClinician) {
        this.orderingClinician = orderingClinician;
    }

    public String getInchargeDoctorSpecialty() {
        return inchargeDoctorSpecialty;
    }

    public void setInchargeDoctorSpecialty(String inchargeDoctorSpecialty) {
        this.inchargeDoctorSpecialty = inchargeDoctorSpecialty;
    }

    public String getDispenseLocation() {
        return dispenseLocation;
    }

    public void setDispenseLocation(String dispenseLocation) {
        this.dispenseLocation = dispenseLocation;
    }

    public String getCareType() {
        return careType;
    }

    public void setCareType(String careType) {
        this.careType = careType;
    }
    public String getEncounterClinic() {
        return encounterClinic;
    }

    public void setEncounterClinic(String encounterClinic) {
        this.encounterClinic = encounterClinic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLengthOfStay() {
        return lengthOfStay;
    }

    public void setLengthOfStay(Double lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }
}
