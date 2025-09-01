package com.santechture.request;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Diagnosis implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="idCaller", required = false)
    private Integer idCaller;
    @JsonProperty(value ="Type", required = true)
    private String type;
    @JsonProperty(value ="Code", required = true)
    private String code;
    @JsonProperty(value ="providerType", required = false)
    private String providerType;
    @JsonProperty(value ="providerCode", required = false)
    private String providerCode;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Claim claimID;
    @OneToMany(mappedBy = "diagnosis", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<DiagnosisOutcome> outcome;
    public Diagnosis() {
    }
    public Diagnosis(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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
    public List<DiagnosisOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<DiagnosisOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new DiagnosisOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<DiagnosisOutcome>();
            this.outcome.add(new DiagnosisOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
    public Integer getIdCaller() {
        return idCaller;
    }
    public void setIdCaller(Integer idCaller) {
        this.idCaller = idCaller;
    }
    public String getProviderType() {
        return providerType;
    }
    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }
    public String getProviderCode() {
        return providerCode;
    }
    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }
}
