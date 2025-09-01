/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.santechture;

import java.io.Serializable;

/**
 *
 * @author waltasseh
 */
public class PLCUSContractCodesConfigurations implements Serializable {

    public PLCUSContractCodesConfigurations(Integer id, long contractId, int type, String code, boolean covered, boolean needApproval) {
        this.id = id;
        this.contractId = contractId;
        this.type = type;
        this.code = code;
        this.covered = covered;
        this.needApproval = needApproval;
    }

    private Integer id;
    private Long contractId;
    private int type;
    private String code;
    private boolean covered;
    private boolean needApproval;
 

    public PLCUSContractCodesConfigurations() {
    }

    public PLCUSContractCodesConfigurations(Integer id) {
        this.id = id;
    }

    public PLCUSContractCodesConfigurations(Integer id, Long contractId, int type, String code) {
        this.id = id;
        this.contractId = contractId;
        this.type = type;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

 

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public boolean isNeedApproval() {
        return needApproval;
    }

    public void setNeedApproval(boolean needApproval) {
        this.needApproval = needApproval;
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
        if (!(object instanceof PLCUSContractCodesConfigurations)) {
            return false;
        }
        PLCUSContractCodesConfigurations other = (PLCUSContractCodesConfigurations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "azazaz.PLCUSContractCodesConfigurations[ id=" + id + " ]";
    }

}
