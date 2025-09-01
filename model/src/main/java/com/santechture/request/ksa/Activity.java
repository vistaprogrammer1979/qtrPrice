package com.santechture.request.ksa;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santechture.converter.ksa.CodeTypeDeserializer;
import com.santechture.converter.ksa.CodeTypeSerializer;
import com.santechture.request.Observation;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="ID", required = true)
    protected String activityID;
    @JsonProperty(value ="idCaller", required = false)
    private Integer idCaller;
    @JsonProperty(value ="priceActivityId", required = false)
    private UUID priceActivityId;
    @JsonProperty(value ="SPCFactor", required = false)
    private Double SPCFactor;
    @JsonProperty(value ="DiscountPercentage", required = false)
    private Double discountPercentage;
    @JsonProperty(value ="Discount", required = false)
    private Double discount;
    @JsonProperty(value ="discountAmount", required = false)
    private Double discountAmount;
    @JsonProperty(value ="denyDisc", required = false)
    private Double denyDisc;
    @JsonProperty(value ="hike", required = false)
    private Double hike;
    @JsonProperty(value ="hikeAmount", required = false)
    private Double hikeAmount;
    @JsonProperty(value ="SpecialDiscountAmount", required = false)
    private Double specialDiscountAmount;
    @JsonProperty(value ="SpecialDiscountPercentage", required = false)
    private Double specialDiscountPercentage;
    @JsonProperty(value ="DrugType", required = false)
    private Integer drugType;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="Start", required = true)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date start;
    @JsonProperty(value ="Type", required = true)
    @JsonSerialize(using = CodeTypeSerializer.class)
    @JsonDeserialize(using = CodeTypeDeserializer.class)
    private CodeType type;
    @JsonProperty(value ="Code", required = true)
    private String code;
    @JsonProperty(value ="Custom_Price_Types", required = true)
    private String custom_Price_Types;
    @JsonProperty(value ="Rejected_Amount")
    private Double  rejected_Amount;
    @JsonProperty(value ="Quantity")
    private Double quantity;
    @JsonProperty(value ="Net")
    private Double net;
    @JsonProperty(value ="ProviderNet", required = false)
    private Double providerNet;
    @JsonProperty(value ="Clinician", required = true)
    private String clinician;
    @JsonProperty(value ="OrderingClinician", required = false)
    private String orderingClinician;
    @JsonProperty(value ="PriorAuthorizationID")
    private String priorAuthorizationID;
    @JsonProperty(value ="Drg_weight", required = false)
    private Double drg_weight;
    @JsonProperty(value ="TimeUnits", required = false)
    private Integer timeUnits;
    @JsonProperty(value ="AnaesthesiaBaseUnits")
    private Double anaesthesiaBaseUnits;
    @JsonProperty(value ="List")
    private Double list;
    @JsonProperty(value ="Gross")
    private Double gross;
    @JsonProperty(value ="PatientShare")
    private Double patientShare;
    @JsonProperty(value ="PaymentAmount")
    private Double paymentAmount;
    @JsonProperty(value ="DenialCode")
    private String denialCode;
    @JsonProperty(value ="Copayment")
    private Double copayment;
    @JsonProperty(value ="deductible")
    private Double deductible;
    @JsonProperty(value ="deductibleOn")
    private Double deductibleOn;
    @JsonProperty(value ="deductibleAmount")
    private Double deductibleAmount;
    @JsonProperty(value ="deductibleType")
    private Double deductibleType;
    @JsonProperty(value ="maxdeductibleAmt")
    private Double maxdeductibleAmt;
    @JsonProperty(value ="maxCoverage")
    private Double maxCoverage;
    @JsonProperty(value ="coveragePeriod")
    private Double coveragePeriod;
    @JsonProperty(value ="EX_PBP")
    private Double EX_PBP;
    @JsonProperty(value ="ManualPrices")
    private Boolean manualPrices;
    @Column(name = "packageGroupID")
    @JsonProperty(value = "packageGroupID")
    private Integer  packageGroupID;
    @JsonProperty(value ="ProcedureType")
    private Integer procedureType;
    @JsonProperty(value ="providerType", required = false)
    private Integer providerType;
    @JsonProperty(value ="providerCode", required = false)
    private String providerCode;
    @JsonProperty(value ="providerPatientShare", required = false)
    private Double providerPatientShare;
    @OneToMany(mappedBy = "activityID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="ActivityGroup")
    private List<ActivityGroup> activityGroup;
    @OneToMany(mappedBy = "activityID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Observation")
    private List<Observation> observation;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Claim claimID;
    @JsonProperty(value ="Cash")
    private Boolean cash;
    @JsonIgnore
    private Integer listPricePredifined;
    @OneToMany(mappedBy = "activity", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<ActivityOutcome> outcome;
    @JsonProperty(value = "serviceItemSerialNumber", required = false)
    private String serviceItemSerialNumber;
    @JsonProperty(value="srvitmslnum")
    private Integer srvitmslnum;
    @JsonProperty(value="NeedApproval")
    private Boolean needApproval;

    @JsonProperty(value= "TotalNetAmount", required = false)
    private Double totalNetAmount;
    @JsonProperty(value= "PatientShareDiscountAmount", required = false)
    private Double patientShareDiscountAmount;
    @JsonProperty(value="PendingAmount")
    private Double pendingAmount;
    @JsonProperty(value="NetAmountVat", required = false)
    private Double netAmountVat;
    @JsonProperty(value="NoneSaudiVatPercentage", required = false)
    private Double noneSaudiVatPercentage;
    @JsonProperty(value="NoneSaudiVat", required = false)
    private Double noneSaudiVat;
    @JsonProperty(value="TotalPatientShare", required = false)
    private Double totalPatientShare;
    @JsonProperty(value="RejectedAmount")
    private Double rejectedAmount;

    @JsonProperty(value= "ApprovedAmount")
    private Double approvedAmount;
    @JsonProperty(value="PendingQuantity")
    private Double pendingQuantity;

    @JsonProperty(value= "GrossPlusVat")
    private Double grossPlusVat;
    @JsonProperty(value= "RejectedQuantity")
    private Double rejectedQuantity;
    @JsonProperty(value=  "Covered")
    private Boolean covered;
    @JsonProperty(value= "SaudiVat", required = false)
    private Double saudiVat;
    @JsonProperty(value= "SaudiVatPercentage", required = false)
    private Double saudiVatPercentage;

    @JsonProperty(value="PatientShareDiscountPercentage", required = false)
    private Double patientShareDiscountPercentage;
    @JsonProperty(value=  "CopaymentAlreadyAdded")
    private Boolean copaymentAlreadyAdded;
    @JsonProperty(value=  "Billed")
    private Boolean billed;
    @JsonProperty(value="modifier")
    private String modifier;

    @JsonProperty(value= "TotalPrice")
    private Double totalPrice;

    @JsonProperty(value = "PatientShareVat", required = false)
    private Double patientShareVat;

    @JsonProperty(value = "DiscountPercentageProcedure", required = false)
    private Double discountPercentageProcedure;
    @JsonProperty(value = "DiscountProcedure", required = false)
    private Double discountProcedure;


/*
    public Double getGrossPlusVat() {
        return grossPlusVat;
    }

    public void setGrossPlusVat(Double grossPlusVat) {
        this.grossPlusVat = grossPlusVat;
    }

    public Double getSaudiVat() {
        return saudiVat;
    }

    public void setSaudiVat(Double saudiVat) {
        this.saudiVat = saudiVat;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getSaudiVatPercentage() {
        return saudiVatPercentage;
    }

    public void setSaudiVatPercentage(Double saudiVatPercentage) {
        this.saudiVatPercentage = saudiVatPercentage;
    }

    public Boolean getBilled() {
        return billed;
    }

    public void setBilled(Boolean billed) {
        this.billed = billed;
    }

    public Boolean getCopaymentAlreadyAdded() {
        return copaymentAlreadyAdded;
    }

    public void setCopaymentAlreadyAdded(Boolean copaymentAlreadyAdded) {
        this.copaymentAlreadyAdded = copaymentAlreadyAdded;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Double getDiscountProcedure() {
        return discountProcedure;
    }

    public void setDiscountProcedure(Double discountProcedure) {
        this.discountProcedure = discountProcedure;
    }

    public Double getDiscountPercentageProcedure() {
        return discountPercentageProcedure;
    }

    public void setDiscountPercentageProcedure(Double discountPercentageProcedure) {
        this.discountPercentageProcedure = discountPercentageProcedure;
    }

    public boolean isNeedApproval() {
        return needApproval;
    }

    public void setNeedApproval(boolean needApproval) {
        this.needApproval = needApproval;
    }

    public Boolean getCovered() {
        return covered;
    }

    public void setCovered(Boolean covered) {
        this.covered = covered;
    }

    public Double getPatientShareDiscountPercentage() {
        return patientShareDiscountPercentage;
    }

    public void setPatientShareDiscountPercentage(Double patientShareDiscountPercentage) {
        this.patientShareDiscountPercentage = patientShareDiscountPercentage;
    }

    public Double getPatientShareDiscountAmount() {
        return patientShareDiscountAmount;
    }

    public void setPatientShareDiscountAmount(Double patientShareDiscountAmount) {
        this.patientShareDiscountAmount = patientShareDiscountAmount;
    }

    public Double getTotalNetAmount() {
        return totalNetAmount;
    }

    public void setTotalNetAmount(Double totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }



    public Double getPatientShareVat() {
        return patientShareVat;
    }

    public void setPatientShareVat(Double patientShareVat) {
        this.patientShareVat = patientShareVat;
    }

    public Double getNoneSaudiVat() {
        return noneSaudiVat;
    }

    public void setNoneSaudiVat(Double noneSaudiVat) {
        this.noneSaudiVat = noneSaudiVat;
    }

    public Double getNoneSaudiVatPercentage() {
        return noneSaudiVatPercentage;
    }

    public void setNoneSaudiVatPercentage(Double noneSaudiVatPercentage) {
        this.noneSaudiVatPercentage = noneSaudiVatPercentage;
    }

    public Double getNetAmountVat() {
        return netAmountVat;
    }

    public void setNetAmountVat(Double netAmountVat) {
        this.netAmountVat = netAmountVat;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public Double getRejectedAmount() {
        return rejectedAmount;
    }

    public void setRejectedAmount(Double rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

     */

    public String getModifier() {
        return modifier;
    }
    public Double getApprovedAmount() {
        return approvedAmount;
    }
    public Boolean getCopaymentAlreadyAdded() {
        return copaymentAlreadyAdded;
    }
    public Boolean getBilled() {
        return billed;
    }

    public void setBilled(Boolean billed) {
        this.billed = billed;
    }

    public Double getPatientShareVat() {
        return patientShareVat;
    }

    public void setPatientShareVat(Double patientShareVat) {
        this.patientShareVat = patientShareVat;
    }

    public void setCopaymentAlreadyAdded(Boolean copaymentAlreadyAdded) {
        this.copaymentAlreadyAdded = copaymentAlreadyAdded;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Boolean getCovered() {
        return covered;
    }
    public void setCovered(Boolean covered) {
        this.covered = covered;
    }
    public Double getSaudiVat() {
        return saudiVat;
    }
    public Double getSaudiVatPercentage() {
        return saudiVatPercentage;
    }

    public Double getDiscountPercentageProcedure() {
        return discountPercentageProcedure;
    }

    public void setDiscountPercentageProcedure(Double discountPercentageProcedure) {
        this.discountPercentageProcedure = discountPercentageProcedure;
    }
    public Double getPatientShareDiscountPercentage() {
        return patientShareDiscountPercentage;
    }

    public void setPatientShareDiscountPercentage(Double patientShareDiscountPercentage) {
        this.patientShareDiscountPercentage = patientShareDiscountPercentage;
    }

    public void setSaudiVatPercentage(Double saudiVatPercentage) {
        this.saudiVatPercentage = saudiVatPercentage;
    }
    public void setSaudiVat(Double saudiVat) {
        this.saudiVat = saudiVat;
    }
    public Double getRejectedQuantity() {
        return rejectedQuantity;
    }

    public void setRejectedQuantity(Double rejectedQuantity) {
        this.rejectedQuantity = rejectedQuantity;
    }
    public Double getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(Double pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public void setApprovedAmount(Double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
    public Double getNoneSaudiVat() {
        return noneSaudiVat;
    }
    public Double getRejectedAmount() {
        return rejectedAmount;
    }

    public Double getGrossPlusVat() {
        return grossPlusVat;
    }

    public void setGrossPlusVat(Double grossPlusVat) {
        this.grossPlusVat = grossPlusVat;
    }
    public void setRejectedAmount(Double rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public void setNoneSaudiVat(Double noneSaudiVat) {
        this.noneSaudiVat = noneSaudiVat;
    }

    public Double getNetAmountVat() {
        return netAmountVat;
    }
    public Double getNoneSaudiVatPercentage() {
        return noneSaudiVatPercentage;
    }

    public void setNoneSaudiVatPercentage(Double noneSaudiVatPercentage) {
        this.noneSaudiVatPercentage = noneSaudiVatPercentage;
    }

    public void setNetAmountVat(Double netAmountVat) {
        this.netAmountVat = netAmountVat;
    }

    public Boolean getNeedApproval() {
        return needApproval;
    }

    public void setNeedApproval(Boolean needApproval) {
        this.needApproval = needApproval;
    }


    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Integer getSrvitmslnum() {
        return srvitmslnum;
    }

    public void setSrvitmslnum(Integer srvitmslnum) {
        this.srvitmslnum = srvitmslnum;
    }

    public String getCustom_Price_Types() {
        return custom_Price_Types;
    }
    public void setCustom_Price_Types(String custom_Price_Types) {
        this.custom_Price_Types = custom_Price_Types;
    }
    public Activity() {
    }
    public String getActivityID() {
        return activityID;
    }
    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }
    public List<ActivityOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<ActivityOutcome> outcome) {
        this.outcome = outcome;
    }
    public Integer getId() {
        return id;
    }
    public Double getSpecialDiscountAmount() {
        return specialDiscountAmount;
    }
    public void setSpecialDiscountAmount(Double specialDiscountAmount) {
        this.specialDiscountAmount = specialDiscountAmount;
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
    public Double getSPCFactor() {
        return SPCFactor;
    }
    public void setSPCFactor(Double SPCFactor) {
        this.SPCFactor = SPCFactor;
    }
    public Double getDiscount() {
        return discount;
    }
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public CodeType getType() {
        return type;
    }
    public void setType(CodeType type) {
        this.type = type;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Double getQuantity() {
        return quantity;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public Double getNet() {
        return net;
    }
    public void setNet(Double net) {
        this.net = net;
    }
    public Double getProviderNet() {
        return providerNet;
    }
    public void setProviderNet(Double providerNet) {
        this.providerNet = providerNet;
    }
    public String getClinician() {
        return clinician;
    }
    public void setClinician(String clinician) {
        this.clinician = clinician;
    }
    public String getOrderingClinician() {
        return orderingClinician;
    }
    public void setOrderingClinician(String orderingClinician) {
        this.orderingClinician = orderingClinician;
    }
    public String getPriorAuthorizationID() {
        return priorAuthorizationID;
    }
    public void setPriorAuthorizationID(String priorAuthorizationID) {
        this.priorAuthorizationID = priorAuthorizationID;
    }
    public Double getList() {
        return list;
    }
    public void setList(Double list) {
        this.list = list;
    }
    public Double getGross() {
        return gross;
    }
    public Double getGrossMinusDeductible() {
        if (deductible != null && !deductible.isNaN() && deductible > 0d) {
            return gross - deductible;
        }
        return gross;
    }
    public void setGross(Double gross) {
        this.gross = gross;
    }
    public Double getTotalPatientShare() {
        return totalPatientShare;
    }

    public void setTotalPatientShare(Double totalPatientShare) {
        this.totalPatientShare = totalPatientShare;
    }

    public Double getTotalNetAmount() {
        return totalNetAmount;
    }
    public Double getDiscountProcedure() {
        return discountProcedure;
    }

    public void setDiscountProcedure(Double discountProcedure) {
        this.discountProcedure = discountProcedure;
    }

    public void setTotalNetAmount(Double totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }
    public Double getPatientShareDiscountAmount() {
        return patientShareDiscountAmount;
    }

    public void setPatientShareDiscountAmount(Double patientShareDiscountAmount) {
        this.patientShareDiscountAmount = patientShareDiscountAmount;
    }
    public Double getPatientShare() {
        return patientShare;
    }
    public void setPatientShare(Double patientShare) {
        this.patientShare = patientShare;
    }
    public Double getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    public Double getRejected_Amount() {
        return rejected_Amount;
    }
    public void setRejected_Amount(Double rejected_Amount) {
        this.rejected_Amount = rejected_Amount;
    }
    public String getDenialCode() {
        return denialCode;
    }
    public void setDenialCode(String denialCode) {
        this.denialCode = denialCode;
    }
    public Double getCopayment() {
        return copayment;
    }
    public void setCopayment(Double copayment) {
        this.copayment = copayment;
    }
    public Double getDeductible() {
        return deductible;
    }
    public void setDeductible(Double deductible) {
        this.deductible = deductible;
    }
    public Boolean getManualPrices() {
        return manualPrices;
    }
    public void setManualPrices(Boolean manualPrices) {
        this.manualPrices = manualPrices;
    }
    public Integer getProcedureType() {
        return procedureType;
    }
    public void setProcedureType(Integer procedureType) {
        this.procedureType = procedureType;
    }
    public List<Observation> getObservation() {
        return observation;
    }
    public void setObservation(List<Observation> observation) {
        this.observation = observation;
    }
    public Claim getClaimID() {
        return claimID;
    }
    public void setClaimID(Claim claimID) {
        this.claimID = claimID;
    }
    public void addSingleRuleOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        ActivityOutcome aout = new ActivityOutcome(severity, ruleName, ShortMsg, longMsg);
        if (!isExists(aout)) {
            this.addOutcome(severity, ruleName, ShortMsg, longMsg);
        }
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        longMsg = !longMsg.isEmpty() ? longMsg +" ," + this.toString() : this.toString();
        if (this.outcome != null) {
            this.outcome.add(new ActivityOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<ActivityOutcome>();
            this.outcome.add(new ActivityOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
    private boolean isExists(ActivityOutcome aout) {
        if (outcome != null) {
            for (ActivityOutcome tmp : outcome) {
                if (tmp.getRuleName().equals(aout.getRuleName())) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean addActivityGroup(Integer id, Integer type, String name) {
        if (activityGroup != null) {
            for (ActivityGroup ag : activityGroup) {
                if (ag.getId().equals(id)) {
                    return false;
                }
            }
        }
        if (this.activityGroup != null) {
            this.activityGroup.add(new ActivityGroup(id, type, name, this));
        } else {
            this.activityGroup = new ArrayList<ActivityGroup>();
            this.activityGroup.add(new ActivityGroup(id, type, name, this));
        }
        return true;
    }
    public Integer getProviderType() {
        return providerType;
    }
    public void setProviderType(Integer providerType) {
        this.providerType = providerType;
    }
    public String getProviderCode() {
        return providerCode;
    }
    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }
    public Double getProviderPatientShare() {
        return providerPatientShare;
    }
    public void setProviderPatientShare(Double providerPatientShare) {
        this.providerPatientShare = providerPatientShare;
    }
    public List<ActivityGroup> getActivityGroup() {
        return activityGroup;
    }
    public void setActivityGroup(List<ActivityGroup> activityGroup) {
        this.activityGroup = activityGroup;
    }
    public Integer getDrugType() {
        return drugType;
    }
    public void setDrugType(Integer drugType) {
        this.drugType = drugType;
    }
    public Double getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    public boolean isMemberOf(CodeType groupType, String groupName) {
        if (activityGroup == null || activityGroup.isEmpty()) {
            return false;
        }
        for (ActivityGroup group : activityGroup) {
            if (group.getType() == groupType.getValue() && group.getName().equals(groupName)) {
                return true;
            }
        }
        return false;
    }
    public Integer getTimeUnits() {
        return timeUnits;
    }
    public void setTimeUnits(Integer timeUnits) {
        this.timeUnits = timeUnits;
    }
    public Double getAnaesthesiaBaseUnits() {
        return anaesthesiaBaseUnits;
    }
    public void setAnaesthesiaBaseUnits(Double anaesthesiaBaseUnits) {
        this.anaesthesiaBaseUnits = anaesthesiaBaseUnits;
    }
    @Override
    public String toString() {
        return "For Activity{" + "id=" + idCaller + ", code=" + code + '}';
    }
    public Double getEX_PBP() {
        return EX_PBP;
    }
    public void setEX_PBP(Double EX_PBP) {
        this.EX_PBP = EX_PBP;
    }
    public Integer getListPricePredifined() {
        return listPricePredifined;
    }
    public void setListPricePredifined(Integer listPricePredifined) {
        this.listPricePredifined = listPricePredifined;
    }
    public Double getDrg_weight() {
        return drg_weight;
    }
    public void setDrg_weight(Double drg_weight) {
        this.drg_weight = drg_weight;
    }
    public Double getSpecialDiscountPercentage() {
        return specialDiscountPercentage;
    }
    public void setSpecialDiscountPercentage(Double specialDiscountPercentage) {
        this.specialDiscountPercentage = specialDiscountPercentage;
    }
    public Integer getPackageGroupID() {
        return packageGroupID;
    }

    public void setPackageGroupID(Integer packageGroupID) {
        this.packageGroupID = packageGroupID;
    }

    public String getServiceItemSerialNumber() {
        return serviceItemSerialNumber;
    }

    public void setServiceItemSerialNumber(String serviceItemSerialNumber) {
        this.serviceItemSerialNumber = serviceItemSerialNumber;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getDenyDisc() {
        return denyDisc;
    }

    public void setDenyDisc(Double denyDisc) {
        this.denyDisc = denyDisc;
    }

    public Double getHike() {
        return hike;
    }

    public void setHike(Double hike) {
        this.hike = hike;
    }

    public Double getHikeAmount() {
        return hikeAmount;
    }

    public void setHikeAmount(Double hikeAmount) {
        this.hikeAmount = hikeAmount;
    }

    public Double getDeductibleOn() {
        return deductibleOn;
    }

    public void setDeductibleOn(Double deductibleOn) {
        this.deductibleOn = deductibleOn;
    }

    public Double getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(Double deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public Double getDeductibleType() {
        return deductibleType;
    }

    public void setDeductibleType(Double deductibleType) {
        this.deductibleType = deductibleType;
    }

    public Double getMaxdeductibleAmt() {
        return maxdeductibleAmt;
    }

    public void setMaxdeductibleAmt(Double maxdeductibleAmt) {
        this.maxdeductibleAmt = maxdeductibleAmt;
    }

    public Double getMaxCoverage() {
        return maxCoverage;
    }

    public void setMaxCoverage(Double maxCoverage) {
        this.maxCoverage = maxCoverage;
    }

    public Double getCoveragePeriod() {
        return coveragePeriod;
    }

    public void setCoveragePeriod(Double coveragePeriod) {
        this.coveragePeriod = coveragePeriod;
    }
    public UUID getPriceActivityId() {
        return priceActivityId;
    }

    public void setPriceActivityId(UUID priceActivityId) {
        this.priceActivityId = priceActivityId;
    }
}
