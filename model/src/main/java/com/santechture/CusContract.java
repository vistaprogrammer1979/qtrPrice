package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.santechture.request.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

@Builder
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CusContract {
    CusContract originalContract;
    @JsonProperty(value = "ID", required = true)
    private Long ID;
    @JsonProperty(value = "priceListId")
    private Long priceListId;
    @JsonProperty(value = "inpatientArDrgPriceListId", required = true)
    private Long inpatientArDrgPriceListId;
    @JsonProperty(value = "outpatientQocsPriceListId", required = true)
    private Long outpatientQocsPriceListId;
    @JsonProperty(value = "dentistryAsdsgPriceListId", required = true)
    private Long dentistryAsdsgPriceListId;
    @JsonProperty(value = "radiologyAchiPriceListId", required = true)
    private Long radiologyAchiPriceListId;
    @JsonProperty(value = "emergencyUrgPriceListId", required = true)
    private Long emergencyUrgPriceListId;
    @JsonProperty(value = "inpatientSubacutePriceListId", required = true)
    private Long inpatientSubacutePriceListId;
    @JsonProperty(value = "insurerLicense", required = true)
    private String insurerLicense;
    @JsonProperty(value = "facilityLicense", required = true)

    private String facilityLicense;
    @JsonProperty(value = "packageName", required = true)
    private String packageName;
    @JsonProperty(value = "clinicianLicense", required = true)
    private String clinicianLicense;
    @JsonProperty(value = "startDate", required = true)
    private Date startDate;
    @JsonProperty(value = "endDate", required = true)
    private Date endDate;
    @JsonProperty(value = "approved", required = true)
    private Boolean approved;
    @JsonProperty(value = "deleted", required = true)
    private Boolean deleted;
    @JsonProperty(value = "PHARM_DISCOUNT", required = true)
    private Double PHARM_DISCOUNT;
    @JsonProperty(value = "IP_DISCOUNT", required = true)
    private Double IP_DISCOUNT;
    @JsonProperty(value = "OP_DISCOUNT", required = true)
    private Double OP_DISCOUNT;
    @JsonProperty(value = "BASE_RATE", required = true)
    private Double BASE_RATE;
    @JsonProperty(value = "regulator", required = true)
    private Integer regulator;
    @JsonProperty(value = "GAP", required = true)
    private Double GAP;
    @JsonProperty(value = "MARGINAL", required = true)
    private Double MARGINAL;
    @JsonProperty(value = "dental", required = true)
    private Boolean dental;
    @JsonProperty(value = "multipleProcedure", required = true)
    private Integer multipleProcedure;
    @JsonProperty(value = "primaryProcedure", required = true)
    private Double primaryProcedure;
    @JsonProperty(value = "secondaryProcedure", required = true)
    private Double secondaryProcedure;
    @JsonProperty(value = "thirdProcedure", required = true)
    private Double thirdProcedure;
    @JsonProperty(value = "forthProcedure", required = true)
    private Double forthProcedure;
    @JsonProperty(value = "IP_DRGFactor", required = true)
    private Double IP_DRGFactor;
    @JsonProperty(value = "status", required = true)
    private Status status;
    @JsonProperty(value = "updatedStatus", required = true)
    private Status updatedStatus;
    @JsonProperty(value = "hspcsMarkUp", required = true)
    private Double hspcsMarkUp;
    private Double roomLimit;
    private Double noneSaudiVat;
    private String className;
    private String policyName;
    private Double priorApprovalLimit;
    private Boolean ipSuspension;
    private Boolean opSuspension;
    private String companyCode;
    private Double saudiVat;
    private Double OP_PHARM_DISCOUNT;
    private Double IP_PHARM_DISCOUNT;
    private Double opCopayment;
    private Double opMaxPatientShare;
    private Double ipCopayment;
    private Double ipMaxPatientShare;
    private String roomType;


    public CusContract() {
    }

    public CusContract(Long ID, Long priceListId, String insurerLicense, String facilityLicense, String packageName, String clinicianLicense, Date startDate, Date endDate, Boolean approved, Boolean deleted, Double PHARM_DISCOUNT, Double IP_DISCOUNT, Double OP_DISCOUNT, Double BASE_RATE, Integer regulator, Double GAP, Double MARGINAL, Integer dental, Integer multipleProcedure, Double primaryProcedure, Double secondaryProcedure, Double thirdProcedure, Double forthProcedure, Double IP_DRGFactor, Double hspcsMarkUp) {
        this.ID = ID;
        this.priceListId = priceListId;
        this.insurerLicense = insurerLicense;
        this.facilityLicense = facilityLicense;
        this.packageName = packageName;
        this.clinicianLicense = clinicianLicense;
        this.startDate = startDate;
        this.endDate = endDate;
        this.approved = approved;
        this.deleted = deleted;
        this.PHARM_DISCOUNT = PHARM_DISCOUNT;
        this.IP_DISCOUNT = IP_DISCOUNT;
        this.OP_DISCOUNT = OP_DISCOUNT;
        this.BASE_RATE = BASE_RATE;
        this.regulator = regulator;
        this.GAP = GAP;
        this.MARGINAL = MARGINAL;
        this.dental = (dental == 1);
        this.multipleProcedure = multipleProcedure;
        this.primaryProcedure = primaryProcedure;
        this.secondaryProcedure = secondaryProcedure;
        this.thirdProcedure = thirdProcedure;
        this.forthProcedure = forthProcedure;
        this.IP_DRGFactor = IP_DRGFactor;
        this.hspcsMarkUp = hspcsMarkUp;
    }

    public CusContract(Long ID, Long priceListId, String insurerLicense, String facilityLicense, String packageName, String clinicianLicense, Date startDate, Date endDate, Boolean approved, Boolean deleted, Double PHARM_DISCOUNT, Double IP_DISCOUNT, Double OP_DISCOUNT, Double BASE_RATE, Integer regulator, Double GAP, Double MARGINAL, Integer dental, Integer multipleProc, Double primaryProc, Double secondaryProc, Double thirdProc, Double forthProc, Double hspcsMarkUp) {
        this.ID = ID;
        this.priceListId = priceListId;
        this.insurerLicense = insurerLicense;
        this.facilityLicense = facilityLicense;
        this.packageName = packageName;
        this.clinicianLicense = clinicianLicense;
        this.startDate = startDate;
        this.endDate = endDate;
        this.approved = approved;
        this.deleted = deleted;
        this.PHARM_DISCOUNT = PHARM_DISCOUNT;
        this.IP_DISCOUNT = IP_DISCOUNT;
        this.OP_DISCOUNT = OP_DISCOUNT;
        this.BASE_RATE = BASE_RATE;
        this.regulator = regulator;
        this.GAP = GAP;
        this.MARGINAL = MARGINAL;
        this.dental = (dental == 1);
        this.multipleProcedure = multipleProc;
        this.primaryProcedure = primaryProc;
        this.secondaryProcedure = secondaryProc;
        this.thirdProcedure = thirdProc;
        this.forthProcedure = forthProc;
        this.hspcsMarkUp = hspcsMarkUp;

    }

    public Double getOpCopayment() {
        return opCopayment;
    }

    public void setOpCopayment(Double opCopayment) {
        this.opCopayment = opCopayment;
    }

    public Double getOpMaxPatientShare() {
        return opMaxPatientShare;
    }

    public void setOpMaxPatientShare(Double opMaxPatientShare) {
        this.opMaxPatientShare = opMaxPatientShare;
    }

    public Double getIpCopayment() {
        return ipCopayment;
    }

    public void setIpCopayment(Double ipCopayment) {
        this.ipCopayment = ipCopayment;
    }

    public Double getIpMaxPatientShare() {
        return ipMaxPatientShare;
    }

    public void setIpMaxPatientShare(Double ipMaxPatientShare) {
        this.ipMaxPatientShare = ipMaxPatientShare;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Double getPriorApprovalLimit() {
        return priorApprovalLimit;
    }

    public void setPriorApprovalLimit(Double priorApprovalLimit) {
        this.priorApprovalLimit = priorApprovalLimit;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getIpSuspension() {
        return ipSuspension;
    }

    public void setIpSuspension(Boolean ipSuspension) {
        this.ipSuspension = ipSuspension;
    }

    public Boolean getOpSuspension() {
        return opSuspension;
    }

    public void setOpSuspension(Boolean opSuspension) {
        this.opSuspension = opSuspension;
    }

    public CusContract getOriginalContract() {
        return originalContract;
    }

    public void setOriginalContract(CusContract originalContract) {
        this.originalContract = originalContract;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Double getSaudiVat() {
        return saudiVat;
    }

    public void setSaudiVat(Double saudiVat) {
        this.saudiVat = saudiVat;
    }

    public Double getOP_PHARM_DISCOUNT() {
        return OP_PHARM_DISCOUNT;
    }

    public void setOP_PHARM_DISCOUNT(Double OP_PHARM_DISCOUNT) {
        this.OP_PHARM_DISCOUNT = OP_PHARM_DISCOUNT;
    }

    public Double getIP_PHARM_DISCOUNT() {
        return IP_PHARM_DISCOUNT;
    }

    public void setIP_PHARM_DISCOUNT(Double IP_PHARM_DISCOUNT) {
        this.IP_PHARM_DISCOUNT = IP_PHARM_DISCOUNT;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Double getNoneSaudiVat() {
        return noneSaudiVat;
    }

    public void setNoneSaudiVat(Double noneSaudiVat) {
        this.noneSaudiVat = noneSaudiVat;
    }

    public Double getRoomLimit() {
        return roomLimit;
    }

    public void setRoomLimit(Double roomLimit) {
        this.roomLimit = roomLimit;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(Long priceListId) {
        this.priceListId = priceListId;
    }

    public Double getIP_DRGFactor() {
        return IP_DRGFactor;
    }

    public void setIP_DRGFactor(Double IP_DRGFactor) {
        this.IP_DRGFactor = IP_DRGFactor;
    }

    public String getInsurerLicense() {
        return insurerLicense;
    }

    public void setInsurerLicense(String insurerLicense) {
        this.insurerLicense = insurerLicense;
    }

    public String getFacilityLicense() {
        return facilityLicense;
    }

    public void setFacilityLicense(String facilityLicense) {
        this.facilityLicense = facilityLicense;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClinicianLicense() {
        return clinicianLicense;
    }

    public void setClinicianLicense(String clinicianLicense) {
        this.clinicianLicense = clinicianLicense;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Double getPHARM_DISCOUNT() {
        return PHARM_DISCOUNT;
    }

    public void setPHARM_DISCOUNT(Double PHARM_DISCOUNT) {
        this.PHARM_DISCOUNT = PHARM_DISCOUNT;
    }

    public Double getIP_DISCOUNT() {
        return IP_DISCOUNT;
    }

    public void setIP_DISCOUNT(Double IP_DISCOUNT) {
        this.IP_DISCOUNT = IP_DISCOUNT;
    }

    public Double getOP_DISCOUNT() {
        return OP_DISCOUNT;
    }

    public void setOP_DISCOUNT(Double OP_DISCOUNT) {
        this.OP_DISCOUNT = OP_DISCOUNT;
    }

    public Double getBASE_RATE() {
        return BASE_RATE;
    }

    public void setBASE_RATE(Double BASE_RATE) {
        this.BASE_RATE = BASE_RATE;
    }

    public Integer getRegulator() {
        return regulator;
    }

    public void setRegulator(Integer regulator) {
        this.regulator = regulator;
    }

    public Double getGAP() {
        return GAP;
    }

    public void setGAP(Double GAP) {
        this.GAP = GAP;
    }

    public Double getMARGINAL() {
        return MARGINAL;
    }

    public void setMARGINAL(Double MARGINAL) {
        this.MARGINAL = MARGINAL;
    }

    public Boolean isDental() {
        return dental;
    }

    public Integer isMultipleProcedure() {
        return multipleProcedure;
    }

    public Integer getMultipleProcedure() {
        return multipleProcedure;
    }

    public void setMultipleProcedure(Integer multipleProcedure) {
        this.multipleProcedure = multipleProcedure;
    }

    public Double getPrimaryProcedure() {
        return primaryProcedure;
    }

    public void setPrimaryProcedure(Double primaryProcedure) {
        this.primaryProcedure = primaryProcedure;
    }

    public Double getSecondaryProcedure() {
        return secondaryProcedure;
    }

    public void setSecondaryProcedure(Double secondaryProcedure) {
        this.secondaryProcedure = secondaryProcedure;
    }

    public Double getThirdProcedure() {
        return thirdProcedure;
    }

    public void setThirdProcedure(Double thirdProcedure) {
        this.thirdProcedure = thirdProcedure;
    }

    public Double getForthProcedure() {
        return forthProcedure;
    }

    public void setForthProcedure(Double forthProcedure) {
        this.forthProcedure = forthProcedure;
    }

    public Boolean getDental() {
        return dental;
    }

    public void setDental(Boolean dental) {
        this.dental = dental;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getUpdatedStatus() {
        return updatedStatus;
    }

    public void setUpdatedStatus(Status updatedStatus) {
        this.updatedStatus = updatedStatus;
    }

    public Double getHspcsMarkUp() {
        return hspcsMarkUp;
    }

    public void setHspcsMarkUp(Double hspcsMarkUp) {
        this.hspcsMarkUp = hspcsMarkUp;
    }

    public Long getInpatientArDrgPriceListId() {
        return inpatientArDrgPriceListId;
    }

    public void setInpatientArDrgPriceListId(Long inpatientArDrgPriceListId) {
        this.inpatientArDrgPriceListId = inpatientArDrgPriceListId;
    }



    public Long getDentistryAsdsgPriceListId() {
        return dentistryAsdsgPriceListId;
    }

    public void setDentistryAsdsgPriceListId(Long dentistryAsdsgPriceListId) {
        this.dentistryAsdsgPriceListId = dentistryAsdsgPriceListId;
    }

    public Long getRadiologyAchiPriceListId() {
        return radiologyAchiPriceListId;
    }

    public void setRadiologyAchiPriceListId(Long radiologyAchiPriceListId) {
        this.radiologyAchiPriceListId = radiologyAchiPriceListId;
    }

    public Long getEmergencyUrgPriceListId() {
        return emergencyUrgPriceListId;
    }

    public void setEmergencyUrgPriceListId(Long emergencyUrgPriceListId) {
        this.emergencyUrgPriceListId = emergencyUrgPriceListId;
    }

    public Long getInpatientSubacutePriceListId() {
        return inpatientSubacutePriceListId;
    }

    public void setInpatientSubacutePriceListId(Long inpatientSubacutePriceListId) {
        this.inpatientSubacutePriceListId = inpatientSubacutePriceListId;
    }

    public Long getOutpatientQocsPriceListId() {
        return outpatientQocsPriceListId;
    }

    public void setOutpatientQocsPriceListId(Long outpatientQocsPriceListId) {
        this.outpatientQocsPriceListId = outpatientQocsPriceListId;
    }
}
