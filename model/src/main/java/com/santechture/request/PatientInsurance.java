package com.santechture.request;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientInsurance implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value = "idCaller", required = false)
    private Integer idCaller;
    @JsonProperty(value = "PATIENT_INSURANCE_ID", required = true)
    private String patientInsuranceId;
    @JsonProperty(value = "RELATION_TO", required = true)
    private String relationTo;
    @JsonProperty(value = "PRINCIPLE_INSURANCE_ID", required = false)
    private String principleInsuranceId;
    @JsonProperty(value = "INSURANCE_LISENCE", required = true)
    private String insuranceLisence;
    @JsonProperty(value = "PACKAGE_NAME", required = true)
    private String packageName;
    @JsonProperty(value = "NetworkName")
    private String networkName;
    @JsonProperty(value = "NetworkId")
    private Integer networkId;
    @JsonProperty(value = "SubNetworkName")
    private String subNetworkName;
    @JsonProperty(value = "SubNetworkId")
    private Integer subNetworkId;
    @JsonProperty(value = "PlanName")
    private String planName;
    @JsonProperty(value = "PlanId")
    private Integer planId;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value = "START_DATE", required = true)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value = "RENEWAL_DATE")
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date renewalDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value = "END_DATE", required = true)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date endDate;
    @JsonProperty(value = "GROSS_PREMIUM")
    private float grossPremium;
    @JsonProperty(value = "POLICY_HOLDER_TYPE_ID")
    private String policyHolderTypeId;
    @JsonProperty(value = "PRINCIPLE_INSURANCE_NUMBER")
    private String principleInsuranceNumber;
    @JsonProperty(value = "IS_EXPIRED", required = true)
    private Integer isExpired;
    @JsonProperty(value = "Policy_Number")
    private String policyNumber;
    @JsonProperty(value = "verified", required = true)
    private Integer verified;
    @JsonProperty(value = "ADMISSION_TYPE", required = true)
    private Integer admissionType;
    @JsonProperty(value = "IS_PENDING", required = true)
    private Integer isPending;
    @JsonProperty(value = "MATERNITY", required = false)
    private Integer maternity;
    @JsonProperty(value = "DENTAL", required = false)
    private Integer dental;
    @JsonProperty(value = "OPTICAL", required = false)
    private Integer optical;
    @JsonProperty(value = "IPMaxPatientShare", required = false)
    private Double ipMaxPatientShare;
    @JsonProperty(value = "OPMaxPatientShare", required = false)
    private Double opMaxPatientShare;
    @JoinColumn(name = "patientID", referencedColumnName = "ID")
    @OneToOne
    @JsonIgnore
    private Patient patientID;
    @OneToMany(mappedBy = "patientInsurance", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value = "CoPayment", required = false)
    private List<CoPayment> coPayment;
    @OneToMany(mappedBy = "patientInsurance", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value = "CoInsurance", required = false)
    private List<CoInsurance> coinsurance;
    @OneToMany(mappedBy = "patientInsurance", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value = "Deductible", required = false)
    private List<Deductible> deductible;
    @OneToMany(mappedBy = "patientInsurance", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value = "Outcome", required = false)
    private List<PatientInsuranceOutcome> outcome;
    @JsonProperty(value = "healthcardValidity", required = false)
    private String healthcardValidity;
    @JsonProperty(value = "CardType")
    private Integer cardType;
    @JsonProperty(value = "claimTotal")
    private Double claimTotal;
    @JsonProperty(value = "IPCopayment", required = false)
    private Double ipCopayment;
    @JsonProperty(value = "OPCopayment", required = false)
    private Double opCopayment;

    public PatientInsurance() {
    }

    public PatientInsurance(Integer id) {
        this.id = id;
    }

    public Double getIpCopayment() {
        return ipCopayment;
    }
    public Double getValueIP(Double gross, Double deductible) {

        return (((double) Math.round((((gross
                - ((deductible == null || deductible.isNaN()) ? 0 : deductible)) / 100) * this.ipCopayment) * 100)) / 100);
    }
    public Double getValueOP(Double gross, Double deductible) {

        return (((double) Math.round((((gross
                - ((deductible == null || deductible.isNaN()) ? 0 : deductible)) / 100) * this.opCopayment) * 100)) / 100);
    }
    public Double getOpCopayment() {
        return opCopayment;
    }

    public void setOpCopayment(Double opCopayment) {
        this.opCopayment = opCopayment;
    }

    public void setIpCopayment(Double ipCopayment) {
        this.ipCopayment = ipCopayment;
    }

    public Double getClaimTotal() {
        return claimTotal;
    }

    public void setClaimTotal(Double claimTotal) {
        this.claimTotal = claimTotal;
    }

    public Double getIpMaxPatientShare() {
        return ipMaxPatientShare;
    }

    public void setIpMaxPatientShare(Double ipMaxPatientShare) {
        this.ipMaxPatientShare = ipMaxPatientShare;
    }
    public Double getOpMaxPatientShare() {
        return opMaxPatientShare;
    }

    public void setOpMaxPatientShare(Double opMaxPatientShare) {
        this.opMaxPatientShare = opMaxPatientShare;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getIdCaller() {
        return idCaller;
    }

    public void setIdCaller(Integer idCaller) {
        this.idCaller = idCaller;
    }

    public String getPatientInsuranceId() {
        return patientInsuranceId;
    }

    public void setPatientInsuranceId(String patientInsuranceId) {
        this.patientInsuranceId = patientInsuranceId;
    }

    public String getRelationTo() {
        return relationTo;
    }

    public void setRelationTo(String relationTo) {
        this.relationTo = relationTo;
    }

    public String getPrincipleInsuranceId() {
        return principleInsuranceId;
    }

    public void setPrincipleInsuranceId(String principleInsuranceId) {
        this.principleInsuranceId = principleInsuranceId;
    }

    public String getInsuranceLisence() {
        return insuranceLisence;
    }

    public void setInsuranceLisence(String insuranceLisence) {
        this.insuranceLisence = insuranceLisence;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    public String getSubNetworkName() {
        return subNetworkName;
    }

    public void setSubNetworkName(String subNetworkName) {
        this.subNetworkName = subNetworkName;
    }

    public Integer getSubNetworkId() {
        return subNetworkId;
    }

    public void setSubNetworkId(Integer subNetworkId) {
        this.subNetworkId = subNetworkId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getGrossPremium() {
        return grossPremium;
    }

    public void setGrossPremium(float grossPremium) {
        this.grossPremium = grossPremium;
    }

    public String getPolicyHolderTypeId() {
        return policyHolderTypeId;
    }

    public void setPolicyHolderTypeId(String policyHolderTypeId) {
        this.policyHolderTypeId = policyHolderTypeId;
    }

    public String getPrincipleInsuranceNumber() {
        return principleInsuranceNumber;
    }

    public void setPrincipleInsuranceNumber(String principleInsuranceNumber) {
        this.principleInsuranceNumber = principleInsuranceNumber;
    }

    public Integer getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Integer getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType(Integer admissionType) {
        this.admissionType = admissionType;
    }

    public Integer getIsPending() {
        return isPending;
    }

    public void setIsPending(Integer isPending) {
        this.isPending = isPending;
    }

    public Integer getMaternity() {
        return maternity;
    }

    public void setMaternity(Integer maternity) {
        this.maternity = maternity;
    }

    public Integer getDental() {
        return dental;
    }

    public void setDental(Integer dental) {
        this.dental = dental;
    }

    public Integer getOptical() {
        return optical;
    }

    public void setOptical(Integer optical) {
        this.optical = optical;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Patient getPatientID() {
        return patientID;
    }

    public void setPatientID(Patient patientID) {
        this.patientID = patientID;
    }

    public List<PatientInsuranceOutcome> getOutcome() {
        return outcome;
    }

    public void setOutcome(List<PatientInsuranceOutcome> outcome) {
        this.outcome = outcome;
    }

    public List<CoPayment> getCoPayment() {
        return coPayment;
    }

    public void setCoPayment(List<CoPayment> coPayment) {
        this.coPayment = coPayment;
    }

    public List<Deductible> getDeductible() {
        return deductible;
    }

    public void setDeductible(List<Deductible> deductible) {
        this.deductible = deductible;
    }

    public String getHealthcardValidity() {
        return healthcardValidity;
    }

    public void setHealthcardValidity(String healthcardValidity) {
        this.healthcardValidity = healthcardValidity;
    }

    public List<CoInsurance> getCoinsurance() {
        return coinsurance;
    }

    public void setCoinsurance(List<CoInsurance> coinsurance) {
        this.coinsurance = coinsurance;
    }
}
