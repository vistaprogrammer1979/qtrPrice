package com.santechture.request.ksa;

import com.fasterxml.jackson.annotation.*;
import com.santechture.CusContractConfigurations;
import com.santechture.request.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@JsonRootName("Claim")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Claim implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="ID", required = true)
    private Integer rootID;
    @JsonProperty(value ="idCaller", required = false)
    private Integer idCaller;
    @JsonProperty(value ="SPC_ID", required = false)
    private Long SPC_ID;
    @JsonProperty(value ="CUS_ID", required = false)
    private Long CUS_ID;
    @JsonProperty(value ="MultipleProcedures", required = false)
    private Integer multipleProcedures;
    @JsonProperty(value ="PrimaryProc", required = false)
    private Double primaryProc;
    @JsonProperty(value ="SecondaryProc", required = false)
    private Double secondaryProc;
    @JsonProperty(value ="ThirdProc", required = false)
    private Double thirdProc;
    @JsonProperty(value ="ForthProc", required = false)
    private Double forthProc;
    @JsonProperty(value ="CUS_DENTAL_ID", required = false)
    private Long CUS_DENTAL_ID;
    @JsonProperty(value ="IDPayer")
    private String IDPayer;
    @JsonProperty(value ="MemberID")
    private String memberID;
    @JsonProperty(value ="PayerID", required = true)
    private String payerID;
    @JsonProperty(value ="ReceiverID", required = true)
    private String receiverID;
    @JsonProperty(value ="ProviderID", required = true)
    private String providerID;
    @JsonProperty(value ="EmiratesIDNumber", required = true)
    private String emiratesIDNumber;
    @JsonProperty(value ="Gross")
    private Double gross;
    @JsonProperty(value ="PatientShare")
    private Double patientShare;
    @JsonProperty(value ="Net")
    private Double net;
    @OneToMany(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="AppliedDeductible", required = false)
    private List<AppliedDeductible> appliedDeductible;
    @OneToMany(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="AppliedCopayment", required = false)
    private List<AppliedCopayment> appliedCopayment;
    @JsonProperty(value ="ProviderInvoiceAmount")
    private Double providerInvoiceAmount;
    @JsonProperty(value ="DenialCode")
    private String denialCode;
    @JsonProperty(value ="PaymentReference")
    private String paymentReference;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="DateSettlement", required = false)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date dateSettlement;
    @JsonProperty(value ="Pending")
    private Boolean pending;
    @JsonProperty(value ="Imported")
    private Boolean imported;
    @JoinColumn(name = "request", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Request request;
    @OneToOne(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Contract")
    private Contract contract;
    @OneToOne(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Resubmission")
    private Resubmission resubmission;
    @OneToMany(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Activity", required = true)
    private List<Activity> activity;
    @OneToMany(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="ClaimType", required = true)
    private List<ClaimType> claimType;
    @OneToMany(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Diagnosis", required = true)
    private List<Diagnosis> diagnosis;
    @OneToMany(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Encounter", required = true)
    private List<Encounter> encounter;
    @OneToOne(mappedBy = "claimID", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Patient")
    private Patient patient;
    @JsonProperty(value ="LogInfo")
    private Boolean logInfo;
    @JsonProperty(value = "rolesTypes", required = false)
    private String rolesTypes;
    @JsonProperty(value = "principalDiagnosisNotCovered")
    private Boolean principalDiagnosisNotCovered;
    @OneToMany(mappedBy = "claim", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<ClaimOutcome> outcome;
    @JsonProperty(value = "visitId", required = false)
    private String visitId;

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    List<ClaimContractConfigurations> copaymentContractlist = new ArrayList<ClaimContractConfigurations>();
    public Claim() {
        this.appliedCopayment = new ArrayList();
        this.appliedDeductible = new ArrayList();
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Boolean getPrincipalDiagnosisNotCovered() {
        return principalDiagnosisNotCovered;
    }

    public void setPrincipalDiagnosisNotCovered(Boolean principalDiagnosisNotCovered) {
        this.principalDiagnosisNotCovered = principalDiagnosisNotCovered;
    }
    public Integer getRootID() {
        return rootID;
    }
    public void setRootID(Integer rootID) {
        this.rootID = rootID;
    }
    public Integer getIdCaller() {
        return idCaller;
    }
    public void setIdCaller(Integer idCaller) {
        this.idCaller = idCaller;
    }
    public Long getSPC_ID() {
        return SPC_ID;
    }
    public void setSPC_ID(Long SPC_ID) {
        this.SPC_ID = SPC_ID;
    }
    public Long getCUS_ID() {
        return CUS_ID;
    }
    public void setCUS_ID(Long CUS_ID) {
        this.CUS_ID = CUS_ID;
    }
    public Long getCUS_DENTAL_ID() {
        return CUS_DENTAL_ID;
    }
    public void setCUS_DENTAL_ID(Long CUS_DENTAL_ID) {
        this.CUS_DENTAL_ID = CUS_DENTAL_ID;
    }
    public String getIDPayer() {
        return IDPayer;
    }
    public void setIDPayer(String IDPayer) {
        this.IDPayer = IDPayer;
    }
    public String getMemberID() {
        return memberID;
    }
    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }
    public String getPayerID() {
        return payerID;
    }
    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }
    public String getReceiverID() {
        return receiverID;
    }
    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
    public String getProviderID() {
        return providerID;
    }
    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }
    public String getEmiratesIDNumber() {
        return emiratesIDNumber;
    }
    public void setEmiratesIDNumber(String emiratesIDNumber) {
        this.emiratesIDNumber = emiratesIDNumber;
    }
    public Double getGross() {
        return gross;
    }
    public void setGross(Double gross) {
        this.gross = gross;
    }
    public Double getPatientShare() {
        return patientShare;
    }
    public void setPatientShare(Double patientShare) {
        this.patientShare = patientShare;
    }
    public Double getNet() {
        return net;
    }
    public void setNet(Double net) {
        this.net = net;
    }
    public void addAppliedDeductible(AppliedDeductible ded){
        appliedDeductible.add(ded);
    }
    public void addAppliedCopayment(AppliedCopayment copay){
        appliedCopayment.add(copay);
    }
    public List<AppliedDeductible> getAppliedDeductible() {
        return appliedDeductible;
    }
    public void setAppliedDeductible(List<AppliedDeductible> appliedDeductible) {
        this.appliedDeductible = appliedDeductible;
    }
    public List<AppliedCopayment> getAppliedCopayment() {
        return appliedCopayment;
    }
    public void setAppliedCopayment(List<AppliedCopayment> appliedCopayment) {
        this.appliedCopayment = appliedCopayment;
    }
    public Double getProviderInvoiceAmount() {
        return providerInvoiceAmount;
    }
    public void setProviderInvoiceAmount(Double providerInvoiceAmount) {
        this.providerInvoiceAmount = providerInvoiceAmount;
    }
    public String getDenialCode() {
        return denialCode;
    }
    public void setDenialCode(String denialCode) {
        this.denialCode = denialCode;
    }
    public String getPaymentReference() {
        return paymentReference;
    }
    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
    public Date getDateSettlement() {
        return dateSettlement;
    }
    public void setDateSettlement(Date dateSettlement) {
        this.dateSettlement = dateSettlement;
    }
    public Boolean getPending() {
        return pending;
    }
    public void setPending(Boolean pending) {
        this.pending = pending;
    }
    public Boolean getImported() {
        return imported;
    }
    public void setImported(Boolean imported) {
        this.imported = imported;
    }
    public Request getRequest() {
        return request;
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    public Contract getContract() {
        return contract;
    }
    public void setContract(Contract contract) {
        this.contract = contract;
    }
    public Resubmission getResubmission() {
        return resubmission;
    }
    public void setResubmission(Resubmission resubmission) {
        this.resubmission = resubmission;
    }
    public List<Activity> getActivity() {
        return activity;
    }
    public void setActivity(List<Activity> activity) {
        this.activity = activity;
    }
    public List<ClaimType> getClaimType() {
        return claimType;
    }
    public void addClaimType(ClaimType cType) {
        if(claimType == null){
            claimType = new ArrayList();
        }
        claimType.add(cType);
    }
    public boolean containsAnyCusContractCopyament(int groupType) {
        //System.out.println("groupType" + groupType);
        for (ClaimContractConfigurations claimContractConfigurations : copaymentContractlist) {
            if (groupType == claimContractConfigurations.getGroupType()) {
                return true;
            }
        }

        return false;
    }
    public void cloneContractCopaymentToClaim(CusContractConfigurations CusContractConfiguration) {
        //System.out.println("adding new contract " + CusContractConfiguration);
        ClaimContractConfigurations c = new ClaimContractConfigurations();
        c.setCopayment(CusContractConfiguration.getCopayment());
        c.setMaxPatientShare(CusContractConfiguration.getMaxPatientShare());
        c.setDeductible(CusContractConfiguration.getDeductible());
        c.setDiscount(CusContractConfiguration.getDiscount());
        c.setCusContractId(CusContractConfiguration.getCusContractId());
        c.setGroupType(CusContractConfiguration.getGroupType());
        c.setId(CusContractConfiguration.getId());
        c.setOp(CusContractConfiguration.getOp());
        c.setIp(CusContractConfiguration.getIp());
        c.setSaudiVat(CusContractConfiguration.getSaudiVat());
        c.setNoneSaudiVat(CusContractConfiguration.getNoneSaudiVat());
        c.setPriorApprovalLimit(CusContractConfiguration.getPriorApprovalLimit());
        copaymentContractlist.add(c);

    }

    public void setClaimType(List<ClaimType> claimType) {
        this.claimType = claimType;
    }
    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(List<Diagnosis> diagnosis) {
        this.diagnosis = diagnosis;
    }
    public List<Encounter> getEncounter() {
        return encounter;
    }
    public void setEncounter(List<Encounter> encounter) {
        this.encounter = encounter;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public List<ClaimOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<ClaimOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new ClaimOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<ClaimOutcome>();
            this.outcome.add(new ClaimOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
    public List<ClaimContractConfigurations> getCopaymentContractlist() {
        return copaymentContractlist;
    }

    public void setCopaymentContractlist(List<ClaimContractConfigurations> copaymentContractlist) {
        this.copaymentContractlist = copaymentContractlist;
    }
    public ClaimContractConfigurations getContractByType(int groupType) {
        for (ClaimContractConfigurations claimContractConfigurations : copaymentContractlist) {
            if (groupType == claimContractConfigurations.getGroupType()) {
                return claimContractConfigurations;
            }
        }

        return new ClaimContractConfigurations();
    }
    public Boolean getLogInfo() {
        return logInfo;
    }
    public void setLogInfo(Boolean logInfo) {
        this.logInfo = logInfo;
    }
    public Integer getMultipleProcedures() {
        return multipleProcedures;
    }
    public void setMultipleProcedures(Integer multipleProcedures) {
        this.multipleProcedures = multipleProcedures;
    }
    public Double getPrimaryProc() {
        return primaryProc;
    }
    public void setPrimaryProc(Double primaryProc) {
        this.primaryProc = primaryProc;
    }
    public Double getSecondaryProc() {
        return secondaryProc;
    }
    public void setSecondaryProc(Double secondaryProc) {
        this.secondaryProc = secondaryProc;
    }
    public Double getThirdProc() {
        return thirdProc;
    }
    public void setThirdProc(Double thirdProc) {
        this.thirdProc = thirdProc;
    }
    public Double getForthProc() {
        return forthProc;
    }
    public void setForthProc(Double forthProc) {
        this.forthProc = forthProc;
    }

    public String getRolesTypes() {
        return rolesTypes;
    }

    public void setRolesTypes(String rolesTypes) {
        this.rolesTypes = rolesTypes;
    }
}
