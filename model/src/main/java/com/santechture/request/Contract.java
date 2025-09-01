package com.santechture.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="PackageName")
    private String packageName;
    @JsonProperty(value ="Network")
    private String network;
    @JsonProperty(value ="SubNetworkName")
    private String subNetworkName;
    @JsonProperty(value ="Policy")
    private String policy;
    @JsonProperty(value ="CompanyCode")
    private String companyCode;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @OneToOne
    @JsonIgnore
    private Claim claimID;
    @OneToMany(mappedBy = "contract", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<ContractOutcome> outcome;
    @JsonProperty(value = "ClassName")
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Contract() {
    }
    public Contract(Integer id) {
        this.id = id;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getNetwork() {
        return network;
    }
    public void setNetwork(String network) {
        this.network = network;
    }
    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;}
    public String getSubNetworkName() {
        return subNetworkName;
    }
    public void setSubNetworkName(String subNetworkName) {
        this.subNetworkName = subNetworkName;
    }
    public String getPolicy() {
        return policy;
    }
    public void setPolicy(String policy) {
        this.policy = policy;
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
    public List<ContractOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<ContractOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new ContractOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<ContractOutcome>();
            this.outcome.add(new ContractOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
}
