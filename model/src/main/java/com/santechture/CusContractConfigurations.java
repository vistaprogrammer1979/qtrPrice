/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.santechture;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * @author waltasseh
 */
public class CusContractConfigurations implements Serializable {

    @JsonProperty(value = "ID")
    private Long id;
    @JsonProperty(value = "GroupType")
    private int groupType;
    @JsonProperty(value = "discount")
    private Float discount;
    @JsonProperty(value = "copayment")
    private Double copayment;
    @JsonProperty(value = "deductible")
    private Float deductible;
    @JsonProperty(value = "MaxPatientShare")
    private Double maxPatientShare;
    @JsonProperty(value = "IP")
    private boolean ip;
    @JsonProperty(value = "OP")
    private boolean op;

    int admissionType;
    @JsonProperty(value = "cusContractId")
   private Long cusContractId;
    @JsonProperty(value = "claimTotal")
   private Double claimTotal;
    @JsonProperty(value = "SaudiVat")
       private Double saudiVat;
    @JsonProperty(value = "priorApprovalLimit")
  private Double priorApprovalLimit;

    public Double getPriorApprovalLimit() {
        return priorApprovalLimit;
    }

    public void setPriorApprovalLimit(Double priorApprovalLimit) {
        this.priorApprovalLimit = priorApprovalLimit;
    }
    public Double getSaudiVat() {
        return saudiVat;
    }

    public void setSaudiVat(Double saudiVat) {
        this.saudiVat = saudiVat;
    }

    public Double getNoneSaudiVat() {
        return noneSaudiVat;
    }

    public void setNoneSaudiVat(Double noneSaudiVat) {
        this.noneSaudiVat = noneSaudiVat;
    }
    private Double noneSaudiVat;

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

    public CusContractConfigurations() {
    }

    public CusContractConfigurations(Long id) {
        this.id = id;
    }

    public CusContractConfigurations(Long id, int groupType, boolean ip, boolean op) {
        this.id = id;
        this.groupType = groupType;
        this.ip = ip;
        this.op = op;
    }

    public CusContractConfigurations(int groupType, Float discount, Double copayment, Float deductible, Double maxPatientShare, boolean ip, boolean op) {
        this.groupType = groupType;
        this.discount = discount;
        this.copayment = copayment;
        this.deductible = deductible;
        this.maxPatientShare = maxPatientShare;
        this.ip = ip;
        this.op = op;
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
        if (!(object instanceof CusContractConfigurations)) {
            return false;
        }
        CusContractConfigurations other = (CusContractConfigurations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hhhh.PLCUSContractConfigurations[ id=" + id + " ]";
    }

}
