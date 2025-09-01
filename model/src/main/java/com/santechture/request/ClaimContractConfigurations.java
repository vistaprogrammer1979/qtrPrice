package com.santechture.request;

import java.io.Serializable;


public class ClaimContractConfigurations implements Serializable {

    int admissionType;
    private Long id;
    private int groupType;
    private Float discount;
    private Double copayment;
    private Float deductible;
    private Double maxPatientShare;
    private boolean ip;
    private boolean op;
    private Long cusContractId;
    private Double claimTotal;
    private Double saudiVat;
    private Double priorApprovalLimit;
    private Double noneSaudiVat;
    private Double claimNetAmount;

    public ClaimContractConfigurations() {
    }

    public ClaimContractConfigurations(Long id) {
        this.id = id;
    }

    public ClaimContractConfigurations(Long id, int groupType, boolean ip, boolean op) {
        this.id = id;
        this.groupType = groupType;
        this.ip = ip;
        this.op = op;
    }

    public ClaimContractConfigurations(int groupType, Float discount, Double copayment, Float deductible, Double maxPatientShare, boolean ip, boolean op) {
        this.groupType = groupType;
        this.discount = discount;
        this.copayment = copayment;
        this.deductible = deductible;
        this.maxPatientShare = maxPatientShare;
        this.ip = ip;
        this.op = op;
    }

    public Double getClaimNetAmount() {
        return claimNetAmount;
    }

    public void setClaimNetAmount(Double claimNetAmount) {
        this.claimNetAmount = claimNetAmount;
    }

    public Double getSaudiVat() {
        return saudiVat;
    }

    public void setSaudiVat(Double saudiVat) {
        this.saudiVat = saudiVat;
    }

    public Double getPriorApprovalLimit() {
        return priorApprovalLimit;
    }

    public void setPriorApprovalLimit(Double priorApprovalLimit) {
        this.priorApprovalLimit = priorApprovalLimit;
    }

    public Double getNoneSaudiVat() {
        return noneSaudiVat;
    }

    public void setNoneSaudiVat(Double noneSaudiVat) {
        this.noneSaudiVat = noneSaudiVat;
    }

    public Double getClaimTotal() {
        return claimTotal;
    }

    public void setClaimTotal(Double claimTotal) {
        this.claimTotal = claimTotal;
    }

    public Long getCusContractId() {
        return cusContractId;
    }

    public void setCusContractId(Long cusContractId) {
        this.cusContractId = cusContractId;
    }

    public int getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType(int admissionType) {
        ip = false;
        op = false;
        if (admissionType == 0) {
            ip = true;
            op = true;
        } else if (admissionType == 1) {
            ip = true;
            op = false;
        } else if (admissionType == 2) {
            ip = false;
            op = true;
        }
        this.admissionType = admissionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Double getCopayment() {
        return copayment;
    }

    public void setCopayment(Double copayment) {
        this.copayment = copayment;
    }

    public Float getDeductible() {
        return deductible;
    }

    public void setDeductible(Float deductible) {
        this.deductible = deductible;
    }

    public Double getMaxPatientShare() {
        return maxPatientShare;
    }

    public void setMaxPatientShare(Double maxPatientShare) {
        this.maxPatientShare = maxPatientShare;
    }

    public boolean getIp() {
        return ip;
    }

    public void setIp(boolean ip) {
        this.ip = ip;
    }

    public boolean getOp() {
        return op;
    }

    public void setOp(boolean op) {
        this.op = op;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClaimContractConfigurations other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "hhhh.PLCUSContractConfigurations[ id=" + id + " ]";
    }

}


