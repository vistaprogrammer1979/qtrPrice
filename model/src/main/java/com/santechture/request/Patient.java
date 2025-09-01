package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santechture.converter.DateDeserializer;
import com.santechture.converter.DateSerializer;
import com.santechture.converter.GenderDeserializer;
import com.santechture.converter.GenderSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="idCaller", required = false)
    private Integer idCaller;
    @JsonProperty(value ="PATIENT_NAME")
    private String patientName;
    @JsonProperty(value ="PATIENT_SURNAME")
    private String patientSurname;
    @JsonProperty(value ="GENDER_ID", required = true)
    @JsonSerialize(using = GenderSerializer.class)
    @JsonDeserialize(using = GenderDeserializer.class)
    private String genderId;
    @JsonProperty(value ="EMIRATES_ID")
    private String emiratesId;
    @JsonProperty(value ="PASSPORT_ID")
    private String passportId;
    @JsonProperty(value ="DRIVING_LICENSE")
    private String drivingLicense;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="DATE_OF_BIRTH", required = true)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date dateOfBirth;
    @JsonProperty(value ="NATIONALITY")
    private String nationality;
    @JsonProperty(value ="MARTIAL_STATUS")
    private String martialStatus;
    @JsonProperty(value ="EMIRATE_TYPES", required = true)
    private Integer emirateTypes;
    @OneToOne(mappedBy = "patientID", cascade = CascadeType.PERSIST)
    @JsonProperty(value ="PatientInsurance")
    private PatientInsurance patientInsurance;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @OneToOne
    @JsonIgnore
    private Claim claimID;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<PatientOutcome> outcome;
    @JsonProperty(value = "age", required = false)
    private Integer age;
    @JsonProperty(value = "billingGroup", required = false)
    private String billingGroup;
    @JsonProperty(value = "subBillingGroup", required = false)
    private String subBillingGroup;
    public Patient() {
    }
    public Patient(Integer id) {
        this.id = id;
    }
    public Integer getIdCaller() {
        return idCaller;
    }
    public void setIdCaller(Integer idCaller) {
        this.idCaller = idCaller;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public String getPatientSurname() {
        return patientSurname;
    }
    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }
    public String getGenderId() {
        return genderId;
    }
    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }
    public String getEmiratesId() {
        return emiratesId;
    }
    public void setEmiratesId(String emiratesId) {
        this.emiratesId = emiratesId;
    }
    public String getPassportId() {
        return passportId;
    }
    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }
    public String getDrivingLicense() {
        return drivingLicense;
    }
    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getMartialStatus() {
        return martialStatus;
    }
    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }
    public Integer getEmirateTypes() {
        return emirateTypes;
    }
    public void setEmirateTypes(Integer emirateTypes) {
        this.emirateTypes = emirateTypes;
    }
    public PatientInsurance getPatientInsurance() {
        return patientInsurance;
    }
    public void setPatientInsurance(PatientInsurance patientInsurance) {
        this.patientInsurance = patientInsurance;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Claim getClaimID() {
        return claimID;
    }
    public void setClaimID(Claim claimID) {
        this.claimID = claimID;
    }
    public List<PatientOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<PatientOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new PatientOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<PatientOutcome>();
            this.outcome.add(new PatientOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBillingGroup() {
        return billingGroup;
    }

    public void setBillingGroup(String billingGroup) {
        this.billingGroup = billingGroup;
    }

    public String getSubBillingGroup() {
        return subBillingGroup;
    }

    public void setSubBillingGroup(String subBillingGroup) {
        this.subBillingGroup = subBillingGroup;
    }
}
